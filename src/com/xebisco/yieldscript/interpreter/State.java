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

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

class StateManager {

    public Map<State, MonoBehavior> stateFunctions = new HashMap<>();
    public State actualState = State.MOVE;

    public void start() {
        stateFunctions.get(actualState).start();
    }

    public void update() {

    }

    public void MOVE() {

    }
}

class MonoBehavior {
    public void start() {

    }

    public void update() {

    }
}

enum State {
    MOVE, FREEZE
}

class Test {
    StateManager manager = new StateManager();
    Test() {

    }
}
