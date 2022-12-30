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

import com.xebisco.ys.calls.Instruction;
import com.xebisco.ys.memory.MemoryBank;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RunUtils {

    public final static Set<String> PACKAGES = new HashSet<>();

    static {
        PACKAGES.add("java.lang");
        PACKAGES.add("com.xebisco.ys.types");
    }

    public static Object run(MemoryBank memoryBank, List<Instruction> instructions) {
        for(Instruction instruction : instructions) {
            Object o = instruction.call(memoryBank);
            if (instruction.isReturnExecution())
                return o;
        }
        return null;
    }
    
    public static Class<?> forName(String clazz) {
        try {
            return Class.forName(clazz);
        } catch (ClassNotFoundException e) {
            for(String pkg : PACKAGES) {
                try {
                    return Class.forName(pkg + '.' + clazz);
                } catch (ClassNotFoundException ignore) {
                }
            }
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }

    public static Object runOnSpecificBank(MemoryBank memoryBank, List<Instruction> instructions) {
        MemoryBank bank = new MemoryBank();
        bank.getVariables().putAll(memoryBank.getVariables());
        bank.getPointers().putAll(memoryBank.getPointers());
        bank.getFunctions().putAll(memoryBank.getFunctions());
        bank.getStringLiterals().putAll(memoryBank.getStringLiterals());
        for(Instruction instruction : instructions) {
            Object o = instruction.call(memoryBank);
            if (instruction.isReturnExecution())
                return o;
        }
        return null;
    }
}
