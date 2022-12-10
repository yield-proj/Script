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
import java.lang.reflect.InvocationTargetException;

public class Lang {
    public static Object var(String s) {
        return s;
    }
    public static Object call(String method, Object obj, Object[] args) {
        Class<?>[] argsTypes = new Class[args.length];
        for(int i= 0 ; i < argsTypes.length; i++)
            argsTypes[i] = args[i].getClass();
        try {
            return obj.getClass().getMethod(method, argsTypes).invoke(obj, args);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
    public static void set(Object obj, int index, Object[] array) {
        array[index] = obj;
    }
    @SuppressWarnings("unchecked")
    public static <T> T[] array(Class<T> type, int length) {
        return (T[]) Array.newInstance(type, length);
    }
    public static int[] intArray(int length) {
        return new int[length];
    }
    public static int[] floatArray(int length) {
        return new int[length];
    }
    public static double[] doubleArray(int length) {
        return new double[length];
    }
    public static boolean[] boolArray(int length) {
        return new boolean[length];
    }
    public static void println(Object obj) {
        System.out.println(obj);
    }
}
