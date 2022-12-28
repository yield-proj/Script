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

import com.xebisco.yieldutils.Pair;
import com.xebisco.ys.memory.MemoryBank;
import com.xebisco.ys.utils.FunctionUtils;

public class FunctionCall extends Instruction {
    private final String functionName;
    private final FunctionCall[] args;

    public FunctionCall(String functionName, FunctionCall[] args) {
        this.functionName = functionName;
        this.args = args;
    }

    @Override
    public Object call(MemoryBank memoryBank) {
        return FunctionUtils.call(memoryBank, functionName, args);
    }

    public String getFunctionName() {
        return functionName;
    }

    public FunctionCall[] getArgs() {
        return args;
    }
}
