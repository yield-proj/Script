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

import com.xebisco.yieldscript.interpreter.instruction.VariableDeclaration;

public class VariableUtils {
    public static Class<?>[] getTypes(VariableDeclaration[] declarations) {
        Class<?>[] types = new Class<?>[declarations.length];
        for(int i = 0; i < types.length; i++)
            types[i] = declarations[i].getType().getJavaClass();
        return types;
    }

    public static String[] getNames(VariableDeclaration[] declarations) {
        String[] names = new String[declarations.length];
        for(int i = 0; i < names.length; i++)
            names[i] = declarations[i].getName();
        return names;
    }
}
