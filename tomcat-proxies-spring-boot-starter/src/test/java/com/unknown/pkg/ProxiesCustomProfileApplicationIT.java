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
package com.unknown.pkg;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import org.junit.runner.RunWith;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Make sure everything works also with different package-names.
 *
 * @author  jbellmann
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {ProxiesApplication.class})
@WebIntegrationTest(randomPort = false)
@ActiveProfiles("custom")
public class ProxiesCustomProfileApplicationIT {

    private static final Logger LOG = LoggerFactory.getLogger(ProxiesCustomProfileApplicationIT.class);

    @Value("${local.server.port}")
    private int port;

    @Test
    public void startsUp() throws InterruptedException {
        TimeUnit.SECONDS.sleep(5);
        LOG.info("RUNNING ON PORT : {}", port);
    }
}
