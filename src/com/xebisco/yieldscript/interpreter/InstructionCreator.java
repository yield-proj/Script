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

import com.xebisco.yieldscript.interpreter.exceptions.SyntaxException;
import com.xebisco.yieldscript.interpreter.instruction.MethodCall;
import com.xebisco.yieldscript.interpreter.instruction.VariableDeclaration;
import com.xebisco.yieldscript.interpreter.instruction.Instruction;
import com.xebisco.yieldscript.interpreter.memory.Function;
import com.xebisco.yieldscript.interpreter.memory.Variable;
import com.xebisco.yieldscript.interpreter.type.Type;
import com.xebisco.yieldscript.interpreter.type.TypeModifier;
import com.xebisco.yieldscript.interpreter.utils.ScriptUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

public class InstructionCreator implements IInstructionCreator {

    private List<Function> functionsLayer = new ArrayList<>();

    @Override
    public Instruction create(String source) {
        Instruction out = null;
        boolean exitedGlobal = false;
        Matcher matcher = Constants.DECLARATION_PATTERN.matcher(source);
        if (matcher.matches()) {
            String[] mods = matcher.group(4).split(",");
            if (mods.length == 1 && mods[0].hashCode() == "".hashCode()) mods = new String[0];
            TypeModifier[] modifiers = new TypeModifier[mods.length];
            for (int i = 0; i < modifiers.length; i++)
                modifiers[i] = TypeModifier.getModifier(mods[i]);
            out = new VariableDeclaration(matcher.group(1), matcher.group(3), Type.getType(matcher.group(2)), modifiers);
        } else {
            matcher = Constants.DECLARATION_PATTERN_DEFAULT_VALUE.matcher(source);
            if (matcher.matches()) {
                String[] mods = matcher.group(3).split(",");
                if (mods.length == 1 && mods[0].hashCode() == "".hashCode()) mods = new String[0];
                TypeModifier[] modifiers = new TypeModifier[mods.length];
                for (int i = 0; i < modifiers.length; i++)
                    modifiers[i] = TypeModifier.getModifier(mods[i]);
                out = new VariableDeclaration(matcher.group(1), null, Type.getType(matcher.group(2)), modifiers);
            } else {
                matcher = Constants.DECLARATION_PATTERN_NO_MODS.matcher(source);
                if (matcher.matches()) {
                    out = new VariableDeclaration(matcher.group(1), matcher.group(3), Type.getType(matcher.group(2)), new TypeModifier[0]);
                } else {
                    matcher = Constants.DECLARATION_PATTERN_DEFAULT_VALUE_NO_MODS.matcher(source);
                    if (matcher.matches()) {
                        out = new VariableDeclaration(matcher.group(1), null, Type.getType(matcher.group(2)), new TypeModifier[0]);
                    } else {
                        matcher = Constants.DECLARATION_PATTERN_AUTO_TYPE.matcher(source);
                        if (matcher.matches()) {
                            String[] mods = matcher.group(3).split(",");
                            if (mods.length == 1 && mods[0].hashCode() == "".hashCode()) mods = new String[0];
                            TypeModifier[] modifiers = new TypeModifier[mods.length];
                            for (int i = 0; i < modifiers.length; i++)
                                modifiers[i] = TypeModifier.getModifier(mods[i]);
                            out = new VariableDeclaration(matcher.group(1), matcher.group(2), null, modifiers);
                        } else {
                            matcher = Constants.DECLARATION_PATTERN_AUTO_TYPE_NO_MODS.matcher(source);
                            if (matcher.matches()) {
                                out = new VariableDeclaration(matcher.group(1), matcher.group(2), null, new TypeModifier[0]);
                            } else {
                                matcher = Constants.FUNCTION_PATTERN.matcher(source);
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
                                    if (functionsLayer.size() == 0) exitedGlobal = true;
                                    functionsLayer.add(function);
                                    out = function;
                                } else {
                                    matcher = Constants.CLOSE_CURLY_BRACES_PATTERN.matcher(source);
                                    if (matcher.matches()) {
                                        functionsLayer.remove(functionsLayer.size() - 1);
                                        return null;
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

        if (functionsLayer.size() > 0 && !exitedGlobal) {
            if (out instanceof Function)
                functionsLayer.add((Function) out);
            else functionsLayer.get(functionsLayer.size() - 1).getInstructions().add(out);
            return null;
        }

        if (out != null) return out;
        throw new SyntaxException();
    }
}
