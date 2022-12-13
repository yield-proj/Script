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

package com.xebisco.yieldscript.interpreter;

import com.xebisco.yieldscript.interpreter.info.ProjectInfo;
import com.xebisco.yieldscript.interpreter.instruction.*;
import com.xebisco.yieldscript.interpreter.memory.Function;
import com.xebisco.yieldscript.interpreter.instruction.IfStatement;
import com.xebisco.yieldscript.interpreter.type.Type;
import com.xebisco.yieldscript.interpreter.type.TypeModifier;
import com.xebisco.yieldscript.interpreter.utils.Pair;
import com.xebisco.yieldscript.interpreter.utils.ScriptUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

public class ExecutableCreator implements IExecutableCreator {

    private List<Function> functionsLayer = new ArrayList<>();
    private boolean ignoreFunctions = false;

    @Override
    public Executable create(String source, ProjectInfo projectInfo) {
        Executable out = null;
        boolean exitedGlobal = false;
        Matcher matcher = Constants.SET_AS_PATTERN.matcher(source);
        while (matcher.matches()) {
            source = source.substring(source.indexOf('=') + 1);
            matcher = Constants.SET_AS_PATTERN.matcher(source);
        }
        if (source.hashCode() == "".hashCode())
            return null;
        matcher.usePattern(Constants.INT_FOR_EACH_PATTERN);
        if (matcher.matches()) {
            ForStatement forStatement = new ForStatement(matcher.group(1), matcher.group(2), "index=(com.xebisco.yieldscript.interpreter.utils.MathUtils)addInt(index, 1)");
            forStatement.setModifiers(TypeModifier._none);
            if (functionsLayer.size() == 0) exitedGlobal = true;
            functionsLayer.add(forStatement);
            out = forStatement;
        } else {
            matcher.usePattern(Constants.FUNCTION_PATTERN);
            if (matcher.matches()) {
                String[] args = matcher.group(2).split(",");
                if (args.length == 1 && args[0].hashCode() == "".hashCode()) args = new String[0];
                String[] argumentsNames = new String[args.length];
                Class<?>[] argumentsTypes = new Class<?>[args.length];
                for (int i = 0; i < args.length; i++) {
                    String[] pcs = args[i].split(":");
                    argumentsNames[i] = pcs[0];
                    argumentsTypes[i] = Type.getType(pcs[1]).getJavaClass();
                }
                Function function = new Function(matcher.group(1), argumentsTypes, argumentsNames, Type.getType(matcher.group(3)).getJavaClass());
                function.setModifiers();
                if (functionsLayer.size() == 0) exitedGlobal = true;
                functionsLayer.add(function);
                out = function;
            } else {
                matcher.usePattern(Constants.FUNCTION_WITH_MODIFIERS_PATTERN);
                if (matcher.matches()) {
                    String[] args = matcher.group(2).split(",");
                    if (args.length == 1 && args[0].hashCode() == "".hashCode())
                        args = new String[0];
                    String[] argumentsNames = new String[args.length];
                    Class<?>[] argumentsTypes = new Class<?>[args.length];
                    for (int i = 0; i < args.length; i++) {
                        String[] pcs = args[i].split(":");
                        argumentsNames[i] = pcs[0];
                        argumentsTypes[i] = Type.getType(pcs[1]).getJavaClass();
                    }
                    Function function = new Function(matcher.group(1), argumentsTypes, argumentsNames, Type.getType(matcher.group(3)).getJavaClass());
                    String[] mods = matcher.group(4).split(",");
                    if (mods.length == 1 && mods[0].hashCode() == "".hashCode())
                        mods = new String[0];
                    TypeModifier[] modifiers = new TypeModifier[mods.length];
                    for (int i = 0; i < modifiers.length; i++)
                        modifiers[i] = TypeModifier.getModifier(mods[i]);
                    function.setModifiers(modifiers);
                    if (functionsLayer.size() == 0) exitedGlobal = true;
                    functionsLayer.add(function);
                    out = function;
                }
            }
        }
        if (out == null) {
            matcher = Constants.DECLARATION_PATTERN.matcher(source);
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
                            matcher.usePattern(Constants.DECLARATION_PATTERN_AUTO_TYPE);
                            if (matcher.matches()) {
                                String[] mods = matcher.group(3).split(",");
                                if (mods.length == 1 && mods[0].hashCode() == "".hashCode())
                                    mods = new String[0];
                                TypeModifier[] modifiers = new TypeModifier[mods.length];
                                for (int i = 0; i < modifiers.length; i++)
                                    modifiers[i] = TypeModifier.getModifier(mods[i]);
                                out = new VariableDeclaration(matcher.group(1), matcher.group(2), null, modifiers);
                            } else {
                                /*matcher.usePattern(Constants.INT_FOR_EACH_PATTERN);
                                if (matcher.matches()) {
                                    out = new VariableDeclaration(matcher.group(1), matcher.group(2), null, modifiers);
                                } else {*/
                                matcher.usePattern(Constants.RETURN_PATTERN);
                                if (matcher.matches()) {
                                    Function f = functionsLayer.get(functionsLayer.size() - 1);
                                    if (f instanceof IfStatement || f instanceof ForStatement) {
                                        for (int i = functionsLayer.size() - 2; i >= 0; i--) {
                                            f = functionsLayer.get(i);
                                            if (!(f instanceof IfStatement || f instanceof ForStatement)) break;
                                        }
                                    }
                                    out = new ReturnDeclaration(f, matcher.group(1));
                                } else {
                                    matcher.usePattern(Constants.CLOSE_CURLY_BRACES_PATTERN);
                                    if (matcher.matches()) {
                                        functionsLayer.remove(functionsLayer.size() - 1);
                                        return null;
                                    } else {
                                        matcher.usePattern(Constants.ATTACH_PATTERN);
                                        if (matcher.matches()) {
                                            out = new AttachScript(ScriptUtils.createScript(Script.class.getResourceAsStream(projectInfo.getProjectPath() + matcher.group(1)), projectInfo));
                                        } else {
                                            out = ScriptUtils.methodCall(source, null);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        List<String> toSetVars = new ArrayList<>();

        if (functionsLayer.size() > 0 && !exitedGlobal && !ignoreFunctions) {
            if (out instanceof Function) {
                functionsLayer.add((Function) out);
                if (out instanceof Instruction)
                    functionsLayer.get(functionsLayer.size() - 2).getInstructions().add(new Pair<>((Instruction) out, toSetVars.toArray(new String[0])));
            } else {
                setIgnoreFunctions(true);
                Executable executable = ScriptUtils.createExecutable(this, toSetVars, source, projectInfo);
                if (executable instanceof Instruction) {
                    functionsLayer.get(functionsLayer.size() - 1).getInstructions().add(new Pair<>((Instruction) executable, toSetVars.toArray(new String[0])));
                }
                setIgnoreFunctions(false);
            }
            return null;
        }
        return out;
    }

    public List<Function> getFunctionsLayer() {
        return functionsLayer;
    }

    public void setFunctionsLayer(List<Function> functionsLayer) {
        this.functionsLayer = functionsLayer;
    }

    public boolean isIgnoreFunctions() {
        return ignoreFunctions;
    }

    public void setIgnoreFunctions(boolean ignoreFunctions) {
        this.ignoreFunctions = ignoreFunctions;
    }
}