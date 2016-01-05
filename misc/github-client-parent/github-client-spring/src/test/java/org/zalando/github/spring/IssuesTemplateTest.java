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

import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.zalando.github.Issue;
import org.zalando.github.IssueRequest;

public class IssuesTemplateTest {

	protected IssuesTemplate issuesTemplate;
	protected MockRestServiceServer mockServer;
	protected HttpHeaders responseHeaders;

	@Before
	public void setup() {
		RestTemplate restTemplate = new RestTemplate();
		this.issuesTemplate = new IssuesTemplate(restTemplate);
		// this.gitHub = new GitHubTemplate("ACCESS_TOKEN");
		this.mockServer = MockRestServiceServer.createServer(restTemplate);

		this.responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_JSON);

		// this.unauthorizedGitHub = new GitHubTemplate();

		// Create a mock server just to avoid hitting real GitHub if something
		// gets past the authorization check.
		// MockRestServiceServer.createServer(unauthorizedGitHub.getRestTemplate());
	}

	@Test
	public void getUserProfile() throws Exception {
		mockServer.expect(requestTo("https://api.github.com/repos/klaus/simple/issues"))
				.andExpect(method(HttpMethod.POST)).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				// .andExpect(header("Authorization", "Bearer ACCESS_TOKEN"))
				.andRespond(
						withSuccess(new ClassPathResource("createIssue.json", getClass()), MediaType.APPLICATION_JSON));

		Issue issue = issuesTemplate.createIssue(new IssueRequest(), "klaus", "simple");

		Assertions.assertThat(issue).isNotNull();
		Assertions.assertThat(issue.getId()).isEqualTo(1);
	}

	protected Resource jsonResource(String filename) {
		return new ClassPathResource(filename + ".json", getClass());
	}

}
