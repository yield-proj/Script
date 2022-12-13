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

public class ForStatement extends Function implements Instruction {

    private final String varName, limitName, operationName;

    public ForStatement(String varName, String limitName, String operationName) {
        super(null, null, null, null);
        this.varName = varName;
        this.limitName = limitName;
        this.operationName = operationName;
    }


    @Override
    public Object execute(Bank bank) {
        int repeat = (int) bank.getObject(limitName);
        Variable index = new Variable(varName, Type._int);
        index.setModifiers(TypeModifier._set);
        ScriptUtils.attachBank(getFunctionBank(), bank);
        getFunctionBank().getObjects().put(varName, index);
        for(int i = 0; i <= (int) getFunctionBank().getObject(limitName); ScriptUtils.methodCall(operationName, null).execute(getFunctionBank())) {
            System.out.println(i);
        }
        System.out.println(repeat);
        getFunctionBank().clear();
        return null;
    }

    public String getVarName() {
        return varName;
    }

    public String getLimitName() {
        return limitName;
    }

    public String getOperationName() {
        return operationName;
    }
}
