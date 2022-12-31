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

package com.xebisco.ys.calls;

import com.xebisco.ys.exceptions.NullValueException;
import com.xebisco.ys.exceptions.SyntaxException;
import com.xebisco.ys.memory.MemoryBank;
import com.xebisco.ys.types.Struct;

public final class ValueMod extends SecurityManager {
    private final long id;
    private final MemoryBank memoryBank;
    private final boolean allowLowSecurity;

    public ValueMod(long id, MemoryBank memoryBank, boolean allowLowSecurity) {
        this.id = id;
        this.memoryBank = memoryBank;
        this.allowLowSecurity = allowLowSecurity;
    }

    public Object getValue(String var) {
        Object o = null;
        if (var.contains(".")) {
            String[] pcs = var.split("\\.");
            if (pcs.length != 2) throw new SyntaxException(var);
            o = ((Struct) getValue(pcs[0])).getFields().get(pcs[1]);
        } else {
            try {
                o = memoryBank.getValue(var);
            } catch (NullValueException e) {
                try {
                    o = memoryBank.getValue(var + '@' + Long.toHexString(id));
                } catch (NullValueException ignore) {
                    e.printStackTrace();
                }
            }
        }
        return o;
    }

    public Object put(String name, Object value) {
        if (id == 0)
            return memoryBank.put(name, value);
        else
            return memoryBank.put(name + '@' + Long.toHexString(id), value);
    }

    public Object remove(int pointer) {
        return memoryBank.getPointers().remove(pointer);
    }

    @Override
    public void checkPackageAccess(String pkg) {
        if (pkg.equals("java.lang.reflect")) {
            throw new SecurityException();
        }
    }

    MemoryBank getMemoryBank() {
        return memoryBank;
    }

    public long getId() {
        return id;
    }

    public boolean isAllowLowSecurity() {
        return allowLowSecurity;
    }
}
