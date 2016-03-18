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
