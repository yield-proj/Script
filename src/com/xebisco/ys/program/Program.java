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
import com.xebisco.ys.calls.*;
import com.xebisco.ys.memory.MemoryBank;
import com.xebisco.ys.utils.SourceUtils;

import java.util.ArrayList;
import java.util.List;

public class Program {
    private MemoryBank bank = new MemoryBank();
    private final Source source;
    private List<Instruction> instructions;

    public Program(Source source) {
        this.source = source;
        SourceUtils.putAll(this.bank.getPointers(), source.getStringLiterals());
    }

    public void addLibs() {
        bank.getFunctions().put(new Pair<>("print", List.of(new Class<?>[]{String.class})), new Function(new Instruction[]{
                new Instruction() {
                    @Override
                    public Object call(MemoryBank memoryBank) {
                        System.out.println(memoryBank.getValue("x"));
                        return null;
                    }
                }
        }, new Argument[]{
                new Argument("x", String.class, false)
        }));
    }

    public void interpret(IInterpreter interpreter) {
        instructions = new ArrayList<>();
        for (String line : source.getContents()) {
            Call call = interpreter.createInstruction(line);
            if (call instanceof Instruction)
                instructions.add((Instruction) call);
            //else bank.getFunctions().put(new Pair<>())
        }
    }

    public void run() {
        for(Instruction instruction : instructions) {
            instruction.call(bank);
        }
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
}
