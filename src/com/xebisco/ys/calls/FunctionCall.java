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

import com.xebisco.ys.utils.FunctionUtils;

public class FunctionCall extends Instruction {
    private final String functionName;
    private final FunctionCall[] args;
    private Class<?> cast;

    public FunctionCall(String functionName, FunctionCall[] args, Class<?> cast) {
        this.functionName = functionName;
        this.args = args;
        this.cast = cast;
    }

    @Override
    public Object call(ValueMod valueMod) {
        Object o = FunctionUtils.call(valueMod, valueMod.getMemoryBank(), functionName, args);
        if(cast == null && o != null) {
            cast = o.getClass();
        }
        return o;
    }

    public String getFunctionName() {
        return functionName;
    }

    public FunctionCall[] getArgs() {
        return args;
    }

    public Class<?> getCast() {
        return cast;
    }

    public void setCast(Class<?> cast) {
        this.cast = cast;
    }
}
