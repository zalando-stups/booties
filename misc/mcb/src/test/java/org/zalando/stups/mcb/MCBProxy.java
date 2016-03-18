package org.zalando.stups.mcb;

import java.lang.reflect.Proxy;

public class MCBProxy {

    public static <T> T proxy(T target, Class<T> iface){
        return proxy(target, iface, McbInvocationHandler.NOT_SET);
    }

    @SuppressWarnings("unchecked")
    public static <T> T proxy(T target, Class<T> iface, Object def) {
        return (T) Proxy.newProxyInstance(iface.getClassLoader(), new Class[] { iface },
                new McbInvocationHandler(new MCB(), target, def));
    }

}
