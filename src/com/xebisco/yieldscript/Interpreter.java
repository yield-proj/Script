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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class Interpreter implements IInterpreter {
    @Override
    public Map<String, Function> getFunctions(String source) {
        Map<String, Function> functions = new HashMap<>();
        String[] lines = source.split("\n");
        int closeLevel = 0;
        List<Pair<String, Instruction>> instructions = null;
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            String[] pcs = line.split(" ");
            if (closeLevel == 0) {
                instructions = new ArrayList<>();
                functions.put(pcs[1], new Function(Primitive.getType(pcs[0]), instructions));
                closeLevel++;
            } else {
                if (line.hashCode() == "end".hashCode() && line.equals("end")) {
                    closeLevel--;
                }
                if (closeLevel > 0) {
                    String varName = null;
                    if(pcs[1].hashCode() == "=".hashCode()) {
                        varName = pcs[0];
                        String[] pcsTemp = new String[pcs.length - 2];
                        System.arraycopy(pcs, 2, pcsTemp, 0, pcsTemp.length);
                        pcs = pcsTemp;
                    }
                    Class<?> clazz;
                    int sub = 0;
                    try {
                        clazz = Class.forName(pcs[0]);
                    } catch (ClassNotFoundException ignore) {
                        /*String[] pcsTemp = new String[pcs.length - 1];
                        System.arraycopy(pcs, 1, pcsTemp, 0, pcsTemp.length);
                        pcs = pcsTemp;*/
                        sub = 1;
                        clazz = Lang.class;
                    }
                    String[] fieldsMethod = pcs[1 - sub].split("!");
                    if (fieldsMethod.length == 1) fieldsMethod = new String[]{null, fieldsMethod[0]};
                    if (pcs[2 - sub].hashCode() != "(".hashCode()) throw new SyntaxException("'(' expected at line " + (i + 1));
                    final List<Class<?>> argsTypes = new ArrayList<>();
                    int argsTypeEnd = 0;
                    for (int x = 3 - sub; x < pcs.length; x++) {
                        argsTypeEnd = x + 1;
                        if (pcs[x].hashCode() == ")".hashCode()) break;
                        Class<?> type = Primitive.getType(pcs[x]);
                        if(type == null) {
                            try {
                                type = Class.forName(pcs[x]);
                            } catch (ClassNotFoundException e) {
                                throw new RuntimeException(e.getMessage() + " at line " + (i + 1));
                            }
                        }
                        argsTypes.add(type);
                    }
                    Object[] args = new Object[argsTypes.size()];
                    //noinspection ManualArrayCopy
                    for (int x = argsTypeEnd; x < pcs.length; x++) {
                        args[x - argsTypeEnd] = pcs[x];
                    }
                    try {
                        Field field = null, lField = null;
                        Object obj = null;
                        Method method;
                        Class<?>[] argsTypesArray = argsTypes.toArray(new Class<?>[0]);
                        if (fieldsMethod[0] != null) {
                            String[] fields = fieldsMethod[0].split("\\.");
                            for (String f : fields) {
                                if (lField == null) field = clazz.getField(f);
                                else field = lField.getType().getField(f);
                                lField = field;
                            }
                            assert field != null;
                            obj = field.get(null);
                            method = field.getType().getMethod(fieldsMethod[1], argsTypesArray);
                        } else {
                            method = clazz.getMethod(fieldsMethod[1], argsTypesArray);
                        }
                        instructions.add(new Pair<>(varName, new MethodCall(method, obj, args)));
                    } catch (NoSuchMethodException | NoSuchFieldException | IllegalAccessException e) {
                        e.printStackTrace();
                        System.exit(1);
                    }
                }
            }
        }
        return functions;
    }
}
