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

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MathUtils {

    public static Integer toInt(double d) {
        return (int) d;
    }

    public static Long toLong(double d) {
        return (long) d;
    }

    public static Byte toByte(double d) {
        return (byte) d;
    }

    public static Short toShort(double d) {
        return (short) d;
    }

    public static Float toFloat(double d) {
        return (float) d;
    }

    public static boolean matchesForMath(String txt) {

        if (txt == null || txt.isEmpty()) return false;
        txt = txt.replaceAll("\\s+", "");
        if (!txt.contains("[") && !txt.contains("]")) return Constants.NUMBER_PATTERN.matcher(txt).matches();
        if (txt.contains("[") ^ txt.contains("]")) return false;
        if (txt.contains("[]")) return false;

        Queue<String> toBeRematch = new ArrayDeque<>();
        toBeRematch.add(txt);
        while (toBeRematch.size() > 0) {
            String line = toBeRematch.poll();
            Matcher m = Constants.BRACE_PATTERN.matcher(line);
            if (m.find()) {
                String newline = line.substring(0, m.start()) + "1" + line.substring(m.end());
                String withoutBraces = line.substring(m.start() + 1, m.end() - 1);
                toBeRematch.add(newline);
                if (!Constants.NUMBER_PATTERN.matcher(withoutBraces).matches()) return false;
            }

        }
        return true;
    }

    public static Object resolve(String expression) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < expression.length()) ? expression.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < expression.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)` | number
            //        | functionName `(` expression `)` | functionName factor
            //        | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return +parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('[')) { // parentheses
                    x = parseExpression();
                    if (!eat(']')) throw new RuntimeException("Missing ']'");
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(expression.substring(startPos, this.pos));
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }
}
