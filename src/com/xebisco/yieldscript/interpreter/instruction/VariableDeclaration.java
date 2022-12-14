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
import com.xebisco.yieldscript.interpreter.memory.UntypedVariable;
import com.xebisco.yieldscript.interpreter.memory.Variable;
import com.xebisco.yieldscript.interpreter.type.Type;
import com.xebisco.yieldscript.interpreter.type.TypeModifier;

import java.util.Arrays;
import java.util.List;

public class VariableDeclaration implements Instruction {

    private final TypeModifier[] modifiers;
    private final String startName, name;
    private final Type type;
    private final UntypedVariable toSet;
    private Variable varObject;

    public VariableDeclaration(String name, String startName, Type type, TypeModifier[] modifiers) {
        this.name = name;
        this.type = type;
        this.startName = startName;
        this.modifiers = modifiers;
        toSet = null;
    }

    public VariableDeclaration(String name, UntypedVariable toSet) {
        this.name = name;
        this.type = null;
        this.startName = null;
        this.modifiers = null;
        this.toSet = toSet;
    }

    @Override
    public Object execute(Bank bank) {
        Variable variable;
        if (toSet == null) {
            Type t = type;
            if (type == null) {
                t = Type.getType(bank.getObject(startName).getClass());
            }
            variable = new Variable(name, t);
            variable.setModifiers(TypeModifier._get, TypeModifier._set);
            if (startName != null)
                variable.setValue(bank.getObject(startName));
            else variable.setValue(t.getInitialValue());
            assert modifiers != null;
            variable.setModifiers(modifiers);
        } else {
            Type t = Type.getType(toSet.getValue().getClass());
            assert t != null;
            variable = new Variable(name, t);
            variable.setValue(toSet.getValue());
        }
        bank.getObjects().put(name, variable);
        setVarObject(variable);
        return variable.getValue();
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

    public UntypedVariable getToSet() {
        return toSet;
    }

    public Variable getVarObject() {
        return varObject;
    }

    public void setVarObject(Variable varObject) {
        this.varObject = varObject;
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
