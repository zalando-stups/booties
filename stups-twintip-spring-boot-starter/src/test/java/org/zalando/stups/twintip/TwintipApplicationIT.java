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
package org.zalando.stups.twintip;

import org.assertj.core.api.Assertions;

import org.junit.Test;

import org.junit.runner.RunWith;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.springframework.web.client.RestTemplate;

/**
 * Just invoke the endpoint and check results.
 *
 * @author  jbellmann
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {TwintipApplication.class})
@WebIntegrationTest(randomPort = true)
public class TwintipApplicationIT {

    private static final Logger LOG = LoggerFactory.getLogger(TwintipApplicationIT.class);

    @Value("${local.server.port}")
    private int port;

    @Test
    public void isConfigured() {

        String endpointUrl = "http://localhost:" + port + "/.well-known/schema-discovery";
        LOG.info("ENDPOINT_URL : {}", endpointUrl);

        RestTemplate template = new RestTemplate();
        ResponseEntity<String> entity = template.getForEntity(endpointUrl, String.class);
        HttpStatus statusCode = entity.getStatusCode();
        String body = entity.getBody();

        Assertions.assertThat(statusCode).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(body).contains("swagger-2.0");

        LOG.info("BODY : {}", body);

    }
}
