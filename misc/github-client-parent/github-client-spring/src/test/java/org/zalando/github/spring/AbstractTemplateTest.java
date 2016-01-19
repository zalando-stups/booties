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
package org.zalando.github.spring;

import java.io.IOException;
import java.nio.charset.Charset;

import org.junit.Before;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

public abstract class AbstractTemplateTest {

	protected RestTemplate restTemplate;
	protected MockRestServiceServer mockServer;
	protected HttpHeaders responseHeaders;

	@Before
	public void setup() {
		restTemplate = new RestTemplate();
		// this.gitHub = new GitHubTemplate("ACCESS_TOKEN");
		this.mockServer = MockRestServiceServer.createServer(restTemplate);

		this.responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_JSON);

		// this.unauthorizedGitHub = new GitHubTemplate();

		// Create a mock server just to avoid hitting real GitHub if something
		// gets past the authorization check.
		// MockRestServiceServer.createServer(unauthorizedGitHub.getRestTemplate());
	}

	protected Resource jsonResource(String filename) {
		return new ClassPathResource(filename + ".json", getClass());
	}

	public static String resourceToString(Resource resource) throws IOException {
		return StreamUtils.copyToString(resource.getInputStream(), Charset.defaultCharset());
	}
}
