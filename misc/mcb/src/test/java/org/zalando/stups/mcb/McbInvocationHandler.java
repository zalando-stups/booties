/**
 * Copyright (C) 2015 Zalando SE (http://tech.zalando.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.zalando.stups.mcb;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

class McbInvocationHandler implements InvocationHandler {

    public static final Object NOT_SET = UUID.randomUUID();

    private final MCB mcb;
    private final Object fallback;
    private final Object target;

    public McbInvocationHandler(MCB mcb, Object target, Object fallback) {
        this.mcb = mcb;
        this.fallback = fallback;
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (mcb.isClosed()) {
            try {
                Object result = method.invoke(target, args);
                mcb.onSuccess();
                return result;
            } catch (Throwable t) {
                mcb.onError();
                if (fallback != NOT_SET) {
                    return fallback;
                } else {
                    throw t;
                }
            }
        } else {
            return fallback;
        }
    }
}