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

public enum Primitive {
    _func(Function.class),
    _int(int.class),
    _short(short.class),
    _byte(byte.class),
    _long(long.class),
    _float(float.class),
    _double(double.class),
    _obj(Object.class),
    _char(char.class),
    _string(String.class),
    _obj_array(Object[].class),
    _class(Class.class),
    _list(ObjectList.class);

    public static Class<?> getType(String name) {
        try {
            return Primitive.valueOf('_' + name).type;
        }catch (IllegalArgumentException e) {
            return null;
        }
    }

    private final Class<?> type;

    Primitive(Class<?> type) {
        this.type = type;
    }

    public Class<?> getType() {
        return type;
    }
}
