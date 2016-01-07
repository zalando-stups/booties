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
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.zalando.github.Email;

/**
 * 
 * @author jbellmann
 *
 */
public class UsersTemplateTest extends AbstractTemplateTest {

	protected UsersTemplate usersTemplate;

	@Before
	public void setupTemplate() {
		this.usersTemplate = new UsersTemplate(restTemplate);
	}

	@Test
	public void getEmails() throws Exception {
		mockServer.expect(requestTo("https://api.github.com/user/emails")).andExpect(method(HttpMethod.GET))
				// .andExpect(header("Authorization", "Bearer ACCESS_TOKEN"))
				.andRespond(
						withSuccess(new ClassPathResource("listEmails.json", getClass()), MediaType.APPLICATION_JSON));

		List<Email> emailList = usersTemplate.listEmails();

		Assertions.assertThat(emailList).isNotNull();
		Assertions.assertThat(emailList.size()).isEqualTo(1);
	}

	@Test
	public void addEmails() throws Exception {
		mockServer.expect(requestTo("https://api.github.com/user/emails")).andExpect(method(HttpMethod.POST))
				// .andExpect(header("Authorization", "Bearer ACCESS_TOKEN"))
				.andRespond(
						withSuccess(new ClassPathResource("addEmails.json", getClass()), MediaType.APPLICATION_JSON));

		List<Email> emailList = usersTemplate.addEmails("octocat@github.com", "support@github.com");

		Assertions.assertThat(emailList).isNotNull();
		Assertions.assertThat(emailList.size()).isEqualTo(2);
	}

	@Test
	public void deleteEmails() throws Exception {
		mockServer.expect(requestTo("https://api.github.com/user/emails")).andExpect(method(HttpMethod.DELETE))
				// .andExpect(header("Authorization", "Bearer ACCESS_TOKEN"))
				.andRespond(MockRestResponseCreators.withNoContent());

		usersTemplate.deleteEmails("octocat@github.com", "support@github.com");

		mockServer.verify();
	}

}
