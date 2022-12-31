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

import com.xebisco.ys.exceptions.NullPointerInsideOfEquationException;
import com.xebisco.ys.utils.MathUtils;

public class PossibleEquationFunctionCall extends FunctionCall {
    public PossibleEquationFunctionCall(String equation, Class<?> cast) {
        super(equation, null, cast);
    }

    private boolean ignoreEquation;

    @Override
    public Object call(ValueMod valueMod) {
        if (!ignoreEquation) {
            try {
                Number out = MathUtils.eval(valueMod, getFunctionName());
                if (out != null)
                    return out;
            } catch (NullPointerException e) {
                e.printStackTrace();
                throw new NullPointerInsideOfEquationException(getFunctionName());
            }
        }
        return valueMod.getValue(getFunctionName());
    }

    public void setIgnoreEquation(boolean ignoreEquation) {
        this.ignoreEquation = ignoreEquation;
    }

    public boolean isIgnoreEquation() {
        return ignoreEquation;
    }
}
