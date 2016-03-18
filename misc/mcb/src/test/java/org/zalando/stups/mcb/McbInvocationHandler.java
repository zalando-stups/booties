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