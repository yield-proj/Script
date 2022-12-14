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

package com.xebisco.yieldscript.interpreter.memory;

import com.xebisco.yieldscript.interpreter.instruction.VariableDeclaration;
import com.xebisco.yieldscript.interpreter.type.Data;
import com.xebisco.yieldscript.interpreter.type.TypeModifier;
import com.xebisco.yieldscript.interpreter.utils.FunctionUtils;
import com.xebisco.yieldscript.interpreter.utils.ScriptUtils;
import com.xebisco.yieldscript.interpreter.utils.VariableUtils;

import java.util.Arrays;

public class DataDeclaration extends Function {
    private final String name;
    private final VariableDeclaration[] values;

    public DataDeclaration(String name, VariableDeclaration[] values, VariableDeclaration[] args) {
        super(name, VariableUtils.getTypes(args), VariableUtils.getNames(args), DataDeclaration.class);
        this.name = name;
        this.values = values;
    }

    @Override
    public Object execute(Bank bank) {
        Variable[] variables = new Variable[values.length];
        FunctionUtils.putArgsInBank(getArgumentsNames(), getFunctionBank(), bank, getCachedVariableNames());
        ScriptUtils.attachBank(getFunctionBank(), bank);
        for(int i = 0 ; i < variables.length; i++) {
            VariableDeclaration declaration = values[i];
            if(Arrays.stream(declaration.getModifiers()).anyMatch(m -> m == TypeModifier._arg))
                variables[i] = getFunctionBank().getObjects().get(declaration.getName());
            else {
                declaration.execute(getFunctionBank());
                variables[i] = declaration.getVarObject();
            }
        }
        FunctionUtils.addCachedVariables(bank, getCachedVariableNames());
        getFunctionBank().clear();
        return new Data(variables);
    }

    @Override
    public String getName() {
        return name;
    }

    public VariableDeclaration[] getValues() {
        return values;
    }
}
