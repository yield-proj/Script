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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

public class FunctionUtils {
    public static Object call(MemoryBank bank, String functionName, FunctionCall[] args) {
        MemoryBank functionBank = new MemoryBank();
        functionBank.getPointers().putAll(bank.getPointers());
        functionBank.setVariables(bank.getVariables());
        List<Class<?>> types = new ArrayList<>();
        Object[] argObjects = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            argObjects[i] = args[i].call(bank);
            types.add(argObjects[i].getClass());
        }
        Function function = bank.getFunctions().get(new Pair<>(functionName, types));
        if(function == null) throw new FunctionNotFoundException(functionName + ' ' + types);
        for (int i = 0; i < args.length; i++) {
            functionBank.getVariables().put(function.getArgs()[i].getName(), argObjects[i]);
        }
        for (Instruction instruction : function.getInstructions()) {
            Object o = instruction.call(functionBank);
            if (instruction.isReturnExecution())
                return o;
        }
        return null;
    }



    public static FunctionCall createCall(String line) {
        Matcher matcher = Constants.FUNCTION_CALL_PATTERN.matcher(line);
        if (matcher.matches()) {
            FunctionCall[] arguments = new FunctionCall[0];
            if (matcher.groupCount() > 1) {
                String[] argStrings = ParseUtils.splitPattern(matcher.group(2));
                arguments = new FunctionCall[argStrings.length];
                for (int i = 0; i < arguments.length; i++) {
                    arguments[i] = createCall(argStrings[i]);
                }
            }
            return new FunctionCall(matcher.group(1), arguments);
        }
        return new VariableFromFunctionCall(line);
    }
}
