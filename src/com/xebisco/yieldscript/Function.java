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

package com.xebisco.yieldscript;

import java.util.List;

public class Function {
    private final List<Pair<String, Instruction>> instructions;
    private final Class<?> returnType;

    public Function(Class<?> returnType, List<Pair<String, Instruction>> instructions) {
        this.returnType = returnType;
        this.instructions = instructions;
    }

    public void run(Script script) {
        for (Pair<String, Instruction> instruction : instructions) {
            if (instruction.getFirst() == null)
                instruction.getSecond().run(script);
            else
                script.getMemoryBank().getObjects().put(instruction.getFirst(), instruction.getSecond().run(script));
        }
    }

    public List<Pair<String, Instruction>> getInstructions() {
        return instructions;
    }

    public Class<?> getReturnType() {
        return returnType;
    }
}
