package org.zalando.stups.mcb;

import java.util.HashMap;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class ProxyTest {

    @Test
    public void create() {
        // MCB mcb = new MCB();
        Endpoint proxied = MCBProxy.proxy(new OriginalEndpoint(), Endpoint.class);
        // Endpoint proxied = (Endpoint)
        // Proxy.newProxyInstance(getClass().getClassLoader(),
        // new Class[] { Endpoint.class },
        // new McbInvocationHandler(mcb, new OriginalEndpoint(), null));
        Map<String, Object> result = proxied.load("EGAL");
        result = proxied.load("EGAL");
        Assertions.assertThat(result).isNotNull();

        result = proxied.load("egal");
        Assertions.assertThat(result).isNotNull();

        try {

            result = proxied.load("EX");
        } catch (RuntimeException e) {

        }
        Assertions.assertThat(result).isNull();

        System.out.println(result.values());

    }

    interface Endpoint {
        Map<String, Object> load(String value);
    }

    class OriginalEndpoint implements Endpoint {

        @Override
        public Map<String, Object> load(String value) {
            if ("EX".equals(value)) {
                throw new RuntimeException("SERVICE_UNAVIALABLE");
            }
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("Key", "VAlue");
            return result;
        }

    }

}
