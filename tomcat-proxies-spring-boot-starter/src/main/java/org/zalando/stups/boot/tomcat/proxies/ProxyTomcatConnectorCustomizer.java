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
package org.zalando.stups.boot.tomcat.proxies;

import org.apache.catalina.connector.Connector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;

import org.springframework.stereotype.Component;

/**
 * @author  jbellmann
 */
@Component
public class ProxyTomcatConnectorCustomizer implements TomcatConnectorCustomizer {

    private final Logger logger = LoggerFactory.getLogger(ProxyTomcatConnectorCustomizer.class);

    private final ProxyConnectorCustomizersProperties proxyConnectorCustomizerProperties;

    @Autowired
    public ProxyTomcatConnectorCustomizer(
            final ProxyConnectorCustomizersProperties proxyConnectorCustomizerProperties) {
        this.proxyConnectorCustomizerProperties = proxyConnectorCustomizerProperties;
    }

    @Override
    public void customize(final Connector connector) {
        if (!proxyConnectorCustomizerProperties.isEnabled()) {
            logger.warn("CUSTOMIZE CONNECTORS IS DISABLED");
            return;
        }

        for (ConnectorCustomizer cc : proxyConnectorCustomizerProperties.getCustomizers()) {

            if (cc.isEnabled()) {

                if (connector.getPort() == cc.getPort()) {
                    logger.warn("CUSTOMIZE CONNECTOR ON PORT : {}", connector.getPort());

                    logger.warn("SET CONNECTOR - 'secure' : {}", cc.isSecure());
                    connector.setSecure(cc.isSecure());

                    logger.warn("SET CONNECTOR - 'scheme' : {}", cc.getScheme());
                    connector.setScheme(cc.getScheme());

                    logger.warn("SET CONNECTOR - 'proxy-port' : {}", cc.getProxyPort());
                    connector.setProxyPort(cc.getProxyPort());

                    logger.warn("SET CONNECTOR - 'proxy-name' : {}", cc.getProxyName());
                    connector.setProxyName(cc.getProxyName());
                } else {
                    logger.info("No customizer found for connector on port : {}", connector.getPort());
                }

            }
        }
    }

}
