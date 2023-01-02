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
import com.xebisco.ys.calls.FunctionCall;
import com.xebisco.ys.calls.Instruction;
import com.xebisco.ys.calls.PossibleEquationFunctionCall;
import com.xebisco.ys.calls.ValueMod;
import com.xebisco.ys.exceptions.NullValueException;
import com.xebisco.ys.exceptions.SyntaxException;
import com.xebisco.ys.memory.MemoryBank;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.ArrayList;
import java.util.List;
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
                if (pos < str.length()) throw new SyntaxException("Unexpected: " + (char) ch);
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
                    if (!eat(')')) throw new SyntaxException("Missing ')'");
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
                        } else if (!(n instanceof Integer))
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

    public static boolean bool(ValueMod valueMod, final String str) {
        boolean out = false;
        String[] verifications = str.split(" ");
        boolean keyWord = false, lastIsOr = true;
        for (int i = 0; i < verifications.length; i++) {
            String verification = verifications[i];
            if (verification.hashCode() != "".hashCode()) {
                if (keyWord) {
                    if (verification.hashCode() == Constants.BOOL_OR_STRING.hashCode() && verification.equals(Constants.BOOL_OR_STRING))
                        lastIsOr = true;
                    else if (verification.hashCode() == Constants.BOOL_AND_STRING.hashCode() && verification.equals(Constants.BOOL_AND_STRING))
                        lastIsOr = false;
                    else throw new SyntaxException(str + ". on: " + verification);
                } else {
                    boolean bool;
                    if (verification.startsWith("[")) {
                        StringBuilder b = new StringBuilder();
                        int layer = 0, toRemoveSize = 1;
                        StringBuilder remaining = new StringBuilder(verification.substring(1));
                        remaining.append(" ");
                        for (int i1 = i + 1; i1 < verifications.length; i1++)
                            remaining.append(verifications[i1]).append(" ");
                        remaining.setLength(remaining.length() - 1);
                        for (char c : remaining.toString().toCharArray()) {
                            if (c == '[') layer++;
                            if (c == ']') layer--;
                            toRemoveSize++;
                            if (layer < 0) break;
                            b.append(c);
                        }
                        int cSize = 0;
                        for (int i1 = i; i1 < verifications.length; i1++) {
                            String v = verifications[i1];
                            cSize += v.length();
                            if (cSize < toRemoveSize)
                                verifications[i1] = "";
                            else break;
                        }
                        verification = b.toString();
                        bool = bool(valueMod, verification);
                    } else
                        bool = oneBool(valueMod, verification);
                    if (lastIsOr) {
                        if (!out && bool) out = true;
                    } else {
                        if (out) out = bool;
                    }
                }
                keyWord = !keyWord;
            }
        }
        return out;
    }

    private static boolean oneBool(ValueMod valueMod, final String str) {
        Matcher matcher = Constants.EQUALS_PATTERN.matcher(str);
        if (matcher.matches()) {
            try {
                return FunctionUtils.createFunctionCall(matcher.group(1), FunctionCall.class, null).call(valueMod).equals(FunctionUtils.createFunctionCall(matcher.group(2), FunctionCall.class, null).call(valueMod));
            } catch (Exception ignore) {
                return FunctionUtils.createFunctionCall(matcher.group(1), FunctionCall.class, null).call(valueMod) == FunctionUtils.createFunctionCall(matcher.group(2), FunctionCall.class, null).call(valueMod);
            }
        } else if (matcher.usePattern(Constants.NOT_EQUALS_PATTERN).matches()) {
            try {
                return (!FunctionUtils.createFunctionCall(matcher.group(1), FunctionCall.class, null).call(valueMod).equals(FunctionUtils.createFunctionCall(matcher.group(2), FunctionCall.class, null).call(valueMod)));
            } catch (Exception ignore) {
                return FunctionUtils.createFunctionCall(matcher.group(1), FunctionCall.class, null).call(valueMod) != FunctionUtils.createFunctionCall(matcher.group(2), FunctionCall.class, null).call(valueMod);
            }
        } else if (matcher.usePattern(Constants.GREATER_OR_EQUAL_THAN_PATTERN).matches()) {
            return toDouble((Number) FunctionUtils.createFunctionCall(matcher.group(1), FunctionCall.class, null).call(valueMod)) >= toDouble((Number) FunctionUtils.createFunctionCall(matcher.group(2), FunctionCall.class, null).call(valueMod));
        } else if (matcher.usePattern(Constants.LESS_OR_EQUAL_THAN_PATTERN).matches()) {
            return toDouble((Number) FunctionUtils.createFunctionCall(matcher.group(1), FunctionCall.class, null).call(valueMod)) <= toDouble((Number) FunctionUtils.createFunctionCall(matcher.group(2), FunctionCall.class, null).call(valueMod));
        } else if (matcher.usePattern(Constants.GREATER_THAN_PATTERN).matches()) {
            return toDouble((Number) FunctionUtils.createFunctionCall(matcher.group(1), FunctionCall.class, null).call(valueMod)) > toDouble((Number) FunctionUtils.createFunctionCall(matcher.group(2), FunctionCall.class, null).call(valueMod));
        } else if (matcher.usePattern(Constants.LESS_THAN_PATTERN).matches()) {
            return toDouble((Number) FunctionUtils.createFunctionCall(matcher.group(1), FunctionCall.class, null).call(valueMod)) < toDouble((Number) FunctionUtils.createFunctionCall(matcher.group(2), FunctionCall.class, null).call(valueMod));
        } else {
            try {
                return (Boolean) valueMod.getValue(str);
            } catch (NullValueException ignore) {
            }
        }
        throw new SyntaxException(str);
    }

    public static double toDouble(Number number) {
        return number.doubleValue();
    }
}
