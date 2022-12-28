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

package com.xebisco.ys.calls;

import java.util.Objects;

public class Argument {
    private final String name;
    private final Class<?> type;
    private final boolean isReference;

    public Argument(String name, Class<?> type, boolean isReference) {
        this.name = name;
        this.type = type;
        this.isReference = isReference;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Argument argument = (Argument) o;
        return isReference == argument.isReference && type.equals(argument.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, isReference);
    }

    public Class<?> getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public boolean isReference() {
        return isReference;
    }
}
