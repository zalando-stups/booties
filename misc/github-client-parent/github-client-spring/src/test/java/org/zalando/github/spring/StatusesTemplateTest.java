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

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.zalando.github.CombinedStatus;
import org.zalando.github.Status;
import org.zalando.github.StatusRequest;

public class StatusesTemplateTest extends AbstractTemplateTest {

	protected StatusesTemplate statusesTemplate;

	@Before
	public void setupTemplate() {
		this.statusesTemplate = new StatusesTemplate(restTemplate);
	}

	@Test
	public void combinedStatuses() throws Exception {
		mockServer.expect(requestTo("https://api.github.com/repos/zalando-stups/blub/commits/abcdefgh1234567/status"))
				.andExpect(method(HttpMethod.GET))
				// .andExpect(header("Authorization", "Bearer ACCESS_TOKEN"))
				.andRespond(withSuccess(jsonResource("combinedStatus"), APPLICATION_JSON));

		CombinedStatus status = statusesTemplate.getCombinedStatus("zalando-stups", "blub", "abcdefgh1234567");

		Assertions.assertThat(status).isNotNull();
		Assertions.assertThat(status.getTotalCount()).isEqualTo(2);
		Assertions.assertThat(status.getRepository().getId()).isEqualTo(1296269);
	}

	@Test
	public void listStatuses() throws Exception {
		mockServer.expect(requestTo("https://api.github.com/repos/zalando-stups/blub/commits/abcdefgh1234567/statuses"))
				.andExpect(method(HttpMethod.GET))
				// .andExpect(header("Authorization", "Bearer ACCESS_TOKEN"))
				.andRespond(withSuccess(jsonResource("statusList"), APPLICATION_JSON));

		List<Status> statusList = statusesTemplate.listStatuses("zalando-stups", "blub", "abcdefgh1234567");

		Assertions.assertThat(statusList).isNotNull();
		Assertions.assertThat(statusList.size()).isEqualTo(1);
		Assertions.assertThat(statusList.get(0).getId()).isEqualTo(1);
	}
	

	@Test
	public void createStatuses() throws Exception {
		mockServer.expect(requestTo("https://api.github.com/repos/zalando-stups/blub/statuses/abcdefgh1234567"))
				.andExpect(method(HttpMethod.POST))
				// .andExpect(header("Authorization", "Bearer ACCESS_TOKEN"))
				.andRespond(withSuccess(jsonResource("status"), APPLICATION_JSON));

		StatusRequest request = StatusRequest.pendingStatusRequest();
		request.setTargetUrl("https://ci.example.com/1000/output");
		request.setDescription("Build started");
		request.setContext("continuous-integration/whatever");
		Status status = statusesTemplate.createStatus("zalando-stups", "blub", "abcdefgh1234567", request);

		Assertions.assertThat(status).isNotNull();
		Assertions.assertThat(status.getId()).isEqualTo(1);
		Assertions.assertThat(status.getUrl()).isEqualTo("https://api.github.com/repos/octocat/Hello-World/statuses/1");
	}
}
