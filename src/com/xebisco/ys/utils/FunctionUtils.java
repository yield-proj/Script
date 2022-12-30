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
import com.xebisco.ys.Constants;
import com.xebisco.ys.calls.*;
import com.xebisco.ys.exceptions.FunctionNotFoundException;
import com.xebisco.ys.memory.MemoryBank;
import com.xebisco.ys.types.ArrayArgs;
import com.xebisco.ys.types.Unit;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class FunctionUtils {
    public static Object call(MemoryBank bank, String functionName, FunctionCall[] args) {
        MemoryBank functionBank = new MemoryBank();
        functionBank.getPointers().putAll(bank.getPointers());
        functionBank.setVariables(bank.getVariables());
        functionBank.setFunctions(bank.getFunctions());
        functionBank.setStringLiterals(bank.getStringLiterals());
        List<Class<?>> types = new ArrayList<>();
        Object[] argObjects = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            argObjects[i] = args[i].call(bank);
            if (args[i].getCast() == null)
                try {
                    types.add(argObjects[i].getClass());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    throw new IllegalStateException("Calling '" + functionName + "' with a null parameter!");
                }
            else
                types.add(args[i].getCast());
        }
        Function function = bank.getFunctions().get(new Pair<>(functionName, types));
        if (function == null) {
            List<Class<?>> cTypes = new ArrayList<>();
            cTypes.add(ArrayArgs.class);
            function = bank.getFunctions().get(new Pair<>(functionName, cTypes));
            if (function == null) {
                for (Class<?> type : types) {
                    cTypes.add(0, type);
                    function = bank.getFunctions().get(new Pair<>(functionName, cTypes));
                    if (function != null) break;
                }
            }
            if (function == null) throw new FunctionNotFoundException(functionName + ' ' + types);
            ArrayArgs arrayArgs = new ArrayArgs(null, args.length - cTypes.size() + 1);
            for (int i = 0; i < arrayArgs.getObjectArray().length; i++) {
                arrayArgs.set(i, argObjects[i + cTypes.size() - 1]);
                if (args[i + cTypes.size() - 1].getCast() == null)
                    arrayArgs.getTypesArray()[i] = argObjects[i + cTypes.size() - 1].getClass();
                else arrayArgs.getTypesArray()[i] = args[i + cTypes.size() - 1].getCast();
            }
            Object[] cArgs = new Object[cTypes.size() - 1];
            System.arraycopy(argObjects, 0, cArgs, 0, cArgs.length);
            argObjects = new Object[cTypes.size()];
            System.arraycopy(cArgs, 0, argObjects, 0, cArgs.length);
            argObjects[argObjects.length - 1] = arrayArgs;
        }
        for (int i = 0; i < argObjects.length; i++) {
            Argument argument = function.getArgs()[i];
            if (argument.isReference())
                functionBank.getVariables().put(argument.getName(), functionBank.getPointers().get((long) argObjects[i]));
            else
                functionBank.getVariables().put(argument.getName(), argObjects[i]);
        }
        Object o = RunUtils.run(functionBank, function.getInstructions());
        if (function.getReturnCast() == null)
            return o;
        else if (function.getReturnCast().equals(Unit.class)) return function.getReturnCast().cast(o);
        else return function.getReturnCast().cast(o);
    }

    public static Class<?>[] argTypes(Argument[] args) {
        Class<?>[] argTypes = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++)
            argTypes[i] = args[i].getType();
        return argTypes;
    }

    public static <T extends FunctionCall> T createCall(String line, Class<T> type, Class<?> cast) {
        if (line.length() > 0) {
            Matcher matcher = Constants.FUNCTION_CALL_PATTERN.matcher(line);
            if (matcher.matches()) {
                FunctionCall[] arguments = null;
                if (matcher.groupCount() > 1) {
                    String[] argStrings = ParseUtils.splitPattern(matcher.group(2));
                    arguments = new FunctionCall[argStrings.length];
                    for (int i = 0; i < arguments.length; i++) {
                        Matcher m = Constants.CAST_PATTERN.matcher(argStrings[i]);
                        if (m.matches()) {
                            arguments[i] = createCall(m.group(2), FunctionCall.class, RunUtils.forName(m.group(1)));
                        } else
                            arguments[i] = createCall(argStrings[i], FunctionCall.class, null);
                    }
                }
                if (arguments == null) arguments = new FunctionCall[0];
                try {
                    return type.getConstructor(String.class, FunctionCall[].class, Class.class).newInstance(matcher.group(1), arguments, cast);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }

            if (!line.startsWith(String.valueOf(Constants.STRING_LITERAL_CHAR)) && (line.contains("+") || line.contains("-") || line.contains("/") || line.contains("^") || line.substring(1).contains("*") || Constants.DECIMAL_NUMBER_PATTERN.matcher(line).matches() || Constants.NUMBER_PATTERN.matcher(line).matches()))
                //noinspection unchecked
                return (T) new EquationFunctionCall(line, cast);
            else
                //noinspection unchecked
                return (T) new VariableFromFunctionCall(line, cast);
        }
        //noinspection unchecked
        return (T) new NullFunctionCall(cast);
    }
}
