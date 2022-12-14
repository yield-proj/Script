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

import com.xebisco.yieldscript.interpreter.exceptions.NonPrimitiveException;
import com.xebisco.yieldscript.interpreter.memory.DataDeclaration;

import java.lang.reflect.Constructor;

public enum Type {
    _string(""),
    _byte((byte) 0, false),
    _short((short) 0, true),
    _int(0, false),
    _long(0L, false),
    _float(0f, false),
    _double(0d, false),
    _boolean(false, false),
    _char((char) 0, Character.class),
    _primitive_byte((byte) 0, true),
    _primitive_short((short) 0, true),
    _primitive_int(0, true),
    _primitive_long(0L, true),
    _primitive_float(0f, true),
    _primitive_double(0d, true),
    _primitive_boolean(false, true),
    _primitive_char((char) 0, true),
    _array_list(null, ArrayList.class),
    _array(null, Array.class),
    _array_args(null, ArrayArgs.class),
    _class(null, Class.class),
    _data(null, Data.class),
    _def(null, Object.class);

    private final Object initialValue;
    private final Class<?> javaClass;
    private final boolean primitive;

    Type(Object initialValue) {
        this.initialValue = initialValue;
        javaClass = initialValue.getClass();
        this.primitive = false;
    }

    Type(Object initialValue, Class<?> javaClass) {
        this.initialValue = javaClass.cast(initialValue);
        this.javaClass = javaClass;
        this.primitive = false;
    }

    Type(Object initialValue, boolean primitive) {
        this.primitive = primitive;
        if (primitive) {
            switch (initialValue.getClass().getName()) {
                case "java.lang.Integer":
                    javaClass = int.class;
                    break;
                case "java.lang.Float":
                    javaClass = float.class;
                    break;
                case "java.lang.Double":
                    javaClass = double.class;
                    break;
                case "java.lang.Long":
                    javaClass = long.class;
                    break;
                case "java.lang.Byte":
                    javaClass = byte.class;
                    break;
                case "java.lang.Boolean":
                    javaClass = boolean.class;
                    break;
                case "java.lang.Character":
                    javaClass = char.class;
                    break;
                case "java.lang.Short":
                    javaClass = short.class;
                    break;
                default:
                    throw new NonPrimitiveException(initialValue.getClass().getName());
            }
        } else javaClass = initialValue.getClass();
        this.initialValue = initialValue;
    }

    public static Type getType(String s) {
        return Type.valueOf('_' + s.replace(' ', '_'));
    }

    public static Type getType(Class<?> c) {
        for (Type type : Type.values()) {
            if (type.javaClass == c) {
                return type;
            }
        }
        return _def;
    }

    public boolean isPrimitive() {
        return primitive;
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
