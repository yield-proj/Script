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
import com.xebisco.ys.calls.Argument;
import com.xebisco.ys.calls.Function;
import com.xebisco.ys.calls.Instruction;
import com.xebisco.ys.memory.MemoryBank;
import com.xebisco.ys.program.Interpreter;
import com.xebisco.ys.program.Program;
import com.xebisco.ys.types.Array;
import com.xebisco.ys.types.ArrayArgs;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class LibUtils {
    public static void addLibs(Program program, LibVersion libVersion) {
        switch (libVersion) {
            case _01:
                //String Conversion functions

                //array to string
                program.getBank().getFunctions().put(new Pair<>("string", List.of(new Class<?>[]{Array.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(MemoryBank memoryBank) {
                                setReturnExecution(true);
                                return memoryBank.getValue("x").toString();
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Array.class, false)
                }));

                //arrayargs to string
                program.getBank().getFunctions().put(new Pair<>("string", List.of(new Class<?>[]{ArrayArgs.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(MemoryBank memoryBank) {
                                setReturnExecution(true);
                                return memoryBank.getValue("x").toString();
                            }
                        }
                }, new Argument[]{
                        new Argument("x", ArrayArgs.class, false)
                }));

                //long to string
                program.getBank().getFunctions().put(new Pair<>("string", List.of(new Class<?>[]{Long.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(MemoryBank memoryBank) {
                                setReturnExecution(true);
                                return String.valueOf((long) memoryBank.getValue("x"));
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Long.class, false)
                }));

                //double to string
                program.getBank().getFunctions().put(new Pair<>("string", List.of(new Class<?>[]{Double.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(MemoryBank memoryBank) {
                                setReturnExecution(true);
                                return String.valueOf((double) memoryBank.getValue("x"));
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Double.class, false)
                }));

                //int to string
                program.getBank().getFunctions().put(new Pair<>("string", List.of(new Class<?>[]{Integer.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(MemoryBank memoryBank) {
                                setReturnExecution(true);
                                return String.valueOf((int) memoryBank.getValue("x"));
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Integer.class, false)
                }));

                //short to string
                program.getBank().getFunctions().put(new Pair<>("string", List.of(new Class<?>[]{Short.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(MemoryBank memoryBank) {
                                setReturnExecution(true);
                                return String.valueOf((short) memoryBank.getValue("x"));
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Short.class, false)
                }));

                //byte to string
                program.getBank().getFunctions().put(new Pair<>("string", List.of(new Class<?>[]{Byte.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(MemoryBank memoryBank) {
                                setReturnExecution(true);
                                return String.valueOf((byte) memoryBank.getValue("x"));
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Byte.class, false)
                }));

                //Number Conversions functions

                //float to double
                program.getBank().getFunctions().put(new Pair<>("double", List.of(new Class<?>[]{Float.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(MemoryBank memoryBank) {
                                setReturnExecution(true);
                                return (double) (float) memoryBank.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Float.class, false)
                }));
                //long to double
                program.getBank().getFunctions().put(new Pair<>("double", List.of(new Class<?>[]{Long.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(MemoryBank memoryBank) {
                                setReturnExecution(true);
                                return (double) (long) memoryBank.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Long.class, false)
                }));
                //int to double
                program.getBank().getFunctions().put(new Pair<>("double", List.of(new Class<?>[]{Integer.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(MemoryBank memoryBank) {
                                setReturnExecution(true);
                                return (double) (int) memoryBank.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Integer.class, false)
                }));

                //short to double
                program.getBank().getFunctions().put(new Pair<>("double", List.of(new Class<?>[]{Short.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(MemoryBank memoryBank) {
                                setReturnExecution(true);
                                return (double) (short) memoryBank.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Short.class, false)
                }));

                //byte to double
                program.getBank().getFunctions().put(new Pair<>("double", List.of(new Class<?>[]{Byte.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(MemoryBank memoryBank) {
                                setReturnExecution(true);
                                return (double) (byte) memoryBank.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Byte.class, false)
                }));

                //string to double
                program.getBank().getFunctions().put(new Pair<>("double", List.of(new Class<?>[]{String.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(MemoryBank memoryBank) {
                                setReturnExecution(true);
                                return Double.parseDouble((String) memoryBank.getValue("x"));
                            }
                        }
                }, new Argument[]{
                        new Argument("x", String.class, false)
                }));



                //double to float
                program.getBank().getFunctions().put(new Pair<>("float", List.of(new Class<?>[]{Double.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(MemoryBank memoryBank) {
                                setReturnExecution(true);
                                return (float) (double) memoryBank.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Double.class, false)
                }));
                //long to float
                program.getBank().getFunctions().put(new Pair<>("float", List.of(new Class<?>[]{Long.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(MemoryBank memoryBank) {
                                setReturnExecution(true);
                                return (float) (long) memoryBank.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Long.class, false)
                }));
                //int to float
                program.getBank().getFunctions().put(new Pair<>("float", List.of(new Class<?>[]{Integer.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(MemoryBank memoryBank) {
                                setReturnExecution(true);
                                return (float) (int) memoryBank.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Integer.class, false)
                }));

                //short to float
                program.getBank().getFunctions().put(new Pair<>("float", List.of(new Class<?>[]{Short.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(MemoryBank memoryBank) {
                                setReturnExecution(true);
                                return (float) (short) memoryBank.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Short.class, false)
                }));

                //byte to float
                program.getBank().getFunctions().put(new Pair<>("float", List.of(new Class<?>[]{Byte.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(MemoryBank memoryBank) {
                                setReturnExecution(true);
                                return (float) (byte) memoryBank.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Byte.class, false)
                }));

                //string to float
                program.getBank().getFunctions().put(new Pair<>("float", List.of(new Class<?>[]{String.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(MemoryBank memoryBank) {
                                setReturnExecution(true);
                                return Float.parseFloat((String) memoryBank.getValue("x"));
                            }
                        }
                }, new Argument[]{
                        new Argument("x", String.class, false)
                }));



                //double to long
                program.getBank().getFunctions().put(new Pair<>("long", List.of(new Class<?>[]{Double.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(MemoryBank memoryBank) {
                                setReturnExecution(true);
                                return (long) (double) memoryBank.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Double.class, false)
                }));
                //float to long
                program.getBank().getFunctions().put(new Pair<>("long", List.of(new Class<?>[]{Float.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(MemoryBank memoryBank) {
                                setReturnExecution(true);
                                return (long) (float) memoryBank.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Float.class, false)
                }));
                //int to long
                program.getBank().getFunctions().put(new Pair<>("long", List.of(new Class<?>[]{Integer.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(MemoryBank memoryBank) {
                                setReturnExecution(true);
                                return (long) (int) memoryBank.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Integer.class, false)
                }));

                //short to long
                program.getBank().getFunctions().put(new Pair<>("long", List.of(new Class<?>[]{Short.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(MemoryBank memoryBank) {
                                setReturnExecution(true);
                                return (long) (short) memoryBank.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Short.class, false)
                }));

                //byte to long
                program.getBank().getFunctions().put(new Pair<>("long", List.of(new Class<?>[]{Byte.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(MemoryBank memoryBank) {
                                setReturnExecution(true);
                                return (long) (byte) memoryBank.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Byte.class, false)
                }));

                //string to long
                program.getBank().getFunctions().put(new Pair<>("long", List.of(new Class<?>[]{String.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(MemoryBank memoryBank) {
                                setReturnExecution(true);
                                return Long.parseLong((String) memoryBank.getValue("x"));
                            }
                        }
                }, new Argument[]{
                        new Argument("x", String.class, false)
                }));



                //double to int
                program.getBank().getFunctions().put(new Pair<>("int", List.of(new Class<?>[]{Double.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(MemoryBank memoryBank) {
                                setReturnExecution(true);
                                return (int) (double) memoryBank.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Double.class, false)
                }));
                //float to int
                program.getBank().getFunctions().put(new Pair<>("int", List.of(new Class<?>[]{Float.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(MemoryBank memoryBank) {
                                setReturnExecution(true);
                                return (int) (float) memoryBank.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Float.class, false)
                }));
                //long to int
                program.getBank().getFunctions().put(new Pair<>("int", List.of(new Class<?>[]{Long.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(MemoryBank memoryBank) {
                                setReturnExecution(true);
                                return (int) (long) memoryBank.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Long.class, false)
                }));

                //short to int
                program.getBank().getFunctions().put(new Pair<>("int", List.of(new Class<?>[]{Short.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(MemoryBank memoryBank) {
                                setReturnExecution(true);
                                return (int) (short) memoryBank.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Short.class, false)
                }));

                //byte to int
                program.getBank().getFunctions().put(new Pair<>("int", List.of(new Class<?>[]{Byte.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(MemoryBank memoryBank) {
                                setReturnExecution(true);
                                return (int) (byte) memoryBank.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Byte.class, false)
                }));

                //string to int
                program.getBank().getFunctions().put(new Pair<>("int", List.of(new Class<?>[]{String.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(MemoryBank memoryBank) {
                                setReturnExecution(true);
                                return Integer.parseInt((String) memoryBank.getValue("x"));
                            }
                        }
                }, new Argument[]{
                        new Argument("x", String.class, false)
                }));




                //double to short
                program.getBank().getFunctions().put(new Pair<>("short", List.of(new Class<?>[]{Double.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(MemoryBank memoryBank) {
                                setReturnExecution(true);
                                return (short) (double) memoryBank.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Double.class, false)
                }));
                //float to short
                program.getBank().getFunctions().put(new Pair<>("short", List.of(new Class<?>[]{Float.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(MemoryBank memoryBank) {
                                setReturnExecution(true);
                                return (short) (float) memoryBank.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Float.class, false)
                }));
                //long to short
                program.getBank().getFunctions().put(new Pair<>("short", List.of(new Class<?>[]{Long.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(MemoryBank memoryBank) {
                                setReturnExecution(true);
                                return (short) (long) memoryBank.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Long.class, false)
                }));

                //int to short
                program.getBank().getFunctions().put(new Pair<>("short", List.of(new Class<?>[]{Short.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(MemoryBank memoryBank) {
                                setReturnExecution(true);
                                return (short) (int) memoryBank.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Short.class, false)
                }));

                //byte to short
                program.getBank().getFunctions().put(new Pair<>("short", List.of(new Class<?>[]{Byte.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(MemoryBank memoryBank) {
                                setReturnExecution(true);
                                return (short) (byte) memoryBank.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Byte.class, false)
                }));

                //string to short
                program.getBank().getFunctions().put(new Pair<>("short", List.of(new Class<?>[]{String.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(MemoryBank memoryBank) {
                                setReturnExecution(true);
                                return Short.parseShort((String) memoryBank.getValue("x"));
                            }
                        }
                }, new Argument[]{
                        new Argument("x", String.class, false)
                }));




                //double to byte
                program.getBank().getFunctions().put(new Pair<>("byte", List.of(new Class<?>[]{Double.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(MemoryBank memoryBank) {
                                setReturnExecution(true);
                                return (byte) (double) memoryBank.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Double.class, false)
                }));
                //float to byte
                program.getBank().getFunctions().put(new Pair<>("byte", List.of(new Class<?>[]{Float.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(MemoryBank memoryBank) {
                                setReturnExecution(true);
                                return (byte) (float) memoryBank.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Float.class, false)
                }));
                //long to byte
                program.getBank().getFunctions().put(new Pair<>("byte", List.of(new Class<?>[]{Long.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(MemoryBank memoryBank) {
                                setReturnExecution(true);
                                return (byte) (long) memoryBank.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Long.class, false)
                }));

                //int to byte
                program.getBank().getFunctions().put(new Pair<>("byte", List.of(new Class<?>[]{Short.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(MemoryBank memoryBank) {
                                setReturnExecution(true);
                                return (byte) (int) memoryBank.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Short.class, false)
                }));

                //short to byte
                program.getBank().getFunctions().put(new Pair<>("byte", List.of(new Class<?>[]{Byte.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(MemoryBank memoryBank) {
                                setReturnExecution(true);
                                return (byte) (short) memoryBank.getValue("x");
                            }
                        }
                }, new Argument[]{
                        new Argument("x", Byte.class, false)
                }));

                //string to byte
                program.getBank().getFunctions().put(new Pair<>("byte", List.of(new Class<?>[]{String.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(MemoryBank memoryBank) {
                                setReturnExecution(true);
                                return Byte.parseByte((String) memoryBank.getValue("x"));
                            }
                        }
                }, new Argument[]{
                        new Argument("x", String.class, false)
                }));

                //function: class(String className)
                //Get class from string
                program.getBank().getFunctions().put(new Pair<>("class", List.of(new Class<?>[]{String.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(MemoryBank memoryBank) {
                                setReturnExecution(true);
                                return RunUtils.forName((String) memoryBank.getValue("className"));
                            }
                        }
                }, new Argument[]{
                        new Argument("className", String.class, false)
                }));

                //function: new(Class class, ArrayArgs args)
                //New instance of a class
                program.getBank().getFunctions().put(new Pair<>("new", List.of(new Class<?>[]{Class.class, ArrayArgs.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(MemoryBank memoryBank) {
                                setReturnExecution(true);
                                ArrayArgs args = (ArrayArgs) memoryBank.getValue("args");
                                try {
                                    return ((Class<?>) memoryBank.getValue("class")).getConstructor(args.getTypesArray()).newInstance(args.getObjectArray());
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





                //function: include(String file)
                //This function execute and attach other ys file in this program.
                program.getBank().getFunctions().put(new Pair<>("include", List.of(new Class<?>[]{String.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(MemoryBank memoryBank) {
                                Program toInclude = new Program(SourceUtils.fromRaw(FileUtils.readFile(new File(program.getLibsFolder(), memoryBank.getValue("file") + ".ys"))));
                                addLibs(toInclude, libVersion);
                                toInclude.interpret(new Interpreter());
                                toInclude.run();
                                memoryBank.getVariables().putAll(toInclude.getBank().getVariables());
                                memoryBank.getPointers().putAll(toInclude.getBank().getPointers());
                                return null;
                            }
                        }
                }, new Argument[]{
                        new Argument("file", String.class, false)
                }));
                //function: include(String file, String libVersion)
                //This function execute and attach other ys file in this program.
                program.getBank().getFunctions().put(new Pair<>("include", List.of(new Class<?>[]{String.class, String.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(MemoryBank memoryBank) {
                                Program toInclude = new Program(SourceUtils.fromRaw(FileUtils.readFile(new File(program.getLibsFolder(), memoryBank.getValue("file") + ".ys"))));
                                addLibs(toInclude, LibVersion.valueOf((String) memoryBank.getValue("libVersion")));
                                toInclude.interpret(new Interpreter());
                                toInclude.run();
                                memoryBank.getVariables().putAll(toInclude.getBank().getVariables());
                                memoryBank.getPointers().putAll(toInclude.getBank().getPointers());
                                return null;
                            }
                        }
                }, new Argument[]{
                        new Argument("file", String.class, false),
                        new Argument("libVersion", String.class, false)
                }));
                //function: puts(String x)
                //This function prints a String into de standard output stream
                program.getBank().getFunctions().put(new Pair<>("puts", List.of(new Class<?>[]{String.class})), new Function(new Instruction[]{
                        new Instruction() {
                            @Override
                            public Object call(MemoryBank memoryBank) {
                                System.out.println(memoryBank.getValue("x"));
                                return null;
                            }
                        }
                }, new Argument[]{
                        new Argument("x", String.class, false)
                }));
                break;
            default:
                throw new IllegalArgumentException("'" + libVersion + "' is not compatible with this interpreter.");
        }
    }
}
