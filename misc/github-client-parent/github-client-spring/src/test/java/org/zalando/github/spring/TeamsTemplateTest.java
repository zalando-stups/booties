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
import static org.springframework.test.web.client.response.MockRestResponseCreators.withNoContent;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.zalando.github.Team;
import org.zalando.github.TeamRequest;

public class TeamsTemplateTest extends AbstractTemplateTest {

	private TeamsTemplate teamsTemplate;

	@Before
	public void setupTemplate() {
		this.teamsTemplate = new TeamsTemplate(restTemplate);
	}

	@Test
	public void listTeams() throws Exception {
		mockServer.expect(requestTo("https://api.github.com/orgs/zalando-stups/teams?per_page=25"))
				.andExpect(method(HttpMethod.GET))
				// .andExpect(header("Authorization", "Bearer ACCESS_TOKEN"))
				.andRespond(
						withSuccess(new ClassPathResource("listTeams.json", getClass()), MediaType.APPLICATION_JSON));

		List<Team> teamList = teamsTemplate.listTeams("zalando-stups");

		Assertions.assertThat(teamList).isNotNull();
		Assertions.assertThat(teamList.size()).isEqualTo(1);
	}

	@Test
	public void getTeam() throws Exception {
		mockServer.expect(requestTo("https://api.github.com/teams/1")).andExpect(method(HttpMethod.GET))
				// .andExpect(header("Authorization", "Bearer ACCESS_TOKEN"))
				.andRespond(withSuccess(new ClassPathResource("getTeam.json", getClass()), MediaType.APPLICATION_JSON));

		Team team = teamsTemplate.getTeam(1);

		Assertions.assertThat(team).isNotNull();
		Assertions.assertThat(team.getId()).isEqualTo(1);
		Assertions.assertThat(team.getName()).isEqualTo("Justice League");
	}

	@Test
	public void updateTeam() throws Exception {
		mockServer.expect(requestTo("https://api.github.com/teams/1")).andExpect(method(HttpMethod.PATCH))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				// .andExpect(header("Authorization", "Bearer ACCESS_TOKEN"))
				.andRespond(withSuccess(new ClassPathResource("getTeam.json", getClass()), MediaType.APPLICATION_JSON));

		Team team = teamsTemplate.updateTeam(1, new TeamRequest("Justice League"));

		Assertions.assertThat(team).isNotNull();
		Assertions.assertThat(team.getId()).isEqualTo(1);
		Assertions.assertThat(team.getName()).isEqualTo("Justice League");
	}

	@Test
	public void createTeam() throws Exception {
		mockServer.expect(requestTo("https://api.github.com/orgs/zalando-stups/teams"))
				.andExpect(method(HttpMethod.POST)).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				// .andExpect(header("Authorization", "Bearer ACCESS_TOKEN"))
				.andRespond(withSuccess(new ClassPathResource("getTeam.json", getClass()), MediaType.APPLICATION_JSON));

		Team team = teamsTemplate.createTeam("zalando-stups", new TeamRequest("Justice League"));

		Assertions.assertThat(team).isNotNull();
		Assertions.assertThat(team.getId()).isEqualTo(1);
		Assertions.assertThat(team.getName()).isEqualTo("Justice League");
	}

	@Test
	public void deleteTeam() throws Exception {
		mockServer.expect(requestTo("https://api.github.com/teams/1")).andExpect(method(HttpMethod.DELETE))
				// .andExpect(header("Authorization", "Bearer ACCESS_TOKEN"))
				.andRespond(withNoContent());

		teamsTemplate.deleteTeam(1);

	}
}
