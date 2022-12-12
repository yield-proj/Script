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

package com.xebisco.yieldscript.interpreter.utils;

import com.xebisco.yieldscript.interpreter.Constants;

import java.util.HashMap;
import java.util.Map;

public class ParseUtils {
    public static String removeUnnecessaryWhiteSpace(String source) {
        return source.replace("\n", "").replace("\t", "");
    }

    public static String removeUnnecessarySpaces(String contents) {
        StringBuilder out = new StringBuilder();
        boolean lastIsSpace = false, removeSpaces = false;
        for(Character c : contents.toCharArray()) {
            if(c == ' ') {
                if(lastIsSpace || removeSpaces) c = Constants.TO_REMOVE_CHAR;
                lastIsSpace = true;
            } else {
                removeSpaces = false;
                for(char rsc : Constants.CHARS_TO_REMOVE_SPACES)
                    if(c == rsc) {
                        removeSpaces = true;
                        if(lastIsSpace)
                            out.setLength(out.length() - 1);
                        break;
                    }
                lastIsSpace = false;
            }
            if(c != Constants.TO_REMOVE_CHAR) {
                out.append(c);
            }
        }
        return out.toString();
    }

    public static Pair<String, Map<Long, String>> extractStringLiterals(String source) {
        long index = Long.MIN_VALUE;
        Map<Long, String> literals = new HashMap<>();
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
                        out.append(Constants.STRING_LITERAL_ID_CHAR).append(index);
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

    @SuppressWarnings("RegExpRedundantEscape")
    public static String removeComments(String content)
    {
        return content.replaceAll("(\\/\\*([^\\*]|[\\r\\n]|(\\*+([^*\\/]|[\\r\\n])))*\\*+\\/)|(\\/\\/.*)", "").trim();
    }
}
