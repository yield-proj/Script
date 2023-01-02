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

import com.xebisco.ys.Constants;
import com.xebisco.ys.exceptions.InvalidPointerException;
import com.xebisco.ys.exceptions.NonExistingVariableException;
import com.xebisco.ys.exceptions.VariableAlreadyExistsException;
import com.xebisco.ys.utils.RunUtils;

import java.util.*;

public class MemoryBank extends LibraryBank {
    private List<String> constants = new ArrayList<>();
    private LinkedList<Object> pointers = new LinkedList<>();
    private Map<String, Object> variables = new HashMap<>();

    public MemoryBank() {
        put("true", true, false);
        put("false", false, false);
        put(Constants.POINTER_CHAR + "nullptr", null, false);
    }

    public Object getValue(String var) {
        Object o;
        boolean isPtr = var.startsWith(String.valueOf(Constants.POINTER_CHAR));
        if (isPtr) var = var.substring(1);
        if (var.startsWith(String.valueOf(Constants.STRING_LITERAL_CHAR)))
            o = RunUtils.STRING_LITERALS.get(Long.parseLong(var.substring(1)));
        else o = variables.get(var);
        if (o == null && !variables.containsKey(var)) {
            throw new NonExistingVariableException(var);
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

    public Object put(String name, Object value, boolean constant) {
        // Checking if the variable is a pointer.
        if (variables.containsKey(name)) throw new VariableAlreadyExistsException(name);
        if (name.startsWith(String.valueOf(Constants.POINTER_CHAR))) {
            pointers.addLast(value);
            variables.put(name.substring(1), pointers.size() - 1);
            if (constant) constants.add(name.substring(1));
            return pointers.size() - 1;
        } else {
            variables.put(name, value);
            if (constant) constants.add(name);
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

    public List<String> getConstants() {
        return constants;
    }

    public void setConstants(List<String> constants) {
        this.constants = constants;
    }
}
