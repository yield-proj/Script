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

import com.xebisco.ys.LastScopeInstruction;
import com.xebisco.ys.calls.Instruction;
import com.xebisco.ys.calls.LowSecurityInstruction;
import com.xebisco.ys.calls.ValueMod;
import com.xebisco.ys.memory.MemoryBank;

import java.util.*;

public class RunUtils {

    public final static Set<String> PACKAGES = new HashSet<>();
    public final static Map<Long, String> STRING_LITERALS = new HashMap<>();

    public static long stringLiteralIndex = Long.parseUnsignedLong("0");

    static {
        PACKAGES.add("java.lang");
        PACKAGES.add("com.xebisco.ys.types");
    }

    public static Object run(MemoryBank memoryBank, List<Instruction> instructions, boolean allowLowSecurity) {
        return run(memoryBank, instructions, new Random().nextLong(), allowLowSecurity);
    }

    public static Object run(MemoryBank memoryBank, List<Instruction> instructions, long valueModID, boolean allowLowSecurity) {
        ValueMod valueMod = new ValueMod(valueModID, memoryBank, allowLowSecurity);
        return run(valueMod, instructions);
    }

    public static Object run(ValueMod valueMod, List<Instruction> instructions) {
        return run(valueMod, null, instructions);
    }

    public static Object run(ValueMod valueMod, ValueMod lastScopeValueMod, List<Instruction> instructions) {
        for (Instruction instruction : instructions) {
            Object o;
            if(instruction instanceof LowSecurityInstruction && !valueMod.isAllowLowSecurity()) {
                throw new SecurityException("This execution does not support low security instructions.");
            }
            if (instruction instanceof LastScopeInstruction)
                o = ((LastScopeInstruction) instruction).call(valueMod, lastScopeValueMod);
            else
                o = instruction.call(valueMod);
            if (instruction.isReturnExecution())
                return o;
        }
        return null;
    }

    public static Class<?> forName(String clazz) {
        try {
            return Class.forName(clazz);
        } catch (ClassNotFoundException e) {
            switch (clazz) {
                case "double":
                    return double.class;
                case "float":
                    return float.class;
                case "long":
                    return long.class;
                case "int":
                    return int.class;
                case "short":
                    return short.class;
                case "byte":
                    return byte.class;
                case "char":
                    return char.class;
            }
            for (String pkg : PACKAGES) {
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
}
