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

import com.xebisco.yieldscript.interpreter.type.Type;
import com.xebisco.yieldscript.interpreter.utils.PatternUtils;

import java.util.regex.Pattern;

public class Constants {
    @SuppressWarnings("RegExpRedundantEscape")
    public final static Pattern
            DECLARATION_PATTERN = Pattern.compile("^(\\w+):(" + PatternUtils.pattern(Type.class) + ")=([^}]*)\\[([^}]*)\\]"),
            DECLARATION_PATTERN_AUTO_TYPE = Pattern.compile("^auto (\\w+)=([^}]*)\\[([^}]*)\\]"),
            DECLARATION_PATTERN_AUTO_TYPE_NO_MODS = Pattern.compile("^auto (\\w+)=([^}]*)"),
            DECLARATION_PATTERN_NO_MODS = Pattern.compile("^(\\w+):(" + PatternUtils.pattern(Type.class) + ")=([^}]*)"),
            DECLARATION_PATTERN_DEFAULT_VALUE = Pattern.compile("^(\\w+):(" + PatternUtils.pattern(Type.class) + ")\\[([^}]*)\\]"),
            DECLARATION_PATTERN_DEFAULT_VALUE_NO_MODS = Pattern.compile("^(\\w+):(" + PatternUtils.pattern(Type.class) + ")"),
            FUNCTION_PATTERN = Pattern.compile("^(\\w+)\\(([^}]*)\\):(" + PatternUtils.pattern(Type.class) + ")\\{"),
            FUNCTION_WITH_MODIFIERS_PATTERN = Pattern.compile("^(\\w+)\\(([^}]*)\\):(" + PatternUtils.pattern(Type.class) + ")\\[([^}]*)\\]\\{"),
            DATA_TYPE_PATTERN = Pattern.compile("^data (\\w+)\\(([^}]*)\\)"),
            DATA_TYPE_WITH_MODIFIERS_PATTERN = Pattern.compile("^data (\\w+)\\(([^}]*)\\)\\[([^}]*)\\]"),
            CLOSE_CURLY_BRACES_PATTERN = Pattern.compile("\\}"),
            CLASS_METHOD_CALL_PATTERN = Pattern.compile("^\\(([^}]*)\\)(\\w+)\\(([^}]*)\\)"),
            CLASS_METHODS_CALL_PATTERN = Pattern.compile("^\\(([^}]*)\\)(\\w.+)\\(([^}]*)\\)"),
            CLASS_FIELD_PATTERN = Pattern.compile("^\\(([^}]*)\\)(\\w+)"),
            CLASS_FIELDS_PATTERN = Pattern.compile("^\\(([^}]*)\\)(\\w.+)"),
            METHOD_CALL_PATTERN = Pattern.compile("^([^\\(.]+)\\(([^}]*)\\)"),
            METHODS_CALL_PATTERN = Pattern.compile("^([^\\(]+)\\(([^}]*)\\)"),
            FIELDS_CALL_PATTERN = Pattern.compile("^(\\w.+)\\(([^}]*)\\)"),
            SET_AS_PATTERN = Pattern.compile("^(\\w+)=( *\\S*)"),
            ATTACH_PATTERN = Pattern.compile("^attach (\\S+)+"),
            RETURN_PATTERN = Pattern.compile("^return([^}]*)"),
            CAST_PATTERN = Pattern.compile("[^}]+as (" + PatternUtils.pattern(Type.class) + ")"),
            INT_FOR_EACH_PATTERN = Pattern.compile("^for\\((\\w+):(\\S+)+\\-\\>(\\S+)+\\)\\{"),
            SUBTRACT_INT_FOR_EACH_PATTERN = Pattern.compile("^for\\((\\w+):(\\S+)+\\<\\-(\\S+)+\\)\\{"),
            NUMBER_PATTERN = Pattern.compile("[+\\-]?(([0-9]+\\.[0-9]+)|([0-9]+\\.?)|(\\.?[0-9]+))([+\\^\\-/*%](([0-9]+\\.[0-9]+)|([0-9]+\\.?)|(\\.?[0-9]+)))*"),
            BRACE_PATTERN = Pattern.compile("\\[[^\\[\\]]+\\]");

    public final static char STRING_LITERAL_ID_CHAR = '\2', TO_REMOVE_CHAR = '\3', SOURCE_BREAK = ';', FUNCTION_ARGUMENT_ID_CHAR = '\3';
    public final static char[] CHARS_TO_REMOVE_SPACES = new char[]{'=', ',', '{', '}', '.', ':', '(', ')', '[', ']', '-', '>', '<', '+', '/', '%', '*'}, CHARS_TO_INSERT_SOURCE_BREAK = new char[]{'{', '}'};
}
