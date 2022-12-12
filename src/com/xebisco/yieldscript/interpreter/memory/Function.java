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

import com.xebisco.yieldscript.interpreter.instruction.Instruction;

import java.util.ArrayList;
import java.util.List;

public class Function implements Instruction {
    private final String name;
    private final Class<?>[] argumentsTypes;
    private final String[] argumentsNames;
    private final Class<?> returnType;
    private final List<Instruction> instructions = new ArrayList<>();

    private Object returnObject;

    public Function(String name, Class<?>[] argumentsTypes, String[] argumentsNames, Class<?> returnType) {
        this.name = name;
        this.argumentsTypes = argumentsTypes;
        this.argumentsNames = argumentsNames;
        this.returnType = returnType;
    }

    public List<Instruction> getInstructions() {
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
    public void execute(Bank bank) {
        setReturnObject(null);
        Bank functionBank = new Bank();
        functionBank.getObjects().putAll(bank.getObjects());
        functionBank.getFunctions().putAll(bank.getFunctions());
        for (Instruction instruction : instructions) {
            instruction.execute(bank);
            if (returnObject != null)
                break;
        }
    }

    public Object getReturnObject() {
        return returnObject;
    }

    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }
}
