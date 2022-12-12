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

package com.xebisco.yieldscript.interpreter.utils;

import com.xebisco.yieldscript.interpreter.Constants;
import com.xebisco.yieldscript.interpreter.info.ProjectInfo;
import com.xebisco.yieldscript.interpreter.Script;
import com.xebisco.yieldscript.interpreter.instruction.MethodCall;
import com.xebisco.yieldscript.interpreter.memory.Bank;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;
import java.util.regex.Matcher;

public class ScriptUtils {
    public static Script createScript(InputStream inputStream, ProjectInfo projectInfo) {
        Pair<String, Map<String, String>> pair = ParseUtils.extractStringLiterals(ParseUtils.removeComments(FileUtils.readInputStream(inputStream)));
        String[] source = ParseUtils.parseChars(ParseUtils.removeUnnecessaryWhiteSpace(pair.getFirst())).split(String.valueOf(Constants.SOURCE_BREAK));
        return new Script(source, projectInfo, pair.getSecond());
    }

    public static Script createScript(File file, ProjectInfo projectInfo) {
        try {
            return createScript(new FileInputStream(file), projectInfo);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void attachBank(Bank bank, Bank otherBank) {
        bank.getFunctions().putAll(otherBank.getFunctions());
        bank.getObjects().putAll(otherBank.getObjects());
    }

    /*public static Object callFunction(String functionName, Bank bank, Object... args) {
        return bank.getFunctions().get(new Pair<>(functionName, ObjectUtils.getObjectTypes(args)));
    }*/

    @SuppressWarnings("RegExpRedundantEscape")
    public static MethodCall[] getArguments(String argsSource) {
        String[] args = argsSource.split("\\(.*?\\)|(\\,)");
        MethodCall[] arguments = new MethodCall[args.length];
        for (int i = 0; i < arguments.length; i++) {
            arguments[i] = methodCall(args[i], null);
        }
        return arguments;
    }

    public static MethodCall methodCall(String line, MethodCall parent) {
        Matcher matcher = Constants.CLASS_METHOD_CALL_PATTERN.matcher(line);
        Class<?> clazz;
        if (matcher.matches()) {
            try {
                clazz = Class.forName(matcher.group(1));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            return new MethodCall(clazz, getArguments(matcher.group(3)), parent, matcher.group(2), null);
        } else if (matcher.usePattern(Constants.CLASS_FIELD_PATTERN).matches()) {
            try {
                clazz = Class.forName(matcher.group(1));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            return new MethodCall(clazz, matcher.group(2), parent);
        } else if (matcher.usePattern(Constants.CLASS_METHODS_CALL_PATTERN).matches()) {
            String f = matcher.group(2);
            if (matcher.group(2).contains(".")) {
                String[] pcs = matcher.group(2).split("\\(.*?\\)|(\\.)");
                MethodCall[] calls = new MethodCall[pcs.length - 1];

                for (int i = 0; i < calls.length; i++) {
                    MethodCall mParent = null;
                    if (i > 0) {
                        mParent = calls[i - 1];
                    }
                    calls[i] = methodCall("(" + matcher.group(1) + ")" + pcs[i], mParent);
                }
                parent = calls[calls.length - 1];
                f = pcs[pcs.length - 1];
            }
            return new MethodCall(null, getArguments(matcher.group(3)), parent, f, null);
        } else if (matcher.usePattern(Constants.CLASS_FIELDS_PATTERN).matches()) {
            String f = matcher.group(2);
            if (matcher.group(2).contains(".")) {
                String[] pcs = matcher.group(2).split("\\(.*?\\)|(\\.)");
                MethodCall[] calls = new MethodCall[pcs.length - 1];

                for (int i = 0; i < calls.length; i++) {
                    MethodCall mParent = null;
                    if (i > 0) {
                        mParent = calls[i - 1];
                    }
                    calls[i] = methodCall("(" + matcher.group(1) + ")"  + pcs[i], mParent);
                }
                parent = calls[calls.length - 1];
                f = pcs[pcs.length - 1];
            }
            return new MethodCall(null, f, parent);
        } else if (matcher.usePattern(Constants.METHODS_CALL_PATTERN).matches()) {
            String f = matcher.group(1);
            if (matcher.group(1).contains(".")) {
                String[] pcs = matcher.group(2).split("\\(.*?\\)|(\\.)");
                MethodCall[] calls = new MethodCall[pcs.length - 1];

                for (int i = 0; i < calls.length; i++) {
                    MethodCall mParent = null;
                    if (i > 0) {
                        mParent = calls[i - 1];
                    }
                    calls[i] = methodCall(pcs[i], mParent);
                }
                parent = calls[calls.length - 1];
                f = pcs[pcs.length - 1];
            }
            return new MethodCall(null, getArguments(matcher.group(2)), parent, f, null);
        } else if (matcher.usePattern(Constants.FIELDS_CALL_PATTERN).matches()) {
            String f = matcher.group(1);
            if (matcher.group(1).contains(".")) {
                String[] pcs = matcher.group(1).split("\\(.*?\\)|(\\.)");
                MethodCall[] calls = new MethodCall[pcs.length - 1];

                for (int i = 0; i < calls.length; i++) {
                    MethodCall mParent = null;
                    if (i > 0) {
                        mParent = calls[i - 1];
                    }
                    calls[i] = methodCall(pcs[i], mParent);
                }
                parent = calls[calls.length - 1];
                f = pcs[pcs.length - 1];
            }
            return new MethodCall(null, f, parent);
        } else {
            return new MethodCall(null, line, parent);
        }
    }
}
