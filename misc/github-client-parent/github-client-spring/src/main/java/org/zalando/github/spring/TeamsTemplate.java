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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.web.client.RestOperations;
import org.zalando.github.Team;
import org.zalando.github.TeamRequest;
import org.zalando.github.TeamsOperations;
import org.zalando.github.spring.pagination.PagingIterator;

/**
 * 
 * @author jbellmann
 *
 */
public class TeamsTemplate extends AbstractGithubTemplate implements TeamsOperations {

	private final ParameterizedTypeReference<List<Team>> teamListTypeRef = new ParameterizedTypeReference<List<Team>>() {
	};

	public TeamsTemplate(RestOperations restOperations, GithubApiUriUtil uriUtil) {
		super(restOperations, uriUtil);
	}

	public TeamsTemplate(RestOperations restOperations) {
		super(restOperations);
	}

	@Override
	public List<Team> listTeams(String organization) {
		Map<String, Object> uriVariables = new HashMap<>();
		uriVariables.put("organization", organization);

		List<Team> teamList = new ArrayList<Team>();
		Iterator<List<Team>> iter = new PagingIterator<>(getRestOperations(),
				buildUri("/orgs/{organization}/teams?per_page=25", uriVariables), teamListTypeRef);
		while (iter.hasNext()) {
			teamList.addAll(iter.next());
		}
		return teamList;
	}

	@Override
	public Team getTeam(long teamId) {
		Map<String, Object> uriVariables = new HashMap<>();
		uriVariables.put("teamid", teamId);
		return getRestOperations().getForObject(buildUri("/teams/{teamid}", uriVariables), Team.class);
	}

	@Override
	public Team createTeam(String organization, TeamRequest teamRequest) {
		Map<String, Object> uriVariables = new HashMap<>();
		uriVariables.put("organization", organization);

		RequestEntity<TeamRequest> entity = RequestEntity.post(buildUri("/orgs/{organization}/teams", uriVariables))
				.contentType(MediaType.APPLICATION_JSON).body(teamRequest);

		return getRestOperations().exchange(entity, Team.class).getBody();
	}

	@Override
	public Team updateTeam(long teamId, TeamRequest teamRequest) {
		Map<String, Object> uriVariables = new HashMap<>();
		uriVariables.put("teamid", teamId);

		RequestEntity<TeamRequest> entity = RequestEntity.patch(buildUri("/teams/{teamid}", uriVariables))
				.contentType(MediaType.APPLICATION_JSON).body(teamRequest);

		return getRestOperations().exchange(entity, Team.class).getBody();
	}

	@Override
	public void deleteTeam(long teamId) {
		Map<String, Object> uriVariables = new HashMap<>();
		uriVariables.put("teamid", teamId);
		getRestOperations().delete(buildUri("/teams/{teamid}", uriVariables));
	}

}
