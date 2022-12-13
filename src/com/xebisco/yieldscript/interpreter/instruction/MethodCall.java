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

package com.xebisco.yieldscript.interpreter.instruction;

import com.xebisco.yieldscript.interpreter.Constants;
import com.xebisco.yieldscript.interpreter.memory.Bank;
import com.xebisco.yieldscript.interpreter.memory.Function;
import com.xebisco.yieldscript.interpreter.memory.Variable;
import com.xebisco.yieldscript.interpreter.type.Type;
import com.xebisco.yieldscript.interpreter.type.TypeModifier;
import com.xebisco.yieldscript.interpreter.utils.Pair;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class MethodCall implements Instruction {

    private final Class<?> methodClass;
    private final MethodCall[] arguments;
    private final MethodCall parent;
    private final String methodName, getter;
    private String returnVariableName;

    public MethodCall(Class<?> methodClass, MethodCall[] arguments, MethodCall parent, String methodName) {
        this.methodClass = methodClass;
        this.arguments = arguments;
        this.parent = parent;
        this.methodName = methodName;
        this.getter = null;
    }

    public MethodCall(Class<?> methodClass, String getter, MethodCall parent) {
        this.getter = getter;
        this.methodClass = methodClass;
        this.arguments = null;
        this.parent = parent;
        this.methodName = null;
        this.returnVariableName = null;
    }

    @Override
    public Object execute(Bank bank) {
        return invoke(bank);
    }

    public Object invoke(Bank bank) {
        if (getter == null) {
            assert arguments != null;
            Object[] args = new Object[arguments.length];
            Class<?>[] types = new Class<?>[args.length];
            for (int i = 0; i < args.length; i++) {
                args[i] = arguments[i].invoke(bank);
                types[i] = args[i].getClass();
            }
            Method method;
            Object obj = null;
            if (parent != null) {
                try {
                    assert methodName != null;
                    method = (obj = parent.invoke(bank)).getClass().getMethod(methodName, types);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            } else if(methodClass == null) {
                try {
                    assert methodName != null;
                    method = (obj = bank.getFunctions().get(new Pair<>(methodName, Arrays.asList(types)))).getClass().getMethod("execute", Bank.class);
                    Function f = (Function) obj;
                    for(int i = 0 ; i < f.getArgumentsNames().length; i++) {
                        Variable variable = new Variable(f.getArgumentsNames()[i], Type.getType(args[i].getClass()));
                        variable.setModifiers(TypeModifier._set);
                        variable.setValue(args[i]);
                        bank.getObjects().put(Constants.FUNCTION_ARGUMENT_ID_CHAR + f.getArgumentsNames()[i], variable);
                    }
                    args = new Object[] {bank};
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    assert methodName != null;
                    method = methodClass.getMethod(methodName, types);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                return method.invoke(obj, args);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        } else {
            if (parent != null) {
                Object o = parent.invoke(bank);
                try {
                    return o.getClass().getField(getter).get(o);
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    throw new RuntimeException(e);
                }
            } else if (methodClass != null) {
                try {
                    return methodClass.getField(getter).get(null);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            } else
                return bank.getObject(getter);
        }
    }

    public Class<?> getMethodClass() {
        return methodClass;
    }

    public MethodCall[] getArguments() {
        return arguments;
    }

    public MethodCall getParent() {
        return parent;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getGetter() {
        return getter;
    }

    public String getReturnVariableName() {
        return returnVariableName;
    }

    public void setReturnVariableName(String returnVariableName) {
        this.returnVariableName = returnVariableName;
    }
}
