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
import com.xebisco.yieldscript.interpreter.memory.Bank;

import java.util.List;

public class FunctionUtils {
    public static void putArgsInBank(String[] argumentsNames, Bank functionBank, Bank bank, List<String> cachedVariableNames) {
        for (String arg : argumentsNames) {
            if (bank.getObjects().containsKey(arg)) {
                cachedVariableNames.add(arg);
                bank.getObjects().put("$" + arg, bank.getObjects().get(arg));
                bank.getObjects().remove(arg);
            }
            functionBank.getObjects().put(arg, bank.getObjects().get(Constants.FUNCTION_ARGUMENT_ID_CHAR + arg));
            bank.getObjects().remove(Constants.FUNCTION_ARGUMENT_ID_CHAR + arg);
        }
    }

    public static void addCachedVariables(Bank bank, List<String> cachedVariableNames) {
        for (String cachedVariable : cachedVariableNames) {
            bank.getObjects().put(cachedVariable, bank.getObjects().get("$" + cachedVariable));
            bank.getObjects().remove("$" + cachedVariable);
        }
        cachedVariableNames.clear();
    }
}
