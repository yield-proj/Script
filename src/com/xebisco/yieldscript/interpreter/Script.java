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
import com.xebisco.yieldscript.interpreter.info.ProjectInfo;
import com.xebisco.yieldscript.interpreter.instruction.Executable;
import com.xebisco.yieldscript.interpreter.instruction.Instruction;
import com.xebisco.yieldscript.interpreter.memory.Bank;
import com.xebisco.yieldscript.interpreter.memory.Function;
import com.xebisco.yieldscript.interpreter.memory.Variable;
import com.xebisco.yieldscript.interpreter.type.Type;
import com.xebisco.yieldscript.interpreter.utils.Pair;
import com.xebisco.yieldscript.interpreter.utils.ParseUtils;
import com.xebisco.yieldscript.interpreter.utils.ScriptUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

public class Script {
    private final String[] source;
    private final ProjectInfo projectInfo;
    private List<Pair<Instruction, String[]>> instructions;
    private final Bank bank;

    public Script(String[] source, ProjectInfo projectInfo, Map<String, String> stringLiterals) {
        this.source = source;
        this.projectInfo = projectInfo;
        bank = new Bank();
        Variable nullVar = new Variable("null", Type._def);
        bank.getObjects().put("null", nullVar);
        for (String id : stringLiterals.keySet()) {
            Variable variable = new Variable(Constants.STRING_LITERAL_ID_CHAR + String.valueOf(id), Type._string);
            variable.setValue(stringLiterals.get(id));
            bank.getObjects().put(id, variable);
        }
    }

    public void createInstructions() {
        createInstructions(new ExecutableCreator());
    }

    public boolean createInstructions(IExecutableCreator instructionCreator) {
        instructions = new ArrayList<>();
        List<String> toSetVars = new ArrayList<>();
        for (int i = 0; i < source.length; i++) {
            try {
                toSetVars.clear();
                String line = source[i];
                Matcher matcher = Constants.SET_AS_PATTERN.matcher(line);
                while (matcher.matches()) {
                    toSetVars.add(matcher.group(1));
                    line = line.substring(line.indexOf('='));
                    matcher = Constants.SET_AS_PATTERN.matcher(line);
                }
                Executable executable = instructionCreator.create(line, projectInfo);
                if (executable != null) {
                    if (executable instanceof Instruction)
                        instructions.add(new Pair<>((Instruction) executable, toSetVars.toArray(new String[0])));
                    else if(executable instanceof Function)
                        bank.getFunctions().put(new Pair<>(((Function) executable).getName(), Arrays.asList(((Function) executable).getArgumentsTypes())), (Function) executable);
                }

            } catch (Exception e) {
                new InstructionCreationException(e.getClass().getSimpleName() + ". in line " + (i + 1) + ": " + source[i]).printStackTrace();
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public boolean execute() {
        return ScriptUtils.executeInstructions(instructions, getBank());
    }

    public List<Pair<Instruction, String[]>> getInstructions() {
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
