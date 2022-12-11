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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MemoryBank {
    private Map<Object, Object> objects;

    public Object getObject(Object o) {
        Object ret;
        if (o instanceof String) {
            if (((String) o).charAt(0) == Constants.OBJECT_ID_CHAR)
                ret = getObjects().get(Long.parseLong(((String) o).substring(1)));
            else {
                ret = Formatter.toNumber((String) o);
                if(ret == null) {
                    String[] pcs = ((String)o).split("\\.");
                    if(pcs.length > 1) {
                        for(int i = 1; i < pcs.length; i++) {
                            String[] mPcs = pcs[i].split(",");
                            Object co = getObject(pcs[0]);
                            Class<?> coClass = co.getClass();
                            try {
                                List<Object> args = new ArrayList<>();
                                String[] n = mPcs[0].split("\\[");
                                mPcs[0] = n[1];
                                for (String mPc : mPcs) {
                                    if (mPc.hashCode() == "]".hashCode()) break;
                                    boolean sub = mPc.endsWith("]");
                                    if (sub)
                                        args.add(getObject(mPc.substring(0, mPc.length() - 1)));
                                    else
                                        args.add(getObject(mPc));
                                }
                                Class<?>[] argsTypes = new Class<?>[args.size()];
                                for(int x = 0; x < argsTypes.length; x++)
                                    argsTypes[x] = Primitive.getJavaPrimitive(args.get(x).getClass());
                                Method method = coClass.getMethod(n[0], argsTypes);
                                ret = method.invoke(co, args.toArray(new Object[0]));
                            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }else ret = getObjects().get(o);
                };
                if(ret == null) throw new NullPointerException((String) o);
            }
        } else ret = o;
        return ret;
    }

    public Map<Object, Object> getObjects() {
        return objects;
    }

    public void setObjects(Map<Object, Object> objects) {
        this.objects = objects;
    }
}
