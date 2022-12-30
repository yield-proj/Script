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

package com.xebisco.ys.calls;

import com.xebisco.ys.exceptions.FunctionNotFoundException;
import com.xebisco.ys.exceptions.ValueNotFoundException;
import com.xebisco.ys.memory.MemoryBank;
import com.xebisco.ys.utils.MathUtils;

import java.util.Objects;

public class VariableDeclaration extends Instruction {

    private final String name;
    private final FunctionCall value;

    public VariableDeclaration(String name, FunctionCall value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public Object call(MemoryBank memoryBank) {
        Object v = null;
        Exception originalException = null;
        try {
            v = value.call(memoryBank);
        } catch (ValueNotFoundException | FunctionNotFoundException e) {
            originalException = e;
        }
        if (v == null) {
            try {
                v = MathUtils.eval(memoryBank, value.getFunctionName());
            } catch (ValueNotFoundException e) {
                Objects.requireNonNullElse(originalException, e).printStackTrace();
                System.exit(1);
            }
        }

        return memoryBank.put(name, v);
    }
}
