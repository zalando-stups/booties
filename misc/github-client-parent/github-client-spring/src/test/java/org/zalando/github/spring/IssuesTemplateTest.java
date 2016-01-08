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

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.zalando.github.Issue;
import org.zalando.github.IssueRequest;

public class IssuesTemplateTest extends AbstractTemplateTest {

	protected IssuesTemplate issuesTemplate;

	@Before
	public void setupTemplate() {
		this.issuesTemplate = new IssuesTemplate(restTemplate);
	}

	@Test
	public void createIssue() throws Exception {
		mockServer.expect(requestTo("https://api.github.com/repos/klaus/simple/issues"))
				.andExpect(method(HttpMethod.POST)).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				// .andExpect(header("Authorization", "Bearer ACCESS_TOKEN"))
				.andRespond(
						withSuccess(new ClassPathResource("createIssue.json", getClass()), MediaType.APPLICATION_JSON));

		Issue issue = issuesTemplate.createIssue(new IssueRequest("issueTitle"), "klaus", "simple");

		Assertions.assertThat(issue).isNotNull();
		Assertions.assertThat(issue.getId()).isEqualTo(1);
	}

	@Test
	public void listAllIssues() throws Exception {
		mockServer.expect(requestTo("https://api.github.com/issues?per_page=25")).andExpect(method(HttpMethod.GET))
				// .andExpect(header("Authorization", "Bearer ACCESS_TOKEN"))
				.andRespond(
						withSuccess(new ClassPathResource("listIssues.json", getClass()), MediaType.APPLICATION_JSON));

		List<Issue> issueList = issuesTemplate.listAllIssues();

		Assertions.assertThat(issueList).isNotNull();
		Assertions.assertThat(issueList.size()).isEqualTo(1);
		Assertions.assertThat(issueList.get(0).getId()).isEqualTo(1);
	}
	
	@Test
	public void listUserIssues() throws Exception {
		mockServer.expect(requestTo("https://api.github.com/user/issues?per_page=25")).andExpect(method(HttpMethod.GET))
				// .andExpect(header("Authorization", "Bearer ACCESS_TOKEN"))
				.andRespond(
						withSuccess(new ClassPathResource("listIssues.json", getClass()), MediaType.APPLICATION_JSON));

		List<Issue> issueList = issuesTemplate.listUserIssues();

		Assertions.assertThat(issueList).isNotNull();
		Assertions.assertThat(issueList.size()).isEqualTo(1);
		Assertions.assertThat(issueList.get(0).getId()).isEqualTo(1);
	}
	
	@Test
	public void listOrgaIssues() throws Exception {
		mockServer.expect(requestTo("https://api.github.com/orgs/zalando-stups/issues?per_page=25")).andExpect(method(HttpMethod.GET))
				// .andExpect(header("Authorization", "Bearer ACCESS_TOKEN"))
				.andRespond(
						withSuccess(new ClassPathResource("listIssues.json", getClass()), MediaType.APPLICATION_JSON));

		List<Issue> issueList = issuesTemplate.listOrganizationIssues("zalando-stups");

		Assertions.assertThat(issueList).isNotNull();
		Assertions.assertThat(issueList.size()).isEqualTo(1);
		Assertions.assertThat(issueList.get(0).getId()).isEqualTo(1);
	}
}
