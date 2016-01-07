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
import org.zalando.github.ExtOrganization;
import org.zalando.github.Organization;
import org.zalando.github.OrganizationUpdate;

/**
 * 
 * @author jbellmann
 *
 */
public class OrganizationTemplateTest extends AbstractTemplateTest {

	protected OrganizationsTemplate usersTemplate;

	@Before
	public void setupTemplate() {
		this.usersTemplate = new OrganizationsTemplate(restTemplate);
	}

	@Test
	public void listAllOrganizations() throws Exception {
		mockServer.expect(requestTo("https://api.github.com/organizations")).andExpect(method(HttpMethod.GET))
				// .andExpect(header("Authorization", "Bearer ACCESS_TOKEN"))
				.andRespond(
						withSuccess(new ClassPathResource("listOrgas.json", getClass()), MediaType.APPLICATION_JSON));

		List<Organization> emailList = usersTemplate.listAllOranizations();

		Assertions.assertThat(emailList).isNotNull();
		Assertions.assertThat(emailList.size()).isEqualTo(1);
	}

	@Test
	public void listOrganizationsForCurrentUser() throws Exception {
		mockServer.expect(requestTo("https://api.github.com/user/orgs")).andExpect(method(HttpMethod.GET))
				// .andExpect(header("Authorization", "Bearer ACCESS_TOKEN"))
				.andRespond(
						withSuccess(new ClassPathResource("listOrgas.json", getClass()), MediaType.APPLICATION_JSON));

		List<Organization> emailList = usersTemplate.listOrganizations();

		Assertions.assertThat(emailList).isNotNull();
		Assertions.assertThat(emailList.size()).isEqualTo(1);
	}

	@Test
	public void deleteEmails() throws Exception {
		mockServer.expect(requestTo("https://api.github.com/user/klaus/orgs")).andExpect(method(HttpMethod.GET))
				// .andExpect(header("Authorization", "Bearer ACCESS_TOKEN"))
				.andRespond(
						withSuccess(new ClassPathResource("listOrgas.json", getClass()), MediaType.APPLICATION_JSON));

		List<Organization> orgaList = usersTemplate.listUserOrganizations("klaus");

		Assertions.assertThat(orgaList).isNotNull();
		Assertions.assertThat(orgaList.size()).isEqualTo(1);
	}

	@Test
	public void getOrganization() throws Exception {
		mockServer.expect(requestTo("https://api.github.com/orgs/zalando-stups")).andExpect(method(HttpMethod.GET))
				// .andExpect(header("Authorization", "Bearer ACCESS_TOKEN"))
				.andRespond(withSuccess(new ClassPathResource("getOrga.json", getClass()), MediaType.APPLICATION_JSON));

		ExtOrganization orga = usersTemplate.getOrganization("zalando-stups");

		Assertions.assertThat(orga).isNotNull();
		Assertions.assertThat(orga.getName()).isEqualTo("zalando-stups");
		Assertions.assertThat(orga.getLogin()).isEqualTo("zalando-stups");
	}

	@Test
	public void patchOrganization() throws Exception {
		mockServer.expect(requestTo("https://api.github.com/orgs/zalando-stups")).andExpect(method(HttpMethod.PATCH))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				// .andExpect(header("Authorization", "Bearer ACCESS_TOKEN"))
				.andRespond(withSuccess(new ClassPathResource("getOrga.json", getClass()), MediaType.APPLICATION_JSON));

		OrganizationUpdate update = new OrganizationUpdate();
		update.setBillingEmail("no-billing-for-open-source@gmail.com");
		ExtOrganization orga = usersTemplate.updateOrganization(update, "zalando-stups");

		Assertions.assertThat(orga).isNotNull();
		Assertions.assertThat(orga.getName()).isEqualTo("zalando-stups");
		Assertions.assertThat(orga.getLogin()).isEqualTo("zalando-stups");
	}
}
