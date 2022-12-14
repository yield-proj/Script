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

package com.xebisco.ys.utils;

import com.xebisco.yieldutils.Pair;
import com.xebisco.ys.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;

public class ParseUtils {
    public static String removeUnnecessaryWhiteSpace(String source) {
        return source.replace("\n", "").replace("\t", "");
    }

    public static String[] toStringArray(Object[] source) {
        String[] array = new String[source.length];
        for (int i = 0; i < array.length; i++)
            array[i] = source[i].toString();
        return array;
    }

    public static String removeEndSpaces(String source) {
        String out = source;
        while (out.endsWith(" ")) out = out.substring(0, out.length() - 1);
        return out;
    }

    public static String parseChars(String contents) {
        StringBuilder out = new StringBuilder();
        boolean lastIsSpace = false, removeSpaces = false, addBreak;
        for (Character c : contents.toCharArray()) {
            addBreak = false;
            if (removeSpaces) lastIsSpace = false;
            if (c == ' ') {
                if (lastIsSpace || removeSpaces) c = Constants.TO_REMOVE_CHAR;
                lastIsSpace = true;
            } else {
                removeSpaces = false;
                for (char rsc : Constants.CHARS_TO_REMOVE_SPACES)
                    if (c == rsc) {
                        removeSpaces = true;
                        if (lastIsSpace)
                            out.setLength(out.length() - 1);
                        break;
                    }
                for (char rsc : Constants.CHARS_TO_INSERT_SOURCE_BREAK)
                    if (c == rsc) {
                        addBreak = true;
                        break;
                    }
                lastIsSpace = false;
            }
            if (c != Constants.TO_REMOVE_CHAR) out.append(c);
            if (addBreak) out.append(Constants.SOURCE_BREAK);
        }
        return out.toString();
    }

    public static String[] toLastCurlyBrace(String[] source) {
        int layer = 1;
        List<String> out = new ArrayList<>();
        for (String line : source) {
            out.add(line);
            if (line.endsWith("}")) {
                String s = out.get(out.size() - 1);
                out.set(out.size() - 1, s.substring(0, s.length() - 1));
                if (out.get(out.size() - 1).hashCode() == "".hashCode()) out.remove(out.size() - 1);
                break;
            }
        }
        return out.toArray(new String[0]);
    }

    public static String[] splitPattern(String s) {
        List<String> args = new ArrayList<>();
        int parenthesisLayer = 0;
        String act = "";
        for(char c : s.toCharArray()) {
            if(c == '(') parenthesisLayer++;
            else if(c == ')') parenthesisLayer--;
            if(c == ',' && parenthesisLayer == 0) {
                args.add(act);
                act = "";
            }
            else act += c;
        }
        if(act.hashCode() != "".hashCode()) {
            args.add(act);
        }
        return args.toArray(new String[0]);
    }

    public static String extractStringLiterals(String source) {
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
                        out.append(Constants.STRING_LITERAL_CHAR).append(RunUtils.stringLiteralIndex);
                        RunUtils.STRING_LITERALS.put(RunUtils.stringLiteralIndex, string.toString().replace("\\\\", "\4").replace("\\n", "\n").replace("\\t", "\t").replace("\4", "\\"));
                        string.setLength(0);
                        RunUtils.stringLiteralIndex++;
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
        return out.toString();
    }

    @SuppressWarnings("RegExpRedundantEscape")
    public static String removeComments(String content) {
        return content.replaceAll("(\\/\\*([^\\*]|[\\r\\n]|(\\*+([^*\\/]|[\\r\\n])))*\\*+\\/)|(\\/\\/.*)", "").trim();
    }
}