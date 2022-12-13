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

import java.lang.reflect.Constructor;

public enum Type {
    _string(""),
    _byte((byte) 0),
    _short((short) 0),
    _int(0),
    _long(0L),
    _float(0f),
    _double(0d),
    _boolean(false),
    _char((char) 0),
    _arraylist(null, ArrayList.class),
    _array(null, Array.class),
    _class(null, Class.class),
    _def(null, Object.class);

    private final Object initialValue;
    private final Class<?> javaClass;

    Type(Object initialValue) {
        this.initialValue = initialValue;
        javaClass = initialValue.getClass();
    }

    Type(Object initialValue, Class<?> javaClass) {
        this.initialValue = javaClass.cast(initialValue);
        this.javaClass = javaClass;
    }

    public static Type getType(String s) {
        return Type.valueOf('_' + s);
    }

    public static Type getType(Class<?> c) {
        for(Type type : Type.values()) {
            if(type.javaClass == c) {
                return type;
            }
        }
        return _def;
    }

    public Constructor<?> getConstructor(Class<?> argumentTypes) {
        try {
            return javaClass.getConstructor(argumentTypes);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public Object getInitialValue() {
        return initialValue;
    }

    public Class<?> getJavaClass() {
        return javaClass;
    }
}
