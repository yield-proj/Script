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

package com.xebisco.ys.calls;

import com.xebisco.ys.exceptions.SyntaxException;
import com.xebisco.ys.exceptions.VariableDontExistException;
import com.xebisco.ys.types.Struct;

public class SetVariable extends Instruction {
    private final String variable;
    private final Instruction instruction;

    public SetVariable(String variable, Instruction instruction) {
        this.variable = variable;
        this.instruction = instruction;
    }

    @Override
    public Object call(ValueMod valueMod) {
        Object o;
        if (variable.contains(".")) {
            String[] pcs = variable.split("\\.");
            if (pcs.length != 2) throw new SyntaxException(variable);
            ((Struct) valueMod.getValue(pcs[0])).getFields().replace(pcs[1], instruction.call(new ValueMod(0, valueMod.getMemoryBank(), valueMod.isAllowLowSecurity())));
            o = ((Struct) valueMod.getValue(pcs[0])).getFields().get(pcs[1]);
        } else {
            o = valueMod.getMemoryBank().getVariables().replace(variable, instruction.call(new ValueMod(0, valueMod.getMemoryBank(), valueMod.isAllowLowSecurity())));
        }
        if (o == null) throw new VariableDontExistException(variable);
        return o;
    }

    public Instruction getInstruction() {
        return instruction;
    }

    public String getVariable() {
        return variable;
    }
}
