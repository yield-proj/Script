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

public class ObjectUtils {
    public static Object toObject(String s) {
        s = s.trim();
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException ignore) {
            try {
                if (!s.endsWith("L")) throw new NumberFormatException();
                return Long.parseLong(s.substring(0, s.length() - 1));
            } catch (NumberFormatException ignore1) {
                try {
                    if (!s.endsWith("f")) throw new NumberFormatException();
                    return Float.parseFloat(s.substring(0, s.length() - 1));
                } catch (NumberFormatException ignore2) {
                    try {
                        return Double.parseDouble(s);
                    } catch (NumberFormatException ignore3) {
                        return null;
                    }
                }
            }
        }
    }

    public static Class<?>[] getObjectTypes(Object[] objects) {
        Class<?>[] types = new Class<?>[objects.length];
        for (int i = 0; i < types.length; i++)
            types[i] = objects.getClass();
        return types;
    }
}
