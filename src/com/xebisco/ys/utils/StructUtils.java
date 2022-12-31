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

package com.xebisco.ys.utils;

import com.xebisco.yieldutils.Pair;
import com.xebisco.ys.LastScopeInstruction;
import com.xebisco.ys.calls.*;
import com.xebisco.ys.types.Struct;

import java.util.ArrayList;
import java.util.List;

public class StructUtils {
    public static Function createStruct(String name, Argument[] fields) {
        List<Argument> functionArgs = new ArrayList<>();
        for (Argument field : fields) {
            if (field.isIn()) functionArgs.add(field);
        }
        Function function = new LastScopeFunction(functionArgs.toArray(new Argument[0]));
        function.setReturnCast(Struct.class);
        function.setTName(name);
        function.getInstructions().add(new LastScopeInstruction() {
            @Override
            public Object call(ValueMod valueMod, ValueMod lastScopeValueMod) {
                setReturnExecution(true);
                Struct struct = new Struct();
                for (Argument field : fields) {
                    if (field instanceof SetArgument)
                        struct.getFields().put(field.getName(), ((SetArgument) field).getSet().call(valueMod));
                    else if (field.isIn())
                        struct.getFields().put(field.getName(), valueMod.getValue(field.getName()));
                    else
                        struct.getFields().put(field.getName(), field.getType().cast(null));
                }
                return struct;
            }
        });
        return function;
    }
}
