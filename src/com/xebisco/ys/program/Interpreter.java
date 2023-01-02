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

import com.xebisco.ys.calls.Call;
import com.xebisco.ys.utils.InterpreterUtils;

import java.util.ArrayList;
import java.util.List;

public class Interpreter implements IInterpreter {

    private final List<Call> functionsLayer = new ArrayList<>();

    @Override
    public Call createInstruction(String line) {
        return InterpreterUtils.createCall(line, null, functionsLayer);
    }
}
