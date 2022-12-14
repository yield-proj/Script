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
import com.xebisco.yieldscript.interpreter.exceptions.FunctionNotFoundException;
import com.xebisco.yieldscript.interpreter.exceptions.NonPrimitiveException;
import com.xebisco.yieldscript.interpreter.memory.Bank;
import com.xebisco.yieldscript.interpreter.memory.Function;
import com.xebisco.yieldscript.interpreter.memory.Variable;
import com.xebisco.yieldscript.interpreter.type.Array;
import com.xebisco.yieldscript.interpreter.type.Type;
import com.xebisco.yieldscript.interpreter.type.TypeModifier;
import com.xebisco.yieldscript.interpreter.utils.Pair;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MethodCall implements Instruction {

    private final Class<?> methodClass;
    private final MethodCall[] arguments;
    private final MethodCall parent;
    private final String methodName, getter;
    private String returnVariableName;
    private String[] toSetVars = {};
    private final Type returnCast;

    public MethodCall(Class<?> methodClass, MethodCall[] arguments, MethodCall parent, String methodName, String[] toSetVars, Type returnCast) {
        this.methodClass = methodClass;
        this.arguments = arguments;
        this.parent = parent;
        this.methodName = methodName;
        this.returnCast = returnCast;
        this.getter = null;
        this.toSetVars = toSetVars;
    }

    public MethodCall(Class<?> methodClass, String getter, MethodCall parent, String[] toSetVars, Type returnCast) {
        this.getter = getter;
        this.methodClass = methodClass;
        this.returnCast = returnCast;
        this.arguments = null;
        this.parent = parent;
        this.methodName = null;
        this.returnVariableName = null;
        this.toSetVars = toSetVars;
    }

    @Override
    public Object execute(Bank bank) {
        Object o = invoke(bank);
        for (String s : toSetVars) {
            bank.getObjects().get(s).setValue(o);
        }
        return o;
    }

    public Object invoke(Bank bank) {
        if (getter == null) {
            assert arguments != null;
            Object[] args = new Object[arguments.length];
            Class<?>[] types = new Class<?>[args.length];
            for (int i = 0; i < args.length; i++) {
                args[i] = arguments[i].invoke(bank);
                if (arguments[i].getReturnCast() == null)
                    types[i] = args[i].getClass();
                else types[i] = arguments[i].getReturnCast().getJavaClass();
            }
            Method method = null;
            Object obj = null;
            if (parent != null) {
                try {
                    assert methodName != null;
                    method = (obj = parent.invoke(bank)).getClass().getMethod(methodName, types);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }

            } else if (methodClass == null) {
                try {
                    assert methodName != null;
                    try {
                        method = (obj = bank.getFunctions().get(new Pair<>(methodName, Arrays.asList(types)))).getClass().getMethod("execute", Bank.class);
                        Function f = (Function) obj;
                        for (int i = 0; i < f.getArgumentsNames().length; i++) {
                            Variable variable = new Variable(f.getArgumentsNames()[i], Type.getType(args[i].getClass()));
                            variable.setModifiers(TypeModifier._set);
                            variable.setValue(args[i]);
                            bank.getObjects().put(Constants.FUNCTION_ARGUMENT_ID_CHAR + f.getArgumentsNames()[i], variable);
                        }
                    } catch (NullPointerException e) {
                        List<Class<?>> cTypes = new ArrayList<>();
                        boolean found = false;
                        try {
                            cTypes.add(Array.class);
                            method = (obj = bank.getFunctions().get(new Pair<>(methodName, cTypes))).getClass().getMethod("execute", Bank.class);
                        } catch (NullPointerException ignore) {
                        }
                        if (obj != null)
                            found = true;
                        if (!found)
                            for (Class<?> type : types) {
                                try {
                                    cTypes.remove(cTypes.size() - 1);
                                    cTypes.add(type);
                                    cTypes.add(Array.class);
                                    method = (obj = bank.getFunctions().get(new Pair<>(methodName, cTypes))).getClass().getMethod("execute", Bank.class);
                                } catch (NullPointerException ignore) {
                                }
                                if (obj != null) {
                                    found = true;
                                    break;
                                }
                            }
                        if (!found)
                            throw new FunctionNotFoundException(methodName + Arrays.toString(types));
                        Function f = (Function) obj;
                        for (int i = 0; i < cTypes.size(); i++) {
                            Variable variable = new Variable(f.getArgumentsNames()[i], Type.getType(cTypes.get(i)));
                            variable.setModifiers(TypeModifier._set);
                            if (i == cTypes.size() - 1) {
                                Array<Object> array = new Array<>(Object.class, types.length - cTypes.size() + 1);
                                for(int i1 = cTypes.size() - 1; i1 < types.length; i1++) {
                                    array.set(args[i1], i1 - cTypes.size() + 1);
                                }
                                variable.setValue(array);
                            } else
                                variable.setValue(args[i]);
                            bank.getObjects().put(Constants.FUNCTION_ARGUMENT_ID_CHAR + f.getArgumentsNames()[i], variable);
                        }
                    }
                    args = new Object[]{bank};
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    assert methodName != null;
                    method = methodClass.getMethod(methodName, types);
                } catch (NoSuchMethodException e) {
                    try {
                        for (int i = 0; i < types.length; i++) if (types[i] == Character.class) types[i] = char.class;
                        method = methodClass.getMethod(methodName, types);
                    } catch (NoSuchMethodException ex) {
                        throw new RuntimeException(e);
                    }
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
            } else {
                return bank.getObject(getter);
            }
        }
    }

    public Type getReturnCast() {
        return returnCast;
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

    public String[] getToSetVars() {
        return toSetVars;
    }

    public void setToSetVars(String[] toSetVars) {
        this.toSetVars = toSetVars;
    }
}
