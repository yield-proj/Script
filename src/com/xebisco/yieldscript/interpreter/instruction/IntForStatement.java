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

package com.xebisco.yieldscript.interpreter.instruction;

import com.xebisco.yieldscript.interpreter.memory.Bank;
import com.xebisco.yieldscript.interpreter.memory.Function;
import com.xebisco.yieldscript.interpreter.memory.Variable;
import com.xebisco.yieldscript.interpreter.type.Type;
import com.xebisco.yieldscript.interpreter.type.TypeModifier;
import com.xebisco.yieldscript.interpreter.utils.ScriptUtils;

public class IntForStatement extends Function implements Instruction {

    private final String start, length, index;
    private final boolean invert;

    public IntForStatement(String index, String start, String length, boolean invert) {
        super(null, null, null, null);
        this.start = start;
        this.length = length;
        this.invert = invert;
        this.index = index;
    }


    @Override
    public Object execute(Bank bank) {
        int length = ScriptUtils.integerValue((Number) bank.getObject(this.length)), start = ScriptUtils.integerValue((Number) bank.getObject(this.start));
        Variable index = bank.getObjects().get(this.index);
        if (index == null) {
            index = new Variable(this.index, Type._int);
            index.setModifiers(TypeModifier._set, TypeModifier._get);
            ScriptUtils.attachBank(getFunctionBank(), bank);
            getFunctionBank().getObjects().put(this.index, index);
        }
        if (invert) {
            for (int i = length; i >= start; i--) {
                index.setValue(i);
                ScriptUtils.executeInstructions(getInstructions(), getFunctionBank());
            }
        } else {
            for (int i = start; i <= length; i++) {
                index.setValue(i);
                ScriptUtils.executeInstructions(getInstructions(), getFunctionBank());
            }
        }
        getFunctionBank().clear();
        return null;
    }

    public String getStart() {
        return start;
    }

    public String getLength() {
        return length;
    }
}
