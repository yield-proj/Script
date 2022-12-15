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
import com.xebisco.yieldscript.interpreter.type.TypeModifier;
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
        for (Type type : Type.values()) {
            Variable variable = new Variable(type.name(), Type._class);
            variable.setModifiers(TypeModifier._get, TypeModifier._set);
            variable.setValue(type.getJavaClass());
            variable.setModifiers(TypeModifier._none);
            bank.getObjects().put(type.name().substring(1), variable);
        }
        for (String id : stringLiterals.keySet()) {
            Variable variable = new Variable(Constants.STRING_LITERAL_ID_CHAR + String.valueOf(id), Type._string);
            variable.setModifiers(TypeModifier._get, TypeModifier._set);
            variable.setValue(stringLiterals.get(id));
            variable.setModifiers(TypeModifier._get);
            bank.getObjects().put(id, variable);
        }
        Variable trueVariable = new Variable("true", Type._boolean);
        trueVariable.setModifiers(TypeModifier._get, TypeModifier._set);
        trueVariable.setValue(true);
        trueVariable.setModifiers(TypeModifier._get);
        bank.getObjects().put(trueVariable.getName(), trueVariable);
        Variable falseVariable = new Variable("false", Type._boolean);
        falseVariable.setModifiers(TypeModifier._get, TypeModifier._set);
        falseVariable.setValue(false);
        falseVariable.setModifiers(TypeModifier._get);
        bank.getObjects().put(falseVariable.getName(), falseVariable);
    }

    public void createInstructions() {
        createInstructions(new ExecutableCreator());
    }

    public void createInstructions(IExecutableCreator instructionCreator) {
        instructions = ScriptUtils.createInstructions(instructionCreator, getSource(), getProjectInfo(), getBank());
    }

    public boolean execute() {
        boolean result = ScriptUtils.executeInstructions(getInstructions(), getBank());
        Function main = getBank().getFunctions().get(new Pair<>("main", List.of(new Class<?>[0])));
        if(main != null) main.execute(getBank());
        return result;
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
