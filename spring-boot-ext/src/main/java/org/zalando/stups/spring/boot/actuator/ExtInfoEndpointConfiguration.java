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
package org.zalando.stups.spring.boot.actuator;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.autoconfigure.EndpointAutoConfiguration;
import org.springframework.boot.actuate.endpoint.InfoEndpoint;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Creates an {@link InfoEndpoint} from existing '.properties'-file in the classpath.
 * 
 * @author jbellmann
 *
 */
@AutoConfigureBefore({ EndpointAutoConfiguration.class })
public class ExtInfoEndpointConfiguration {

	private static final String PROPERTIES_SUFFIX_REPLACEMENT = "";
	private static final String PROPERTIES_SUFFIX = ".properties";
	private final Logger log = LoggerFactory.getLogger(ExtInfoEndpointConfiguration.class);

	@Bean
	public InfoEndpoint infoEndpoint() throws Exception {
		LinkedHashMap<String, Object> info = new LinkedHashMap<String, Object>();
		for (String filename : getAllPropertiesFiles()) {
			Resource resource = new ClassPathResource("/" + filename);
			Properties properties = new Properties();
			if (resource.exists()) {
				properties = PropertiesLoaderUtils.loadProperties(resource);
				String name = resource.getFilename();

				info.put(name.replace(PROPERTIES_SUFFIX, PROPERTIES_SUFFIX_REPLACEMENT), Maps.fromProperties(properties));
			} else {
				if (failWhenResourceNotExists()) {
					throw new RuntimeException("Resource : " + filename + " does not exist");
				} else {
					log.info("Resource {} does not exist", filename);
				}
			}
		}
		return new InfoEndpoint(info);
	}

	protected List<String> getAllPropertiesFiles() {
		return Lists.newArrayList();
	}

	protected boolean failWhenResourceNotExists() {
		return false;
	}
}
