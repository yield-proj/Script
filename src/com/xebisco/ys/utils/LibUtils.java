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

package com.xebisco.ys.utils;

import com.xebisco.yieldutils.Pair;
import com.xebisco.ys.calls.*;
import com.xebisco.ys.memory.MemoryBank;
import com.xebisco.ys.program.Interpreter;
import com.xebisco.ys.program.Library;
import com.xebisco.ys.program.Program;
import com.xebisco.ys.types.Array;
import com.xebisco.ys.types.ArrayArgs;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class LibUtils {
    public static Library addLibs(LibVersion libVersion) {
        Library library = new Library("lib");
        switch (libVersion) {
            case _01:

                //Action functions

                //function: if(boolean execute)
                library.getFunctions().put(new Pair<>("if", List.of(new Class<?>[]{Boolean.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(ValueMod valueMod) {
                                setReturnExecution(true);
                                return valueMod.getValue("execute");
                            }
                        }
                }, new Argument[]{
                        new Argument("execute", Boolean.class, false)
                }));

                //String Conversion functions

                //array to string
                library.getFunctions().put(new Pair<>("string", List.of(new Class<?>[]{Array.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(ValueMod valueMod) {
                                setReturnExecution(true);
                                return valueMod.getValue("x").toString();
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Array.class, false)
                }));

                //arrayargs to string
                library.getFunctions().put(new Pair<>("string", List.of(new Class<?>[]{ArrayArgs.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(ValueMod valueMod) {
                                setReturnExecution(true);
                                return valueMod.getValue("x").toString();
                            }
                        }
                }, new Argument[]{
                        new Argument("x", ArrayArgs.class, false)
                }));

                //boolean to string
                library.getFunctions().put(new Pair<>("string", List.of(new Class<?>[]{Boolean.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(ValueMod valueMod) {
                                setReturnExecution(true);
                                return String.valueOf((boolean) valueMod.getValue("x"));
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Boolean.class, false)
                }));

                //long to string
                library.getFunctions().put(new Pair<>("string", List.of(new Class<?>[]{Long.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(ValueMod valueMod) {
                                setReturnExecution(true);
                                return String.valueOf((long) valueMod.getValue("x"));
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Long.class, false)
                }));

                //double to string
                library.getFunctions().put(new Pair<>("string", List.of(new Class<?>[]{Double.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(ValueMod valueMod) {
                                setReturnExecution(true);
                                return String.valueOf((double) valueMod.getValue("x"));
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Double.class, false)
                }));

                //float to string
                library.getFunctions().put(new Pair<>("string", List.of(new Class<?>[]{Float.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(ValueMod valueMod) {
                                setReturnExecution(true);
                                return String.valueOf((float) valueMod.getValue("x"));
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Float.class, false)
                }));

                //int to string
                library.getFunctions().put(new Pair<>("string", List.of(new Class<?>[]{Integer.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(ValueMod valueMod) {
                                setReturnExecution(true);
                                return String.valueOf((int) valueMod.getValue("x"));
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Integer.class, false)
                }));

                //short to string
                library.getFunctions().put(new Pair<>("string", List.of(new Class<?>[]{Short.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(ValueMod valueMod) {
                                setReturnExecution(true);
                                return String.valueOf((short) valueMod.getValue("x"));
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Short.class, false)
                }));

                //byte to string
                library.getFunctions().put(new Pair<>("string", List.of(new Class<?>[]{Byte.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(ValueMod valueMod) {
                                setReturnExecution(true);
                                return String.valueOf((byte) valueMod.getValue("x"));
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Byte.class, false)
                }));

                //Number Conversions functions

                //float to double
                library.getFunctions().put(new Pair<>("double", List.of(new Class<?>[]{Float.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(ValueMod valueMod) {
                                setReturnExecution(true);
                                return (double) (float) valueMod.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Float.class, false)
                }));
                //long to double
                library.getFunctions().put(new Pair<>("double", List.of(new Class<?>[]{Long.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(ValueMod valueMod) {
                                setReturnExecution(true);
                                return (double) (long) valueMod.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Long.class, false)
                }));
                //int to double
                library.getFunctions().put(new Pair<>("double", List.of(new Class<?>[]{Integer.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(ValueMod valueMod) {
                                setReturnExecution(true);
                                return (double) (int) valueMod.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Integer.class, false)
                }));

                //short to double
                library.getFunctions().put(new Pair<>("double", List.of(new Class<?>[]{Short.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(ValueMod valueMod) {
                                setReturnExecution(true);
                                return (double) (short) valueMod.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Short.class, false)
                }));

                //byte to double
                library.getFunctions().put(new Pair<>("double", List.of(new Class<?>[]{Byte.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(ValueMod valueMod) {
                                setReturnExecution(true);
                                return (double) (byte) valueMod.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Byte.class, false)
                }));

                //string to double
                library.getFunctions().put(new Pair<>("double", List.of(new Class<?>[]{String.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(ValueMod valueMod) {
                                setReturnExecution(true);
                                return Double.parseDouble((String) valueMod.getValue("x"));
                            }
                        }
                }, new Argument[]{
                        new Argument("x", String.class, false)
                }));


                //double to float
                library.getFunctions().put(new Pair<>("float", List.of(new Class<?>[]{Double.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(ValueMod valueMod) {
                                setReturnExecution(true);
                                return (float) (double) valueMod.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Double.class, false)
                }));
                //long to float
                library.getFunctions().put(new Pair<>("float", List.of(new Class<?>[]{Long.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(ValueMod valueMod) {
                                setReturnExecution(true);
                                return (float) (long) valueMod.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Long.class, false)
                }));
                //int to float
                library.getFunctions().put(new Pair<>("float", List.of(new Class<?>[]{Integer.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(ValueMod valueMod) {
                                setReturnExecution(true);
                                return (float) (int) valueMod.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Integer.class, false)
                }));

                //short to float
                library.getFunctions().put(new Pair<>("float", List.of(new Class<?>[]{Short.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(ValueMod valueMod) {
                                setReturnExecution(true);
                                return (float) (short) valueMod.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Short.class, false)
                }));

                //byte to float
                library.getFunctions().put(new Pair<>("float", List.of(new Class<?>[]{Byte.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(ValueMod valueMod) {
                                setReturnExecution(true);
                                return (float) (byte) valueMod.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Byte.class, false)
                }));

                //string to float
                library.getFunctions().put(new Pair<>("float", List.of(new Class<?>[]{String.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(ValueMod valueMod) {
                                setReturnExecution(true);
                                return Float.parseFloat((String) valueMod.getValue("x"));
                            }
                        }
                }, new Argument[]{
                        new Argument("x", String.class, false)
                }));


                //double to long
                library.getFunctions().put(new Pair<>("long", List.of(new Class<?>[]{Double.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(ValueMod valueMod) {
                                setReturnExecution(true);
                                return (long) (double) valueMod.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Double.class, false)
                }));
                //float to long
                library.getFunctions().put(new Pair<>("long", List.of(new Class<?>[]{Float.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(ValueMod valueMod) {
                                setReturnExecution(true);
                                return (long) (float) valueMod.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Float.class, false)
                }));
                //int to long
                library.getFunctions().put(new Pair<>("long", List.of(new Class<?>[]{Integer.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(ValueMod valueMod) {
                                setReturnExecution(true);
                                return (long) (int) valueMod.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Integer.class, false)
                }));

                //short to long
                library.getFunctions().put(new Pair<>("long", List.of(new Class<?>[]{Short.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(ValueMod valueMod) {
                                setReturnExecution(true);
                                return (long) (short) valueMod.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Short.class, false)
                }));

                //byte to long
                library.getFunctions().put(new Pair<>("long", List.of(new Class<?>[]{Byte.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(ValueMod valueMod) {
                                setReturnExecution(true);
                                return (long) (byte) valueMod.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Byte.class, false)
                }));

                //string to long
                library.getFunctions().put(new Pair<>("long", List.of(new Class<?>[]{String.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(ValueMod valueMod) {
                                setReturnExecution(true);
                                return Long.parseLong((String) valueMod.getValue("x"));
                            }
                        }
                }, new Argument[]{
                        new Argument("x", String.class, false)
                }));


                //double to int
                library.getFunctions().put(new Pair<>("int", List.of(new Class<?>[]{Double.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(ValueMod valueMod) {
                                setReturnExecution(true);
                                return (int) (double) valueMod.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Double.class, false)
                }));
                //float to int
                library.getFunctions().put(new Pair<>("int", List.of(new Class<?>[]{Float.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(ValueMod valueMod) {
                                setReturnExecution(true);
                                return (int) (float) valueMod.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Float.class, false)
                }));
                //long to int
                library.getFunctions().put(new Pair<>("int", List.of(new Class<?>[]{Long.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(ValueMod valueMod) {
                                setReturnExecution(true);
                                return (int) (long) valueMod.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Long.class, false)
                }));

                //short to int
                library.getFunctions().put(new Pair<>("int", List.of(new Class<?>[]{Short.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(ValueMod valueMod) {
                                setReturnExecution(true);
                                return (int) (short) valueMod.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Short.class, false)
                }));

                //byte to int
                library.getFunctions().put(new Pair<>("int", List.of(new Class<?>[]{Byte.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(ValueMod valueMod) {
                                setReturnExecution(true);
                                return (int) (byte) valueMod.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Byte.class, false)
                }));

                //string to int
                library.getFunctions().put(new Pair<>("int", List.of(new Class<?>[]{String.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(ValueMod valueMod) {
                                setReturnExecution(true);
                                return Integer.parseInt((String) valueMod.getValue("x"));
                            }
                        }
                }, new Argument[]{
                        new Argument("x", String.class, false)
                }));


                //double to short
                library.getFunctions().put(new Pair<>("short", List.of(new Class<?>[]{Double.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(ValueMod valueMod) {
                                setReturnExecution(true);
                                return (short) (double) valueMod.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Double.class, false)
                }));
                //float to short
                library.getFunctions().put(new Pair<>("short", List.of(new Class<?>[]{Float.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(ValueMod valueMod) {
                                setReturnExecution(true);
                                return (short) (float) valueMod.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Float.class, false)
                }));
                //long to short
                library.getFunctions().put(new Pair<>("short", List.of(new Class<?>[]{Long.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(ValueMod valueMod) {
                                setReturnExecution(true);
                                return (short) (long) valueMod.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Long.class, false)
                }));

                //int to short
                library.getFunctions().put(new Pair<>("short", List.of(new Class<?>[]{Short.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(ValueMod valueMod) {
                                setReturnExecution(true);
                                return (short) (int) valueMod.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Short.class, false)
                }));

                //byte to short
                library.getFunctions().put(new Pair<>("short", List.of(new Class<?>[]{Byte.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(ValueMod valueMod) {
                                setReturnExecution(true);
                                return (short) (byte) valueMod.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Byte.class, false)
                }));

                //string to short
                library.getFunctions().put(new Pair<>("short", List.of(new Class<?>[]{String.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(ValueMod valueMod) {
                                setReturnExecution(true);
                                return Short.parseShort((String) valueMod.getValue("x"));
                            }
                        }
                }, new Argument[]{
                        new Argument("x", String.class, false)
                }));


                //double to byte
                library.getFunctions().put(new Pair<>("byte", List.of(new Class<?>[]{Double.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(ValueMod valueMod) {
                                setReturnExecution(true);
                                return (byte) (double) valueMod.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Double.class, false)
                }));
                //float to byte
                library.getFunctions().put(new Pair<>("byte", List.of(new Class<?>[]{Float.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(ValueMod valueMod) {
                                setReturnExecution(true);
                                return (byte) (float) valueMod.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Float.class, false)
                }));
                //long to byte
                library.getFunctions().put(new Pair<>("byte", List.of(new Class<?>[]{Long.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(ValueMod valueMod) {
                                setReturnExecution(true);
                                return (byte) (long) valueMod.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Long.class, false)
                }));

                //int to byte
                library.getFunctions().put(new Pair<>("byte", List.of(new Class<?>[]{Short.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(ValueMod valueMod) {
                                setReturnExecution(true);
                                return (byte) (int) valueMod.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Short.class, false)
                }));

                //short to byte
                library.getFunctions().put(new Pair<>("byte", List.of(new Class<?>[]{Byte.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(ValueMod valueMod) {
                                setReturnExecution(true);
                                return (byte) (short) valueMod.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Byte.class, false)
                }));

                //string to byte
                library.getFunctions().put(new Pair<>("byte", List.of(new Class<?>[]{String.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(ValueMod valueMod) {
                                setReturnExecution(true);
                                return Byte.parseByte((String) valueMod.getValue("x"));
                            }
                        }
                }, new Argument[]{
                        new Argument("x", String.class, false)
                }));

                //function: class(String className)
                //Get class from string
                library.getFunctions().put(new Pair<>("class", List.of(new Class<?>[]{String.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(ValueMod valueMod) {
                                setReturnExecution(true);
                                return RunUtils.forName((String) valueMod.getValue("className"));
                            }
                        }
                }, new Argument[]{
                        new Argument("className", String.class, false)
                }));

                //function: array(ArrayArgs args)
                //Create array of objects
                library.getFunctions().put(new Pair<>("array", List.of(new Class<?>[]{ArrayArgs.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(ValueMod valueMod) {
                                setReturnExecution(true);
                                return new Array((ArrayArgs) valueMod.getValue("args"));
                            }
                        }
                }, new Argument[]{
                        new Argument("args", ArrayArgs.class, false)
                }));

                //function: new(Class class, ArrayArgs args)
                //New instance of a class
                library.getFunctions().put(new Pair<>("new", List.of(new Class<?>[]{Class.class, ArrayArgs.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(ValueMod valueMod) {
                                setReturnExecution(true);
                                ArrayArgs args = (ArrayArgs) valueMod.getValue("args");
                                try {
                                    return ((Class<?>) valueMod.getValue("class")).getConstructor(args.getTypesArray()).newInstance(args.getObjectArray());
                                } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                                         InvocationTargetException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                }, new Argument[]{
                        new Argument("class", Class.class, false),
                        new Argument("args", ArrayArgs.class, false)
                }));

                //function: setPointer(Long ptr, Object value)
                library.getFunctions().put(new Pair<>("setPointer", List.of(new Class<?>[]{Integer.class, Object.class})), new Function(new Instruction[]{
                        new LowSecurityInstruction() {
                            @Override
                            public Object call(MemoryBank memoryBank, ValueMod valueMod) {
                                memoryBank.getPointers().set((Integer) valueMod.getValue("ptr"), valueMod.getValue("value"));
                                return null;
                            }
                        }
                }, new Argument[]{
                        new Argument("ptr", Long.class, false),
                        new Argument("value", Object.class, false)
                }));

                //function: include(String file)
                //This function execute and attach other ys file in this program.
                library.getFunctions().put(new Pair<>("include", List.of(new Class<?>[]{String.class})), new Function(new Instruction[]{
                        new LowSecurityInstruction() {
                            @Override
                            public Object call(MemoryBank memoryBank, ValueMod valueMod) {
                                File file = new File(valueMod.getValue("file") + ".ys");
                                if (!file.exists())
                                    file = new File(library.getLibsFolder(), valueMod.getValue("file") + ".ys");
                                Program toInclude = new Program(SourceUtils.fromRaw(FileUtils.readFile(file)), memoryBank);
                                toInclude.interpret(new Interpreter());
                                toInclude.run();
                                return null;
                            }
                        }
                }, new Argument[]{
                        new Argument("file", String.class, false)
                }));

                //function: includels(String file)
                //This function execute and attach other ys file in this program with allow low security set to true.
                library.getFunctions().put(new Pair<>("includels", List.of(new Class<?>[]{String.class})), new Function(new Instruction[]{
                        new LowSecurityInstruction() {
                            @Override
                            public Object call(MemoryBank memoryBank, ValueMod valueMod) {
                                File file = new File(valueMod.getValue("file") + ".ys");
                                if (!file.exists())
                                    file = new File(library.getLibsFolder(), valueMod.getValue("file") + ".ys");
                                Program toInclude = new Program(SourceUtils.fromRaw(FileUtils.readFile(file)), memoryBank);
                                toInclude.setAllowLowSecurity(true);
                                toInclude.interpret(new Interpreter());
                                toInclude.run();
                                return null;
                            }
                        }
                }, new Argument[]{
                        new Argument("file", String.class, false)
                }));

                //function: puts(String x)
                //This function prints a String into de standard output stream
                library.getFunctions().put(new Pair<>("puts", List.of(new Class<?>[]{String.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(ValueMod valueMod) {
                                System.out.println(valueMod.getValue("x"));
                                return null;
                            }
                        }
                }, new Argument[]{
                        new Argument("x", String.class, false)
                }));

                //function: execMethod(Object& obj, String method, ArrayArgs args)
                library.getFunctions().put(new Pair<>("execMethod", List.of(new Class<?>[]{Integer.class, String.class, ArrayArgs.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(ValueMod valueMod) {
                                Object o = valueMod.getValue("obj");
                                ArrayArgs arrayArgs = (ArrayArgs) valueMod.getValue("args");
                                try {
                                    return o.getClass().getDeclaredMethod((String) valueMod.getValue("method"), arrayArgs.getTypesArray()).invoke(o, arrayArgs.getObjectArray());
                                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                }, new Argument[]{
                        new Argument("obj", Object.class, true),
                        new Argument("method", String.class, false),
                        new Argument("args", ArrayArgs.class, false)
                }));

                //function: execMethod(Class class, String method, Array casts, Array args)
                library.getFunctions().put(new Pair<>("execMethod", List.of(new Class<?>[]{Class.class, String.class, Array.class, Array.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(ValueMod valueMod) {
                                Array casts = (Array) valueMod.getValue("casts");
                                setReturnExecution(true);
                                Class<?>[] classes = new Class<?>[casts.getObjectArray().length];
                                for (int i = 0; i < casts.getObjectArray().length; i++) {
                                    classes[i] = (Class<?>) casts.getObjectArray()[i];
                                }
                                try {
                                    return ((Class<?>) valueMod.getValue("class")).getDeclaredMethod((String) valueMod.getValue("method"), classes).invoke(null, ((Array) valueMod.getValue("args")).getObjectArray());
                                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                }, new Argument[]{
                        new Argument("class", Class.class, false),
                        new Argument("method", String.class, false),
                        new Argument("casts", Array.class, false),
                        new Argument("args", Array.class, false)
                }));

                //function: execMethod(Class class, String method, ArrayArgs args)
                library.getFunctions().put(new Pair<>("execMethod", List.of(new Class<?>[]{Class.class, String.class, ArrayArgs.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(ValueMod valueMod) {
                                ArrayArgs args = (ArrayArgs) valueMod.getValue("args");
                                setReturnExecution(true);
                                try {
                                    return ((Class<?>) valueMod.getValue("class")).getDeclaredMethod((String) valueMod.getValue("method"), args.getTypesArray()).invoke(null, args.getObjectArray());
                                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                }, new Argument[]{
                        new Argument("class", Class.class, false),
                        new Argument("method", String.class, false),
                        new Argument("args", ArrayArgs.class, false)
                }));
                break;
            default:
                throw new IllegalArgumentException("'" + libVersion + "' is not compatible with this interpreter.");
        }
        return library;
    }
}
