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
import com.xebisco.ys.program.Source;

import java.util.Map;

public class SourceUtils {
    public static Source fromRaw(String contents) {
        String[] source = ParseUtils.parseChars(ParseUtils.removeUnnecessaryWhiteSpace(ParseUtils.extractStringLiterals(ParseUtils.removeComments(contents)))).split(String.valueOf(Constants.SOURCE_BREAK));
        for (int i = 0; i < source.length; i++)
            source[i] = source[i].trim();
        return new Source(source);
    }

    public static <F, S> Pair<F, S>[] toPairArray(Map<F, S> map) {
        @SuppressWarnings("unchecked") Pair<F, S>[] pairs = new Pair[map.size()];
        int index = 0;
        for(F key : map.keySet()) {
            pairs[index] = new Pair<>(key, map.get(key));
            index++;
        }
        return pairs;
    }

    public static <F, S> void putAll(Map<F, S> map, Pair<F, S>[] pairs) {
        for(Pair<F, S> pair : pairs) {
            map.put(pair.getFirst(), pair.getSecond());
        }
    }
}
