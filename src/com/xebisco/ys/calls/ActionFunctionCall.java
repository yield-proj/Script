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

import com.xebisco.ys.exceptions.ReturnException;
import com.xebisco.ys.utils.RunUtils;

import java.util.ArrayList;
import java.util.List;

public class ActionFunctionCall extends FunctionCall {

    private final List<Instruction> instructions = new ArrayList<>();

    public ActionFunctionCall(String functionName, FunctionCall[] args, Class<?> cast) {
        super(functionName, args, cast);
    }

    @Override
    public Object call(ValueMod valueMod) {
        Object o = super.call(valueMod);
        if (o instanceof Boolean) {
            if ((Boolean) o)
                RunUtils.run(valueMod.getMemoryBank(), instructions);
            return o;
        }
        throw new ReturnException("A action function needs to return a boolean, not '" + o.getClass().getName() + "' (" + getFunctionName() + ")");
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }
}
