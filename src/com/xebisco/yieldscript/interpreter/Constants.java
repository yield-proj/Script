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
            DECLARATION_PATTERN = Pattern.compile("^(\\w+):(" + PatternUtils.pattern(Type.class) + ")=(\2-\\w+)\\[([^}]*)\\]"),
            DECLARATION_PATTERN_AUTO_TYPE = Pattern.compile("^(\\w+)=(\2-\\w+)\\[([^}]*)\\]"),
            DECLARATION_PATTERN_AUTO_TYPE_NO_MODS = Pattern.compile("^(\\w+)=(\2-\\w+)"),
            DECLARATION_PATTERN_NO_MODS = Pattern.compile("^(\\w+):(" + PatternUtils.pattern(Type.class) + ")=(\2-\\w+)"),
            DECLARATION_PATTERN_DEFAULT_VALUE = Pattern.compile("^(\\w+):(" + PatternUtils.pattern(Type.class) + ")\\[([^}]*)\\]"),
            DECLARATION_PATTERN_DEFAULT_VALUE_NO_MODS = Pattern.compile("^(\\w+):(" + PatternUtils.pattern(Type.class) + ")"),
            FUNCTION_PATTERN = Pattern.compile("^(\\w+)\\(([^}]*)\\):(" + PatternUtils.pattern(Type.class) + ")\\{"),
            CLOSE_CURLY_BRACES_PATTERN = Pattern.compile("\\}"),
            CLASS_METHOD_CALL_PATTERN = Pattern.compile("^\\(([^}]*)\\)(\\w+)\\(([^}]*)\\)"),
            CLASS_METHODS_CALL_PATTERN = Pattern.compile("^\\(([^}]*)\\)(\\w.+)\\(([^}]*)\\)"),
            CLASS_FIELD_PATTERN = Pattern.compile("^\\(([^}]*)\\)(\\w+)"),
            CLASS_FIELDS_PATTERN = Pattern.compile("^\\(([^}]*)\\)(\\w.+)"),
            METHOD_CALL_PATTERN = Pattern.compile("^(\\w+)\\(([^}]*)\\)"),
            METHODS_CALL_PATTERN = Pattern.compile("^(\\w.+)\\(([^}]*)\\)"),
            FIELDS_CALL_PATTERN = Pattern.compile("^(\\w.+)\\(([^}]*)\\)"),
            EQUALS_PATTERN = Pattern.compile("^(\\w+)=(\\w+)");
    public final static char STRING_LITERAL_ID_CHAR = '\2', TO_REMOVE_CHAR = '\3', SOURCE_BREAK = ';';
    public final static char[] CHARS_TO_REMOVE_SPACES = new char[]{'=', ',', '{', '}', '.', ':', '(', ')', '[', ']'}, CHARS_TO_INSERT_SOURCE_BREAK = new char[]{'{', '}'};
}
