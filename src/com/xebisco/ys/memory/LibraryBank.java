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
import com.xebisco.ys.exceptions.FunctionNotFoundException;
import com.xebisco.ys.exceptions.MissingLibraryException;
import com.xebisco.ys.program.Library;
import com.xebisco.ys.types.ArrayArgs;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class LibraryBank {
    private final List<Library> libraries = new ArrayList<>();

    public Library getLibrary(String libName) {
        for (Library library : libraries) {
            if (library.getName().hashCode() == libName.hashCode() && library.getName().equals(libName)) return library;
        }
        throw new MissingLibraryException(libName);
    }

    public boolean containsLibrary(String libName) {
        for (Library library : libraries) {
            if (library.getName().hashCode() == libName.hashCode() && library.getName().equals(libName)) return true;
        }
        return false;
    }

    public Function getFunction(String functionName, List<Class<?>> types) {
        Function function = getFunctionNullable(functionName, types);
        if(function != null)
            return function;
        throw new FunctionNotFoundException(functionName + types);
    }

    private Function getFunctionNullable(String functionName, List<Class<?>> types) {
        Matcher matcher = Constants.LIBRARY_FUNCTION_PATTERN.matcher(functionName);
        if (matcher.matches()) {
            return findByArrayArgs(getLibrary(matcher.group(1)), new Pair<>(matcher.group(2), types), types);
        } else {
            Pair<String, List<Class<?>>> pair = new Pair<>(functionName, types);
            for (Library library : libraries) {
                Function f = findByArrayArgs(library, pair, types);
                if(f != null)
                    return f;
            }
        }
        return null;
    }

    private Function findByArrayArgs(Library library, Pair<String, List<Class<?>>> pair, List<Class<?>> types) {
        if (library.getFunctions().containsKey(pair)) {
            return library.getFunctions().get(pair);
        } else {
            List<Class<?>> cTypes = new ArrayList<>();
            cTypes.add(ArrayArgs.class);
            Function function = library.getFunctions().get(new Pair<>(pair.getFirst(), cTypes));
            if (function == null) {
                for (Class<?> type : types) {
                    cTypes.add(cTypes.size() - 1, type);
                    function = library.getFunctions().get(new Pair<>(pair.getFirst(), cTypes));
                    if (function != null) break;
                }
            }
            return function;
        }
    }

    public Function getFunction(String functionName, Class<?>[] types) {
        return getFunction(functionName, List.of(types));
    }

    public List<Library> getLibraries() {
        return libraries;
    }
}
