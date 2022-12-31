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

import com.xebisco.ys.Constants;
import com.xebisco.ys.calls.Instruction;
import com.xebisco.ys.calls.PossibleEquationFunctionCall;
import com.xebisco.ys.calls.ValueMod;
import com.xebisco.ys.exceptions.NullValueException;
import com.xebisco.ys.exceptions.SyntaxException;
import com.xebisco.ys.memory.MemoryBank;

import java.util.Objects;
import java.util.regex.Matcher;

public class MathUtils {
    public static Number eval(ValueMod valueMod, final String str) {
        final Class<?>[] number = {Integer.class};
        Number result = new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            Double parse() {
                nextChar();
                Double x = parseExpression();
                if (x == null) return null;
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)` | number
            //        | functionName `(` expression `)` | functionName factor
            //        | factor `^` factor

            Double parseExpression() {
                Double x = parseTerm();
                if (x == null) return null;
                for (; ; ) {
                    if (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            Double parseTerm() {
                Double x = parseFactor();
                if (x == null) return null;
                for (; ; ) {
                    if (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation
                    else return x;
                }
            }

            Double parseFactor() {
                if (eat('+')) return +parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                Double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    if (!eat(')')) throw new RuntimeException("Missing ')'");
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.' || ch == 'L' || ch == 'f') nextChar();
                    String s = str.substring(startPos, this.pos);
                    try {
                        x = Integer.valueOf(s).doubleValue();
                    } catch (NumberFormatException ignore) {
                        try {
                            if (!s.endsWith("L")) throw new NumberFormatException();
                            x = Long.valueOf(s.substring(0, s.length() - 1)).doubleValue();
                            if (number[0] == Integer.class) number[0] = Long.class;
                        } catch (NumberFormatException ignore1) {
                            try {
                                if (!s.endsWith("f")) throw new NumberFormatException();
                                x = Float.valueOf(s.substring(0, s.length() - 1)).doubleValue();
                                if (number[0] == Integer.class || number[0] == Long.class) number[0] = Float.class;
                            } catch (NumberFormatException ignore2) {
                                try {
                                    x = Double.parseDouble(s);
                                    number[0] = Double.class;
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                    throw new SyntaxException(s);
                                }
                            }
                        }
                    }
                } else { // functions
                    int callLayer = 1;
                    boolean first = true;
                    boolean firstChar = true;
                    boolean cancel = false;
                    while (!cancel) {
                        nextChar();
                        if (callLayer <= 0) cancel = true;
                        if (callLayer == 1 && (ch == '+' || ch == '-' || (ch == '*' && !firstChar) || ch == '/' || ch == '^' || ch == -1))
                            break;
                        if (ch == '(') {
                            if (callLayer == 2) first = false;
                            callLayer++;
                        } else if (ch == ')') {
                            callLayer--;
                            if (first)
                                callLayer--;
                            first = false;
                        }
                        firstChar = false;
                    }
                    String func = str.substring(startPos, this.pos);
                    Instruction instruction = ((Instruction) Objects.requireNonNull(InterpreterUtils.createCall(func, null)));
                    try {
                        if (instruction instanceof PossibleEquationFunctionCall)
                            ((PossibleEquationFunctionCall) instruction).setIgnoreEquation(true);
                        Number n = ((Number) instruction.call(valueMod));
                        x = n.doubleValue();
                        if (n instanceof Long) {
                            if (number[0] == Integer.class) number[0] = Long.class;
                        } else if (n instanceof Float) {
                            if (number[0] == Integer.class || number[0] == Long.class) number[0] = Float.class;
                        } else if(!(n instanceof Integer))
                            number[0] = Double.class;
                        if (instruction instanceof PossibleEquationFunctionCall)
                            ((PossibleEquationFunctionCall) instruction).setIgnoreEquation(false);
                    } catch (ClassCastException | NullValueException | NullPointerException ignore) {
                        x = null;
                    }
                }
                return x;
            }
        }.parse();
        if (result == null) return null;
        switch (number[0].getSimpleName()) {
            case "Integer":
                result = result.intValue();
                break;
            case "Long":
                result = result.longValue();
                break;
            case "Float":
                result = result.floatValue();
                break;
            case "Double":
                result = result.doubleValue();
                break;
        }
        return result;
    }

    public static boolean bool(MemoryBank memoryBank, final String str) {
        Matcher matcher = Constants.EQUALS_PATTERN.matcher(str);
        if (matcher.matches()) {
            try {
                return memoryBank.getValue(matcher.group(1)).equals(memoryBank.getValue(matcher.group(2)));
            } catch (Exception ignore) {
                return memoryBank.getValue(matcher.group(1)) == memoryBank.getValue(matcher.group(2));
            }
        } else if (matcher.usePattern(Constants.NOT_EQUALS_PATTERN).matches()) {
            return !memoryBank.getValue(matcher.group(1)).equals(memoryBank.getValue(matcher.group(2)));
        }
        return false;
    }
}
