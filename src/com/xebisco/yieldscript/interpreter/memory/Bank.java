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

import com.xebisco.yieldscript.interpreter.Constants;
import com.xebisco.yieldscript.interpreter.type.Type;
import com.xebisco.yieldscript.interpreter.utils.ObjectUtils;
import com.xebisco.yieldscript.interpreter.utils.Pair;

import java.util.HashMap;
import java.util.Map;

public class Bank {
    private final Map<Object, Variable> objects = new HashMap<>();

    public Object getObject(String name) {
        if(name.charAt(0) == Constants.STRING_LITERAL_ID_CHAR) return objects.get(new Pair<>((Object) Long.parseLong(name.substring(1)), Type._string));
        Object o = ObjectUtils.toObject(name);
        if(o == null) {
            o = objects.get(name);
            if(o.getClass().isPrimitive()) {
                return o;
            } else {
                //TODO
                return null;
            }
        }
        return o;
    }

    public Map<Object, Variable> getObjects() {
        return objects;
    }
}
