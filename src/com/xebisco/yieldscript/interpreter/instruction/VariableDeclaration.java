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

package com.xebisco.yieldscript.interpreter.instruction;

import com.xebisco.yieldscript.interpreter.memory.Bank;
import com.xebisco.yieldscript.interpreter.memory.Variable;
import com.xebisco.yieldscript.interpreter.type.Type;
import com.xebisco.yieldscript.interpreter.type.TypeModifier;

import java.util.Arrays;

public class VariableDeclaration implements Instruction {

    private final TypeModifier[] modifiers;
    private final String startName, name;
    private final Type type;

    public VariableDeclaration(String name, String startName, Type type, TypeModifier[] modifiers) {
        this.name = name;
        this.type = type;
        this.startName = startName;
        this.modifiers = modifiers;
    }

    @Override
    public void execute(Bank bank) {
        Type t = type;
        System.out.println(name + ", " + startName);
        if(type == null)
            t = bank.getVariable(startName).getType();
        final Variable variable = new Variable(name, t);
        if (startName != null)
            variable.setValue(bank.getObject(startName));
        else variable.setValue(t.getInitialValue());
        bank.getObjects().put(name, variable);
    }

    @Override
    public String toString() {
        return "VariableDeclaration{" +
                "modifiers=" + Arrays.toString(modifiers) +
                ", startName='" + startName + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                '}';
    }

    public TypeModifier[] getModifiers() {
        return modifiers;
    }

    public String getStartName() {
        return startName;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }
}
