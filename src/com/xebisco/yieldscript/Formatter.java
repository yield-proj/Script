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

import java.util.HashMap;
import java.util.Map;

public class Formatter {
    public static String format(String contents) {
        StringBuilder formatted = new StringBuilder();
        boolean lastIsSpace = false;
        for (Character c : contents.toCharArray()) {
            for (char c1 : Constants.CHARS_TO_BE_REMOVED) {
                if (c == c1) {
                    c = null;
                    break;
                }
            }
            if (c != null) {
                boolean surroundBySpaces = false, removeSpaceBefore = false;
                for (char c1 : Constants.CHARS_TO_GET_SURROUNDED_BY_SPACES) {
                    if (c == c1) {
                        surroundBySpaces = true;
                        break;
                    }
                }
                if (surroundBySpaces) {
                    if (!lastIsSpace) formatted.append(' ');
                    formatted.append(c).append(' ');
                    lastIsSpace = true;
                    c = null;
                } else {
                    for (char c1 : Constants.CHARS_TO_REMOVE_SPACE_BEFORE) {
                        if (c == c1) {
                            removeSpaceBefore = true;
                            break;
                        }
                    }
                    if (removeSpaceBefore) {
                        if (lastIsSpace) formatted.setLength(formatted.length() - 1);
                        lastIsSpace = false;
                    } else {
                        if (c == ' ') {
                            if (lastIsSpace)
                                c = null;
                            lastIsSpace = true;
                        } else {
                            lastIsSpace = false;
                        }
                    }
                }
            }
            if (c != null)
                formatted.append(c);
        }
        return formatted.toString();
    }

    public static Object toNumber(String s) {
        try {
            String sub = s.substring(0, s.length() - 1);
            double d = Double.parseDouble(sub);
            if (s.endsWith("d"))
                return d;
            else if (s.endsWith("f"))
                return Float.parseFloat(sub);
            else if (s.endsWith("l"))
                return Long.parseLong(sub);
            else if (s.endsWith("i"))
                return Integer.parseInt(sub);
            else if (s.endsWith("s"))
                return Short.parseShort(sub);
            else if (s.endsWith("b"))
                return Byte.parseByte(sub);
        } catch (NumberFormatException ignore) {
            return null;
        }
        return null;
    }

    public static Pair<String, Map<Object, Object>> extractStringLiterals(String source) {
        long index = Long.MIN_VALUE;
        Map<Object, Object> literals = new HashMap<>();
        StringBuilder out = new StringBuilder();
        boolean inString = false, lastIsSlash = false, append;
        StringBuilder string = new StringBuilder();
        for (char c : source.toCharArray()) {
            append = true;
            if (c == '\"' || c == '\'') {
                if (!lastIsSlash) {
                    append = false;
                    inString = !inString;
                    if (!inString) {
                        out.append(Constants.OBJECT_ID_CHAR).append(index);
                        literals.put(index, string.toString());
                        string.setLength(0);
                        index++;
                    }
                } else {
                    string.setLength(string.length() - 1);
                }
            }
            if (inString && append) {
                string.append(c);
                append = false;
            }
            lastIsSlash = c == '\\';
            if (append)
                out.append(c);
        }
        return new Pair<>(out.toString(), literals);
    }
}
