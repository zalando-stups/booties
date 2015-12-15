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
package org.springframework.boot.autoconfigure.orm.jpa;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.SharedCacheMode;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jta.JtaAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.EclipselinkJpaAutoconfiguration.EclipseLinkEntityManagerCondition;
import org.springframework.boot.autoconfigure.orm.jpa.EntityManagerFactoryBuilder.EntityManagerFactoryBeanCallback;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.util.ClassUtils;

/**
 * Autoconfiguration for JPA with EclipseLink.
 *
 * @author  jbellmann
 */
@Configuration
@ConditionalOnClass({ LocalContainerEntityManagerFactoryBean.class})
@Conditional(EclipseLinkEntityManagerCondition.class)
@AutoConfigureAfter({ DataSourceAutoConfiguration.class, JtaAutoConfiguration.class })
public class EclipselinkJpaAutoconfiguration extends JpaBaseConfiguration {

    @Autowired
    private JpaProperties properties;

    @Autowired
    private DataSource dataSource;

    @Override
    protected AbstractJpaVendorAdapter createJpaVendorAdapter() {
        return new EclipseLinkJpaVendorAdapter();
    }

    @Override
    protected Map<String, Object> getVendorProperties() {
        Map<String, Object> vendorProperties = new LinkedHashMap<String, Object>();
        vendorProperties.putAll(this.properties.getProperties());
        return vendorProperties;
    }

    @Override
    protected EntityManagerFactoryBeanCallback getVendorCallback() {
        return new EclipseLinkEntityManagerFactoryBeanCallback();
    }

    /**
     * Not quite sure, is this configured by default?
     */
    static final class EclipseLinkEntityManagerFactoryBeanCallback implements EntityManagerFactoryBeanCallback {

        @Override
        public void execute(final LocalContainerEntityManagerFactoryBean factory) {
            factory.setSharedCacheMode(SharedCacheMode.ENABLE_SELECTIVE);
        }
    }

    /**
     * Same as for hibernate only changed class-names.
     */
    @Order(Ordered.HIGHEST_PRECEDENCE + 20)
    public static class EclipseLinkEntityManagerCondition extends SpringBootCondition {

        private static String[] CLASS_NAMES = {"org.eclipse.persistence.jpa.JpaEntityManager"};

        @Override
        public ConditionOutcome getMatchOutcome(final ConditionContext context, final AnnotatedTypeMetadata metadata) {
            for (String className : CLASS_NAMES) {
                if (ClassUtils.isPresent(className, context.getClassLoader())) {
                    return ConditionOutcome.match("found EclipselinkEntityManager class");
                }
            }

            return ConditionOutcome.noMatch("did not find EclipselinkEntityManager class");
        }

    }
}
