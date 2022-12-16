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
import com.xebisco.yieldscript.interpreter.exceptions.FunctionNotFoundException;
import com.xebisco.yieldscript.interpreter.utils.MathUtils;
import com.xebisco.yieldscript.interpreter.utils.ObjectUtils;
import com.xebisco.yieldscript.interpreter.utils.Pair;
import com.xebisco.yieldscript.interpreter.utils.ScriptUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bank {
    private final Map<Object, Variable> objects = new HashMap<>();
    private final Map<Pair<String, List<Class<?>>>, Function> functions = new HashMap<>();

    private String lastGetObjectName;

    public void clear() {
        objects.clear();
        functions.clear();
    }

    public Object getObject(String name) {
        lastGetObjectName = name;
        Variable string = getString(name);
        if (string != null) return string.getValue();
        Object o = ObjectUtils.toObject(name);
        if (o == null) {
            if (MathUtils.matchesForMath(name)) o = MathUtils.resolve(name);
            if (o == null) {
                o = MathUtils.booleanOut(name, this);
                if (o == null) {
                    o = objects.get(name);
                    if (o != null) return ((Variable) o).getValue();
                    try {
                        return ScriptUtils.methodCall(name, null).invoke(this);
                    } catch (StackOverflowError e) {
                        throw new FunctionNotFoundException(name);
                    }
                }
            }
        }
        return o;
    }

    public Field getField(String name, String fieldName) {
        try {
            return getObject(name).getClass().getField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public Method getMethod(String name, String methodName, Class<?>... parameterTypes) {
        try {
            return getObject(name).getClass().getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public Variable getString(String name) {
        try {
            if (name.charAt(0) == Constants.STRING_LITERAL_ID_CHAR) return objects.get(name.substring(1));
        } catch (StringIndexOutOfBoundsException ignore) {

        }
        return null;
    }

    public String getLastGetObjectName() {
        return lastGetObjectName;
    }

    public void setLastGetObjectName(String lastGetObjectName) {
        this.lastGetObjectName = lastGetObjectName;
    }

    public Map<Object, Variable> getObjects() {
        return objects;
    }

    public Map<Pair<String, List<Class<?>>>, Function> getFunctions() {
        return functions;
    }
}
