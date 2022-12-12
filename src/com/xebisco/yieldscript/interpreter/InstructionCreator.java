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

package com.xebisco.yieldscript.interpreter;

import com.xebisco.yieldscript.interpreter.instruction.VariableDeclaration;
import com.xebisco.yieldscript.interpreter.instruction.Instruction;
import com.xebisco.yieldscript.interpreter.type.Type;
import com.xebisco.yieldscript.interpreter.type.TypeModifier;

import java.util.Arrays;
import java.util.regex.Matcher;

public class InstructionCreator implements IInstructionCreator {

    @Override
    public Instruction create(String source) {
        Matcher matcher = Constants.DECLARATION_PATTERN.matcher(source);
        if (matcher.matches()) {
            String[] mods = matcher.group(4).split(",");
            TypeModifier[] modifiers = new TypeModifier[mods.length];
            for (int i = 0; i < modifiers.length; i++)
                modifiers[i] = TypeModifier.getModifier(mods[i]);
            return new VariableDeclaration(matcher.group(1), matcher.group(3), Type.getType(matcher.group(2)), modifiers);
        }
        matcher = Constants.DECLARATION_PATTERN_DEFAULT_VALUE.matcher(source);
        if (matcher.matches()) {
            String[] mods = matcher.group(3).split(",");
            TypeModifier[] modifiers = new TypeModifier[mods.length];
            for (int i = 0; i < modifiers.length; i++)
                modifiers[i] = TypeModifier.getModifier(mods[i]);
            Type type = Type.getType(matcher.group(2));
            return new VariableDeclaration(matcher.group(1), null, Type.getType(matcher.group(2)), modifiers);
        }
        return null;
    }
}
