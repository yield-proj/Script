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

package com.xebisco.ys.types;

import java.util.Arrays;
import java.util.Objects;

public class Array {
    private final Object[] objectArray;

    public Array(int length) {
        objectArray = new Object[length];
    }

    public Array(Object[] objectArray) {
        this.objectArray = objectArray;
    }

    public Array(ArrayArgs arrayArgs) {
        objectArray = arrayArgs.getObjectArray();
    }

    public void set(int index, Object o) {
        objectArray[index] = o;
    }

    public Object get(int index) {
        return objectArray[index];
    }

    @Override
    public String toString() {
        return Arrays.toString(objectArray);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Array array = (Array) o;
        return Arrays.equals(objectArray, array.objectArray);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(objectArray);
    }

    public Object[] getObjectArray() {
        return objectArray;
    }
}
