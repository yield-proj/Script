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
import com.xebisco.yieldscript.interpreter.utils.ObjectUtils;
import com.xebisco.yieldscript.interpreter.utils.Pair;
import com.xebisco.yieldscript.interpreter.utils.ScriptUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Bank {
    private final Map<Object, Variable> objects = new HashMap<>();
    private final Map<Pair<String, List<Class<?>>>, Function> functions = new HashMap<>();

    private String lastGetObjectName;

    public Object getObject(String name) {
        lastGetObjectName = name;
        Variable string = getString(name);
        if (string != null) return string.getValue();
        Object o = ObjectUtils.toObject(name);
        if (o == null) {
            o = objects.get(name);
            if (o != null) return ((Variable) o).getValue();
            if (lastGetObjectName.hashCode() != name.hashCode())
                return ScriptUtils.methodCall(name, null).invoke(this);
            else throw new FunctionNotFoundException(name);
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
        if (name.charAt(0) == Constants.STRING_LITERAL_ID_CHAR) return objects.get(name.substring(1));
        return null;
    }

    public Map<Object, Variable> getObjects() {
        return objects;
    }

    public Map<Pair<String, List<Class<?>>>, Function> getFunctions() {
        return functions;
    }
}
