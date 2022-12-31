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
import com.xebisco.ys.calls.*;
import com.xebisco.ys.exceptions.SyntaxException;

import java.util.List;
import java.util.regex.Matcher;

public class InterpreterUtils {
    public static Call createCall(String line, List<Call> functionsLayer) {
        Call call = null;
        Matcher matcher = Constants.CAST_PATTERN.matcher(line);
        Class<?> cast = null;
        while (matcher.find()) {
            cast = RunUtils.forName(matcher.group(1));
            line = line.substring(line.indexOf(")") + 1);
            matcher = Constants.CAST_PATTERN.matcher(line);
        }

        while (matcher.usePattern(Constants.NEW_PATTERN).find()) {
            line = line.replaceAll(Constants.NEW_PATTERN.pattern(), "new(class(" + matcher.group(1) + ")," + matcher.group(2) + ")");
        }

        while (matcher.usePattern(Constants.SET_POINTER_PATTERN).find()) {
            line = line.replaceAll(Constants.SET_POINTER_PATTERN.pattern(), "setPointer(" + matcher.group(1).substring(1) + ",(Object)" + matcher.group(2) + ")");
        }

        boolean outOfGlobal = false;

        //Functions
        if (matcher.usePattern(Constants.FUNCTION_DECLARATION_PATTERN).matches()) {
            String[] args = matcher.group(3).split(",");
            if(args.length == 1 && args[0].hashCode() == "".hashCode()) args = new String[0];
            Argument[] arguments = new Argument[args.length];
            for (int i = 0; i < arguments.length; i++) {
                Matcher m = Constants.REFERENCE_ARGUMENT_PATTERN.matcher(args[i]);
                if (m.matches())
                    arguments[i] = new Argument(m.group(2), RunUtils.forName(m.group(1)), true, false);
                else if (m.usePattern(Constants.ARGUMENT_PATTERN).matches())
                    arguments[i] = new Argument(m.group(2), RunUtils.forName(m.group(1)), false, false);
                else throw new SyntaxException(line + ". on: " + args[i]);
            }
            Function function = new Function(arguments);
            function.setReturnCast(RunUtils.forName(matcher.group(1)));
            function.setTName(matcher.group(2));
            functionsLayer.add(function);
            call = function;
            outOfGlobal = true;
        } else if (matcher.usePattern(Constants.STRUCT_DECLARATION_PATTERN).matches()) {
            String[] args = matcher.group(2).split(",");
            if(args.length == 1 && args[0].hashCode() == "".hashCode()) args = new String[0];
            Argument[] arguments = new Argument[args.length];
            for (int i = 0; i < arguments.length; i++) {
                Matcher m = Constants.IN_REFERENCE_ARGUMENT_PATTERN.matcher(args[i]);
                if (m.matches())
                    arguments[i] = new Argument(m.group(2), RunUtils.forName(m.group(1)), true, true);
                else if (m.usePattern(Constants.IN_ARGUMENT_PATTERN).matches())
                    arguments[i] = new Argument(m.group(2), RunUtils.forName(m.group(1)), false, true);
                else if (m.usePattern(Constants.REFERENCE_ARGUMENT_PATTERN).matches())
                    arguments[i] = new Argument(m.group(2), RunUtils.forName(m.group(1)), true, false);
                else if (m.usePattern(Constants.ARGUMENT_PATTERN).matches())
                    arguments[i] = new Argument(m.group(2), RunUtils.forName(m.group(1)), false, false);
                else throw new SyntaxException(line + ". on: " + args[i]);
            }
            call = StructUtils.createStruct(matcher.group(1), arguments);
        } else if (matcher.usePattern(Constants.ACTION_FUNCTION_PATTERN).matches()) {
            call = FunctionUtils.createFunctionCall(line.substring(0, line.length() - 1), ActionFunctionCall.class, cast);
            functionsLayer.add(call);
            outOfGlobal = true;
        } else if (matcher.usePattern(Constants.CLOSE_CURLY_BRACES_PATTERN).matches()) {
            functionsLayer.remove(functionsLayer.size() - 1);
        } else if (matcher.usePattern(Constants.IMPORT_PACKAGE_PATTERN).matches()) {
            RunUtils.PACKAGES.add(matcher.group(1));
        }

        //Other
        else if (matcher.usePattern(Constants.SET_PATTERN).matches()) {
            call = new SetVariable(matcher.group(1), (Instruction) createCall(matcher.group(2), null));
        }else if (matcher.usePattern(Constants.VARIABLE_DECLARATION_PATTERN).matches() || matcher.usePattern(Constants.POINTER_DECLARATION_PATTERN).matches()) {
            call = new VariableDeclaration(matcher.group(1), (FunctionCall) createCall(matcher.group(2), null));
        } else if (matcher.usePattern(Constants.RETURN_PATTERN).matches()) {
            if (functionsLayer.size() > 0)
            {
                Call f = functionsLayer.get(functionsLayer.size() - 1);
                if(!(f instanceof Function))
                    throw new SyntaxException("return() cannot be called outside a function declaration or outside the global scope. '" + line + "'");
                cast = ((Function) f).getReturnCast();
            }
            call = FunctionUtils.createFunctionCall(matcher.group(1), FunctionCall.class, cast);
            ((Instruction) call).setReturnExecution(true);
        } else {
            call = FunctionUtils.createFunctionCall(line, FunctionCall.class, cast);
        }

        if (functionsLayer != null && functionsLayer.size() > 0 && call != null && !outOfGlobal) {
            Call function = functionsLayer.get(functionsLayer.size() - 1);
            assert call instanceof Instruction;
            if (function instanceof ActionFunctionCall) {
                ((ActionFunctionCall) function).getInstructions().add((Instruction) call);
            } else {
                ((Function) function).getInstructions().add((Instruction) call);
            }
            return null;
        } else
            return call;
    }
}
