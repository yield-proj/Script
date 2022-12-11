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
import java.util.ArrayList;

public class Lang {
    public static Object instance(Class<?> type, Object[] args) {
        try {
            return type.getConstructor(getTypes(args)).newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
    public static String var(String s) {
        return s;
    }
    public static int var(int o) {
        return o;
    }
    public static long var(long o) {
        return o;
    }
    public static float var(float o) {
        return o;
    }
    public static double var(double o) {
        return o;
    }
    public static char var(char o) {
        return o;
    }
    public static byte var(byte o) {
        return o;
    }
    public static short var(short o) {
        return o;
    }
    public static Object call(String method, Object obj, Object[] args) {
        try {
            return obj.getClass().getMethod(method, getTypes(args)).invoke(obj, args);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
    public static Class<?>[] getTypes(Object[] args) {
        Class<?>[] argsTypes = new Class[args.length];
        for(int i= 0 ; i < argsTypes.length; i++)
            argsTypes[i] = args[i].getClass();
        return argsTypes;
    }
    public static void set(Object obj, int index, Object[] array) {
        array[index] = obj;
    }
    public static <T> ArrayList<T> newArrayList(Class<T> clazz) {
        return new ArrayList<>();
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
