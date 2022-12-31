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

package com.xebisco.ys.memory;

import com.xebisco.yieldutils.Pair;
import com.xebisco.ys.Constants;
import com.xebisco.ys.calls.Function;
import com.xebisco.ys.exceptions.InvalidPointerException;
import com.xebisco.ys.exceptions.NullValueException;
import com.xebisco.ys.exceptions.VariableAlreadyExistsException;
import com.xebisco.ys.utils.RunUtils;

import java.util.*;

public class MemoryBank {
    private LinkedList<Object> pointers = new LinkedList<>();
    private Map<String, Object> variables = new HashMap<>();
    private Map<Pair<String, List<Class<?>>>, Function> functions = new HashMap<>();

    public Object getValue(String var) {
        Object o;
        boolean isPtr = var.startsWith(String.valueOf(Constants.POINTER_CHAR));
        if (isPtr) var = var.substring(1);
        if (var.startsWith(String.valueOf(Constants.STRING_LITERAL_CHAR)))
            o = RunUtils.STRING_LITERALS.get(Long.parseLong(var.substring(1)));
        else o = variables.get(var);
        if (o == null) {
            throw new NullValueException(var);
        }
        if (isPtr) {
            try {
                o = pointers.get((Integer) o);
            } catch (ClassCastException ignore) {
                throw new InvalidPointerException(var);
            }
        }
        return o;
    }

    public Object put(String name, Object value) {
        // Checking if the variable is a pointer.
        if(variables.containsKey(name)) throw new VariableAlreadyExistsException(name);
        if (name.startsWith(String.valueOf(Constants.POINTER_CHAR))) {
            pointers.addLast(value);
            variables.put(name.substring(1), pointers.size() - 1);
            return pointers.size() - 1;
        } else {
            variables.put(name, value);
            return value;
        }
    }

    public LinkedList<Object> getPointers() {
        return pointers;
    }

    public void setPointers(LinkedList<Object> pointers) {
        this.pointers = pointers;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }

    public Map<Pair<String, List<Class<?>>>, Function> getFunctions() {
        return functions;
    }

    public void setFunctions(Map<Pair<String, List<Class<?>>>, Function> functions) {
        this.functions = functions;
    }
}
