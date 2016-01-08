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

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withNoContent;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.zalando.github.User;

/**
 * 
 * @author jbellmann
 *
 */
public class MembersTemplateTest extends AbstractTemplateTest {

	protected MembersTemplate membersTemplate;

	@Before
	public void setupTemplate() {
		this.membersTemplate = new MembersTemplate(restTemplate);
	}

	@Test
	public void listMembers() throws Exception {
		mockServer.expect(requestTo("https://api.github.com/orgs/zalando-stups/members?per_page=25"))
				.andExpect(method(HttpMethod.GET))
				// .andExpect(header("Authorization", "Bearer ACCESS_TOKEN"))
				.andRespond(
						withSuccess(new ClassPathResource("listMembers.json", getClass()), MediaType.APPLICATION_JSON));

		List<User> issueList = membersTemplate.listMembers("zalando-stups");

		Assertions.assertThat(issueList).isNotNull();
		Assertions.assertThat(issueList.size()).isEqualTo(1);
	}

	@Test
	public void listPublicMembers() throws Exception {
		mockServer.expect(requestTo("https://api.github.com/orgs/zalando-stups/public_members?per_page=25"))
				.andExpect(method(HttpMethod.GET))
				// .andExpect(header("Authorization", "Bearer ACCESS_TOKEN"))
				.andRespond(
						withSuccess(new ClassPathResource("listMembers.json", getClass()), MediaType.APPLICATION_JSON));

		List<User> issueList = membersTemplate.listPublicMembers("zalando-stups");

		Assertions.assertThat(issueList).isNotNull();
		Assertions.assertThat(issueList.size()).isEqualTo(1);
	}

	@Test
	public void isMember() throws Exception {
		mockServer.expect(requestTo("https://api.github.com/orgs/zalando-stups/members/klaus"))
				.andExpect(method(HttpMethod.GET))
				// .andExpect(header("Authorization", "Bearer ACCESS_TOKEN"))
				.andRespond(withNoContent());

		boolean answer = membersTemplate.isMemberOfOrganization("zalando-stups", "klaus");

		Assertions.assertThat(answer).isTrue();
	}

	@Test
	public void isNotMember() throws Exception {
		mockServer.expect(requestTo("https://api.github.com/orgs/zalando-stups/members/klaus"))
				.andExpect(method(HttpMethod.GET))
				// .andExpect(header("Authorization", "Bearer ACCESS_TOKEN"))
				.andRespond(withStatus(NOT_FOUND));

		boolean answer = membersTemplate.isMemberOfOrganization("zalando-stups", "klaus");

		Assertions.assertThat(answer).isFalse();
	}

	@Test
	public void removeMember() throws Exception {
		mockServer.expect(requestTo("https://api.github.com/orgs/zalando-stups/members/klaus"))
				.andExpect(method(HttpMethod.DELETE))
				// .andExpect(header("Authorization", "Bearer ACCESS_TOKEN"))
				.andRespond(withNoContent());

		membersTemplate.removeFromOrganization("zalando-stups", "klaus");
	}
}
