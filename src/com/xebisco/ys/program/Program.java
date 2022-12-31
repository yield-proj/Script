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

package com.xebisco.ys.program;

import com.xebisco.yieldutils.Pair;
import com.xebisco.ys.calls.Call;
import com.xebisco.ys.calls.Function;
import com.xebisco.ys.calls.Instruction;
import com.xebisco.ys.exceptions.MissingLibraryException;
import com.xebisco.ys.memory.LibraryBank;
import com.xebisco.ys.memory.MemoryBank;
import com.xebisco.ys.utils.FunctionUtils;
import com.xebisco.ys.utils.RunUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Program {
    private MemoryBank bank;
    private final Source source;
    private File libsFolder = new File("./");
    private List<Instruction> instructions;
    private boolean allowLowSecurity;

    public Program(Source source) {
        this.source = source;
        bank = new MemoryBank();
    }

    public Program(Source source, MemoryBank bank) {
        this.source = source;
        this.bank = bank;
    }

    public void interpret(IInterpreter interpreter) {
        bank.getLibraries().add(new Library("."));
        instructions = new ArrayList<>();
        for (String line : source.getContents()) {
            Call call = interpreter.createInstruction(line);
            if (call instanceof Instruction)
                instructions.add((Instruction) call);
            else if(call instanceof Function) {
                bank.getLibrary(".").getFunctions().put(new Pair<>(((Function) call).getTName(), List.of(FunctionUtils.argTypes(((Function) call).getArgs()))), (Function) call);
            }
        }
    }

    public void run() {
        RunUtils.run(bank, instructions, 0, allowLowSecurity);
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<Instruction> instructions) {
        this.instructions = instructions;
    }

    public File getLibsFolder() {
        return libsFolder;
    }

    public void setLibsFolder(File libsFolder) {
        this.libsFolder = libsFolder;
    }

    public MemoryBank getBank() {
        return bank;
    }

    public void setBank(MemoryBank bank) {
        this.bank = bank;
    }

    public Source getSource() {
        return source;
    }

    public boolean isAllowLowSecurity() {
        return allowLowSecurity;
    }

    public void setAllowLowSecurity(boolean allowLowSecurity) {
        this.allowLowSecurity = allowLowSecurity;
    }
}
