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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodCall implements Instruction {
    private final Method method;
    private final Object[] args;
    private final Object object;

    public MethodCall(Method method, Object object, Object... args) {
        this.method = method;
        this.args = args;
        this.object = object;
    }

    @Override
    public Object run(Script script) {
        try {
            Object[] a_args = new Object[args.length];
            for (int i = 0; i < a_args.length; i++)
                a_args[i] = script.getMemoryBank().getObject(args[i]);
            return method.invoke(object, a_args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public Method getMethod() {
        return method;
    }

    public Object[] getArgs() {
        return args;
    }

    public Object getObject() {
        return object;
    }
}
