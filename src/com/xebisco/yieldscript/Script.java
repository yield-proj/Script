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

package com.xebisco.yieldscript;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Script {
    private final String contents;
    private boolean interpreted;
    private Map<String, Function> functions;
    private final MemoryBank memoryBank = new MemoryBank();


    public Script(String contents) {
        this.contents = contents;
    }

    public void interpret() {
        interpreted = true;
        Pair<String, Map<Object, Object>> literals = Formatter.extractStringLiterals(contents);
        memoryBank.setObjects(literals.getSecond());
        for (Primitive primitive : Primitive.values())
            memoryBank.getObjects().put(primitive.name().substring(1), primitive.getType());
        String source = Formatter.format(literals.getFirst());
        IInterpreter interpreter = new Interpreter();
        functions = interpreter.getFunctions(source);
    }

    public void run() {
        if (!interpreted) interpret();
        functions.get("Main").run(this);
    }

    public boolean isInterpreted() {
        return interpreted;
    }

    public void setInterpreted(boolean interpreted) {
        this.interpreted = interpreted;
    }

    public MemoryBank getMemoryBank() {
        return memoryBank;
    }

    public String getContents() {
        return contents;
    }

    public Map<String, Function> getFunctions() {
        return functions;
    }

    public void setFunctions(Map<String, Function> functions) {
        this.functions = functions;
    }
}
