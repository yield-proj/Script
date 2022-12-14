/*
 * Copyright [2022] [Xebisco]
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xebisco.yieldscript.interpreter.utils;

import com.xebisco.yieldscript.interpreter.Constants;
import com.xebisco.yieldscript.interpreter.IExecutableCreator;
import com.xebisco.yieldscript.interpreter.Script;
import com.xebisco.yieldscript.interpreter.exceptions.InstructionCreationException;
import com.xebisco.yieldscript.interpreter.info.ProjectInfo;
import com.xebisco.yieldscript.interpreter.instruction.Executable;
import com.xebisco.yieldscript.interpreter.instruction.Instruction;
import com.xebisco.yieldscript.interpreter.instruction.MethodCall;
import com.xebisco.yieldscript.interpreter.instruction.VariableDeclaration;
import com.xebisco.yieldscript.interpreter.memory.Bank;
import com.xebisco.yieldscript.interpreter.memory.Function;
import com.xebisco.yieldscript.interpreter.type.Type;
import com.xebisco.yieldscript.interpreter.type.TypeModifier;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScriptUtils {
    public static Script createScript(InputStream inputStream, ProjectInfo projectInfo) {
        Pair<String, Map<String, String>> pair = ParseUtils.extractStringLiterals(ParseUtils.removeComments(FileUtils.readInputStream(inputStream)));
        String[] source = ParseUtils.parseChars(ParseUtils.removeUnnecessaryWhiteSpace(pair.getFirst())).split(String.valueOf(Constants.SOURCE_BREAK));
        for (int i = 0; i < source.length; i++)
            source[i] = source[i].trim();
        return new Script(source, projectInfo, pair.getSecond());
    }

    public static Script createScript(File file, ProjectInfo projectInfo) {
        try {
            return createScript(new FileInputStream(file), projectInfo);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Pair<Instruction, String[]>> createInstructions(IExecutableCreator instructionCreator, String[] source, ProjectInfo projectInfo, Bank bank) {
        List<Pair<Instruction, String[]>> instructions = new ArrayList<>();
        List<String> toSetVars = new ArrayList<>();
        for (int i = 0; i < source.length; i++) {
            try {
                Executable executable = createExecutable(instructionCreator, toSetVars, source[i], projectInfo);
                if (executable != null) {
                    if (executable instanceof Instruction)
                        instructions.add(new Pair<>((Instruction) executable, toSetVars.toArray(new String[0])));
                    if (executable instanceof Function)
                        bank.getFunctions().put(new Pair<>(((Function) executable).getName(), com.xebisco.yieldscript.interpreter.type.ArrayList.of(((Function) executable).getArgumentsTypes())), (Function) executable);
                }
            } catch (Exception e) {
                new InstructionCreationException(e.getClass().getSimpleName() + ". in line " + (i + 1) + ": " + source[i]).printStackTrace();
                e.printStackTrace();
            }
        }
        return instructions;
    }

    public static VariableDeclaration declaration(String source) {
        Matcher matcher = Constants.DECLARATION_PATTERN.matcher(source);
        VariableDeclaration out = null;
        if (matcher.matches()) {
            String[] mods = matcher.group(4).split(",");
            if (mods.length == 1 && mods[0].hashCode() == "".hashCode()) mods = new String[0];
            TypeModifier[] modifiers = new TypeModifier[mods.length];
            for (int i = 0; i < modifiers.length; i++)
                modifiers[i] = TypeModifier.getModifier(mods[i]);
            out = new VariableDeclaration(matcher.group(1), matcher.group(3), Type.getType(matcher.group(2)), modifiers);
        } else {
            matcher.usePattern(Constants.DECLARATION_PATTERN_DEFAULT_VALUE);
            if (matcher.matches()) {
                String[] mods = matcher.group(3).split(",");
                if (mods.length == 1 && mods[0].hashCode() == "".hashCode()) mods = new String[0];
                TypeModifier[] modifiers = new TypeModifier[mods.length];
                for (int i = 0; i < modifiers.length; i++)
                    modifiers[i] = TypeModifier.getModifier(mods[i]);
                out = new VariableDeclaration(matcher.group(1), null, Type.getType(matcher.group(2)), modifiers);
            } else {
                matcher.usePattern(Constants.DECLARATION_PATTERN_NO_MODS);
                if (matcher.matches()) {
                    out = new VariableDeclaration(matcher.group(1), matcher.group(3), Type.getType(matcher.group(2)), new TypeModifier[0]);
                } else {
                    matcher.usePattern(Constants.DECLARATION_PATTERN_DEFAULT_VALUE_NO_MODS);
                    if (matcher.matches()) {
                        out = new VariableDeclaration(matcher.group(1), null, Type.getType(matcher.group(2)), new TypeModifier[0]);
                    } else {
                        matcher = Constants.DECLARATION_PATTERN_AUTO_TYPE_NO_MODS.matcher(source);
                        if (matcher.matches()) {
                            out = new VariableDeclaration(matcher.group(1), matcher.group(2), null, new TypeModifier[0]);
                        } else {
                            matcher.usePattern(Constants.DECLARATION_PATTERN_AUTO_TYPE);
                            if (matcher.matches()) {
                                String[] mods = matcher.group(3).split(",");
                                if (mods.length == 1 && mods[0].hashCode() == "".hashCode())
                                    mods = new String[0];
                                TypeModifier[] modifiers = new TypeModifier[mods.length];
                                for (int i = 0; i < modifiers.length; i++)
                                    modifiers[i] = TypeModifier.getModifier(mods[i]);
                                out = new VariableDeclaration(matcher.group(1), matcher.group(2), null, modifiers);
                            }
                        }
                    }
                }
            }
        }
        return out;
    }

    public static Executable createExecutable(IExecutableCreator instructionCreator, List<String> toSetVars, String line, ProjectInfo projectInfo) {
        toSetVars.clear();
        Matcher matcher = Constants.SET_AS_PATTERN.matcher(line);
        while (matcher.matches()) {
            toSetVars.add(matcher.group(1));
            line = line.substring(line.indexOf('=') + 1);
            matcher = Constants.SET_AS_PATTERN.matcher(line);
        }
        return instructionCreator.create(line, projectInfo);
    }

    public static void attachBank(Bank bank, Bank otherBank) {
        for (Pair<String, List<Class<?>>> pair : otherBank.getFunctions().keySet()) {
            if (otherBank.getFunctions().get(pair).getModifiers().contains(TypeModifier._get))
                bank.getFunctions().put(pair, otherBank.getFunctions().get(pair));
        }
        for (Object o : otherBank.getObjects().keySet()) {
            if (otherBank.getObjects().get(o).getModifiers().contains(TypeModifier._get))
                bank.getObjects().put(o, otherBank.getObjects().get(o));
        }
        bank.getObjects().putAll(otherBank.getObjects());
    }

    public static boolean executeInstructions(List<Pair<Instruction, String[]>> instructions, Bank bank) {
        for (Pair<Instruction, String[]> instruction : instructions) {
            try {
                Object o = instruction.getFirst().execute(bank);
                if (instruction.getSecond() != null)
                    for (String var : instruction.getSecond()) {
                        bank.getObjects().get(var).setValue(o);
                    }
                if (o instanceof StopExecution)
                    break;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    /*public static Object callFunction(String functionName, Bank bank, Object... args) {
        return bank.getFunctions().get(new Pair<>(functionName, ObjectUtils.getObjectTypes(args)));
    }*/

    @SuppressWarnings("RegExpRedundantEscape")
    public static MethodCall[] getArguments(String argsSource) {
        String[] args = Pattern.compile("(?:[^,(]|\\([^)]*\\))+").matcher(argsSource).results().map(MatchResult::group).toArray(String[]::new);
        if (args.length == 1 && args[0].hashCode() == "".hashCode()) return new MethodCall[0];
        MethodCall[] arguments = new MethodCall[args.length];
        for (int i = 0; i < arguments.length; i++) {
            arguments[i] = methodCall(args[i], null);
        }
        return arguments;
    }

    public static MethodCall methodCall(String line, MethodCall parent) {
        Class<?> clazz;
        Type returnCast = null;
        List<String> toSetVarsList = new ArrayList<>();
        Matcher matcher = Constants.SET_AS_PATTERN.matcher(line);
        while (matcher.matches()) {
            toSetVarsList.add(matcher.group(1));
            line = line.substring(line.indexOf('=') + 1);
            matcher = Constants.SET_AS_PATTERN.matcher(line);
        }
        matcher.usePattern(Constants.CAST_PATTERN);
        if (matcher.matches()) {
            returnCast = Type.getType(matcher.group(1));
            line = ParseUtils.removeEndSpaces(line.substring(0, line.lastIndexOf("as ")));
        }
        String[] toSetVars = toSetVarsList.toArray(new String[0]);
        matcher = Constants.CLASS_METHOD_CALL_PATTERN.matcher(line);
        if (matcher.matches()) {
            try {
                clazz = Class.forName(matcher.group(1));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            return new MethodCall(clazz, getArguments(matcher.group(3)), parent, matcher.group(2), toSetVars, returnCast);
        } else if (matcher.usePattern(Constants.CLASS_FIELD_PATTERN).matches()) {
            try {
                clazz = Class.forName(matcher.group(1));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            return new MethodCall(clazz, matcher.group(2), parent, toSetVars, returnCast);
        } else if (matcher.usePattern(Constants.CLASS_METHODS_CALL_PATTERN).matches()) {
            String f = matcher.group(2);
            if (matcher.group(2).contains(".")) {
                String[] pcs = matcher.group(2).split("\\(.*?\\)|(\\.)");
                MethodCall[] calls = new MethodCall[pcs.length - 1];

                for (int i = 0; i < calls.length; i++) {
                    MethodCall mParent = null;
                    if (i > 0) {
                        mParent = calls[i - 1];
                    }
                    calls[i] = methodCall("(" + matcher.group(1) + ")" + pcs[i], mParent);
                }
                parent = calls[calls.length - 1];
                f = pcs[pcs.length - 1];
            }
            return new MethodCall(null, getArguments(matcher.group(3)), parent, f, toSetVars, returnCast);
        } else if (matcher.usePattern(Constants.CLASS_FIELDS_PATTERN).matches()) {
            String f = matcher.group(2);
            if (matcher.group(2).contains(".")) {
                String[] pcs = matcher.group(2).split("\\(.*?\\)|(\\.)");
                MethodCall[] calls = new MethodCall[pcs.length - 1];

                for (int i = 0; i < calls.length; i++) {
                    MethodCall mParent = null;
                    if (i > 0) {
                        mParent = calls[i - 1];
                    }
                    calls[i] = methodCall("(" + matcher.group(1) + ")" + pcs[i], mParent);
                }
                parent = calls[calls.length - 1];
                f = pcs[pcs.length - 1];
            }
            return new MethodCall(null, f, parent, toSetVars, returnCast);
        } else if (matcher.usePattern(Constants.METHODS_CALL_PATTERN).matches()) {
            String f = matcher.group(1);
            if (matcher.group(1).contains(".")) {
                String[] pcs = matcher.group(1).split("\\(.*?\\)|(\\.)");
                MethodCall[] calls = new MethodCall[pcs.length - 1];

                for (int i = 0; i < calls.length; i++) {
                    MethodCall mParent = null;
                    if (i > 0) {
                        mParent = calls[i - 1];
                    }
                    calls[i] = methodCall(pcs[i], mParent);
                }
                parent = calls[calls.length - 1];
                f = pcs[pcs.length - 1];
            }
            return new MethodCall(null, getArguments(matcher.group(2)), parent, f, toSetVars, returnCast);
        } else if (matcher.usePattern(Constants.FIELDS_CALL_PATTERN).matches()) {
            String f = matcher.group(1);
            if (matcher.group(1).contains(".")) {
                String[] pcs = matcher.group(1).split("\\(.*?\\)|(\\.)");
                MethodCall[] calls = new MethodCall[pcs.length - 1];

                for (int i = 0; i < calls.length; i++) {
                    MethodCall mParent = null;
                    if (i > 0) {
                        mParent = calls[i - 1];
                    }
                    calls[i] = methodCall(pcs[i], mParent);
                }
                parent = calls[calls.length - 1];
                f = pcs[pcs.length - 1];
            }
            return new MethodCall(null, f, parent, toSetVars, returnCast);
        } else {
            return new MethodCall(null, line, parent, toSetVars, returnCast);
        }
    }

}
