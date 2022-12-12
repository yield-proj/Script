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

import com.xebisco.yieldscript.interpreter.exceptions.InstructionCreationException;
import com.xebisco.yieldscript.interpreter.instruction.Instruction;
import com.xebisco.yieldscript.interpreter.memory.Bank;
import com.xebisco.yieldscript.interpreter.memory.Variable;
import com.xebisco.yieldscript.interpreter.type.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Script {
    private final String[] source;
    private final ProjectInfo projectInfo;
    private List<Instruction> instructions;
    private final Bank bank;

    public Script(String[] source, ProjectInfo projectInfo, Map<Long, String> stringLiterals) {
        this.source = source;
        this.projectInfo = projectInfo;
        bank = new Bank();
        bank.getObjects().put("null", null);
        for(long id : stringLiterals.keySet()) {
            Variable variable = new Variable(Constants.STRING_LITERAL_ID_CHAR + String.valueOf(id), Type._string);
            variable.setValue(stringLiterals.get(id));
            bank.getObjects().put(id, variable);
        }
    }

    public void createInstructions() {
        createInstructions(new InstructionCreator());
    }

    public void createInstructions(IInstructionCreator instructionCreator) {
        instructions = new ArrayList<>();
        for(int i = 0 ; i < source.length; i++) {
            try {
                instructions.add(instructionCreator.create(source[i]));
            } catch (Exception e) {
                throw new InstructionCreationException(e.getClass().getSimpleName() + ": " + e.getMessage() + ". in line " + i + ": " + source[i]);
            }
        }
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }

    public Bank getBank() {
        return bank;
    }

    public ProjectInfo getProjectInfo() {
        return projectInfo;
    }

    public String[] getSource() {
        return source;
    }
}
