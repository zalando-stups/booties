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
import org.zalando.github.ExtPubKey;
import org.zalando.github.PubKey;

/**
 * 
 * @author jbellmann
 *
 */
public class UsersTemplatePublicKeyTest extends AbstractTemplateTest {

	protected UsersTemplate usersTemplate;

	@Before
	public void setupTemplate() {
		this.usersTemplate = new UsersTemplate(restTemplate);
	}

	@Test
	public void getPublicKeys() throws Exception {
		mockServer.expect(requestTo("https://api.github.com/user/keys")).andExpect(method(HttpMethod.GET))
				// .andExpect(header("Authorization", "Bearer ACCESS_TOKEN"))
				.andRespond(withSuccess(new ClassPathResource("listPublicKeys.json", getClass()),
						MediaType.APPLICATION_JSON));

		List<ExtPubKey> publicKeyList = usersTemplate.listPublicKeys();

		Assertions.assertThat(publicKeyList).isNotNull();
		Assertions.assertThat(publicKeyList.size()).isEqualTo(1);
		Assertions.assertThat(publicKeyList.get(0).getKey()).isEqualTo("ssh-rsa AAA...");
	}

	@Test
	public void getPublicKeysForUser() throws Exception {
		mockServer.expect(requestTo("https://api.github.com/users/klaus/keys")).andExpect(method(HttpMethod.GET))
				// .andExpect(header("Authorization", "Bearer ACCESS_TOKEN"))
				.andRespond(withSuccess(new ClassPathResource("listPublicKeysForUser.json", getClass()),
						MediaType.APPLICATION_JSON));

		List<PubKey> pubKeyList = usersTemplate.listPublicKeys("klaus");

		Assertions.assertThat(pubKeyList).isNotNull();
		Assertions.assertThat(pubKeyList.size()).isEqualTo(1);
		Assertions.assertThat(pubKeyList.get(0).getKey()).isEqualTo("ssh-rsa AAA...");
	}

	@Test
	public void deletePublicKey() throws Exception {
		mockServer.expect(requestTo("https://api.github.com/user/keys/1")).andExpect(method(HttpMethod.DELETE))
				// .andExpect(header("Authorization", "Bearer ACCESS_TOKEN"))
				.andRespond(MockRestResponseCreators.withNoContent());

		usersTemplate.deletePublicKey(1);
	}

}
