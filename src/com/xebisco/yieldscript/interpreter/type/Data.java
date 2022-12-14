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

package com.xebisco.yieldscript.interpreter.type;

import com.xebisco.yieldscript.interpreter.memory.Variable;
import com.xebisco.yieldscript.interpreter.utils.Pair;

import java.util.List;

public class Data {

    private final Variable[] values;

    public Data(Variable[] values) {
        this.values = values;
    }

    public void set(String name, Object value) {
        for (Variable variable : values)
            if (variable.getName().hashCode() == name.hashCode()) {
                variable.setValue(value);
                return;
            }
        throw new IllegalArgumentException("Could not find member: '" + name + "'.");
    }

    public Object get(String name) {
        for (Variable variable : values)
            if (variable.getName().hashCode() == name.hashCode()) return variable.getValue();
        return null;
    }
}
