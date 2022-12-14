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

import com.xebisco.yieldscript.interpreter.exceptions.ImmutableException;
import com.xebisco.yieldscript.interpreter.type.Type;
import com.xebisco.yieldscript.interpreter.type.TypeModifier;

import java.util.ArrayList;
import java.util.List;

public class Variable extends UntypedVariable {
    private final Type type;
    private List<TypeModifier> modifiers = new ArrayList<>();

    public Variable(String name, Type type) {
        super(name);
        this.type = type;
        super.setValue(type.getInitialValue());
    }

    @Override
    public String toString() {
        return "Variable{" +
                "value=" + getValue() +
                ", type=" + type +
                ", name='" + getName() + '\'' +
                '}';
    }

    @Override
    public void setValue(Object value) {
        if(!modifiers.contains(TypeModifier._set) && !modifiers.contains(TypeModifier._arg))
            throw new ImmutableException(getName());
        super.setValue(type.getJavaClass().cast(value));
    }

    public Type getType() {
        return type;
    }
    public void setModifiers(TypeModifier... modifiers) {
        if(modifiers.length == 0) this.modifiers = List.of(TypeModifier._get, TypeModifier._set);
        else this.modifiers = List.of(modifiers);
    }

    public List<TypeModifier> getModifiers() {
        return modifiers;
    }
}
