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

package com.xebisco.ys;

import java.util.regex.Pattern;

public class Constants {

    public final static Pattern FUNCTION_CALL_PATTERN = Pattern.compile("^([^(.]+)\\(([^}]*)\\)"),
            LIBRARY_FUNCTION_PATTERN = Pattern.compile("([^()_]+)_([^(.]+)"),
            FUNCTION_DECLARATION_PATTERN = Pattern.compile("^([^(]+) (\\w+)\\(([^}]*)\\)\\{"),
            STRUCT_DECLARATION_PATTERN = Pattern.compile("^Struct (\\w+)\\(([^}]*)\\)"),
            VARIABLE_DECLARATION_PATTERN = Pattern.compile("^var (\\S+)=([^}]*)"),
            POINTER_DECLARATION_PATTERN = Pattern.compile("^var(\\*\\S+)=([^}]*)"),
            CONSTANT_VARIABLE_DECLARATION_PATTERN = Pattern.compile("^let (\\S+)=([^}]*)"),
            CONSTANT_POINTER_DECLARATION_PATTERN = Pattern.compile("^let(\\*\\S+)=([^}]*)"),
            CAST_PATTERN = Pattern.compile("^\\(([^()]+)\\)"),
            SET_PATTERN = Pattern.compile("^([^ *=]+)=([^}]+)"),
            SET_POINTER_PATTERN = Pattern.compile("^(\\*[^ *=]+)=([^}]+)"),
            CLOSE_CURLY_BRACES_PATTERN = Pattern.compile("}"),
            ACTION_FUNCTION_PATTERN = Pattern.compile("^(\\S+)\\(([^}]*)\\)\\{"),
            IMPORT_PACKAGE_PATTERN = Pattern.compile("^import package (.*\\w+)"),
            NEW_PATTERN = Pattern.compile("new (\2-*[0-9]+)\\(([^}]*)\\)"),
            ARGUMENT_PATTERN = Pattern.compile("^(.*\\S+) (\\S+)"),
            REFERENCE_ARGUMENT_PATTERN = Pattern.compile("^(.*\\S*)&(\\S+)"),
            IN_ARGUMENT_PATTERN = Pattern.compile("^in (.*\\S+) (\\S+)"),
            IN_REFERENCE_ARGUMENT_PATTERN = Pattern.compile("^in (.*\\S*)&(\\S+)"),
            RETURN_PATTERN = Pattern.compile("^return\\(([^}]*)\\)"),
            EQUALS_PATTERN = Pattern.compile("^(\\S+)==(\\S+)"),
            NOT_EQUALS_PATTERN = Pattern.compile("^(\\S+)!=(\\S+)"),
            GREATER_THAN_PATTERN = Pattern.compile("^(\\S+)>(\\S+)"),
            GREATER_OR_EQUAL_THAN_PATTERN = Pattern.compile("^(\\S+)>=(\\S+)"),
            LESS_THAN_PATTERN = Pattern.compile("^(\\S+)<(\\S+)"),
            LESS_OR_EQUAL_THAN_PATTERN = Pattern.compile("^(\\S+)<=(\\S+)");

    public static final String BOOL_OR_STRING = "or", BOOL_AND_STRING = "and";

    public final static char POINTER_CHAR = '*', STRING_LITERAL_CHAR = '\2', TO_REMOVE_CHAR = '\3', BOOL_INVERT_CHAR = '!', SOURCE_BREAK = ';', FUNCTION_ARGUMENT_ID_CHAR = '\3', NEGATE_CHAR = '!';
    public final static char[] CHARS_TO_REMOVE_SPACES = new char[]{'=', ',', '{', '}', '.', ':', '(', ')', '-', '>', '<', '+', '/', '%', '*', '!', '&'}, CHARS_TO_INSERT_SOURCE_BREAK = new char[]{'{', '}'};
}