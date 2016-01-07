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

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriTemplate;
import org.zalando.github.Issue;
import org.zalando.github.IssueRequest;
import org.zalando.github.IssuesOperations;

/**
 * 
 * @author jbellmann
 *
 */
public class IssuesTemplate extends AbstractGithubTemplate implements IssuesOperations {

	private final ParameterizedTypeReference<List<Issue>> issueListTypeRef = new ParameterizedTypeReference<List<Issue>>() {
	};

	public IssuesTemplate(RestOperations restOperations) {
		super(restOperations);
	}

	//TODO, pagination
	@Override
	public List<Issue> listAllIssues() {
		return getRestOperations().exchange(buildUri("/issues"), HttpMethod.GET, null, issueListTypeRef).getBody();
	}

	//TODO, pagination
	@Override
	public List<Issue> listUserIssues() {
		return getRestOperations().exchange(buildUri("/user/issues"), HttpMethod.GET, null, issueListTypeRef).getBody();
	}

	//TODO, pagination
	@Override
	public List<Issue> listOrganizationIssues(String organization) {
		Map<String, Object> uriVariables = new HashMap<>();
		uriVariables.put("organization", organization);
		return getRestOperations()
				.exchange(buildUri("/orgs/{organization}/issues", uriVariables), HttpMethod.GET, null, issueListTypeRef)
				.getBody();
	}

	@Override
	public Issue createIssue(IssueRequest issueRequest, String owner, String repo) {
		Map<String, Object> uriVariables = new HashMap<>();
		uriVariables.put("owner", owner);
		uriVariables.put("repo", repo);

		URI uri = new UriTemplate(buildUriString("/repos/{owner}/{repo}/issues")).expand(uriVariables);
		RequestEntity<IssueRequest> entity = RequestEntity.post(uri).contentType(MediaType.APPLICATION_JSON)
				.body(issueRequest);

		ResponseEntity<Issue> responseEntity = getRestOperations().exchange(entity, Issue.class);
		return responseEntity.getBody();
	}

}
