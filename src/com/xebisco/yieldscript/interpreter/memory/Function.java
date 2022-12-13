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

package com.xebisco.yieldscript.interpreter.memory;

import com.xebisco.yieldscript.interpreter.Constants;
import com.xebisco.yieldscript.interpreter.instruction.Executable;
import com.xebisco.yieldscript.interpreter.instruction.Instruction;
import com.xebisco.yieldscript.interpreter.type.TypeModifier;
import com.xebisco.yieldscript.interpreter.utils.Pair;
import com.xebisco.yieldscript.interpreter.utils.ScriptUtils;

import java.util.ArrayList;
import java.util.List;

public class Function implements Executable {
    private final String name;
    private final Class<?>[] argumentsTypes;
    private final String[] argumentsNames;
    private final Class<?> returnType;
    private final List<String> cachedVariableNames = new ArrayList<>();
    private final List<Pair<Instruction, String[]>> instructions = new ArrayList<>();
    private List<TypeModifier> modifiers = new ArrayList<>();

    private Object returnObject;

    public Function(String name, Class<?>[] argumentsTypes, String[] argumentsNames, Class<?> returnType) {
        this.name = name;
        this.argumentsTypes = argumentsTypes;
        this.argumentsNames = argumentsNames;
        this.returnType = returnType;
    }

    public List<Pair<Instruction, String[]>> getInstructions() {
        return instructions;
    }

    public String getName() {
        return name;
    }

    public Class<?>[] getArgumentsTypes() {
        return argumentsTypes;
    }

    public String[] getArgumentsNames() {
        return argumentsNames;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    @Override
    public Object execute(Bank bank) {
        setReturnObject(null);
        Bank functionBank = new Bank();
        for (String arg : argumentsNames) {
            if (bank.getObjects().containsKey(arg)) {
                cachedVariableNames.add(arg);
                bank.getObjects().put("$" + arg, bank.getObjects().get(arg));
                bank.getObjects().remove(arg);
            }
            functionBank.getObjects().put(arg, bank.getObjects().get(Constants.FUNCTION_ARGUMENT_ID_CHAR + arg));
            bank.getObjects().remove(Constants.FUNCTION_ARGUMENT_ID_CHAR + arg);
        }
        ScriptUtils.attachBank(functionBank, bank);
        ScriptUtils.executeInstructions(instructions, functionBank);
        for (String cachedVariable : cachedVariableNames) {
            bank.getObjects().put(cachedVariable, bank.getObjects().get("$" + cachedVariable));
            bank.getObjects().remove("$" + cachedVariable);
        }
        cachedVariableNames.clear();
        return null;
    }

    public Object getReturnObject() {
        return returnObject;
    }

    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }
    public void setModifiers(TypeModifier... modifiers) {
        if(modifiers.length == 0) this.modifiers = List.of(TypeModifier._get);
        else this.modifiers = List.of(modifiers);
    }

    public List<TypeModifier> getModifiers() {
        return modifiers;
    }
}
