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
import org.zalando.github.CombinedStatus;
import org.zalando.github.Status;
import org.zalando.github.StatusRequest;
import org.zalando.github.StatusesOperations;

public class StatusesTemplate extends AbstractGithubTemplate implements StatusesOperations {

	private final ParameterizedTypeReference<List<Status>> statusListTypeRef = new ParameterizedTypeReference<List<Status>>() {
	};
	
	public StatusesTemplate(RestOperations restOperations) {
		super(restOperations);
	}

	@Override
	public Status createStatus(String owner, String repository, String sha, StatusRequest body) {
		Map<String, Object> uriVariables = new HashMap<>();
		uriVariables.put("owner", owner);
		uriVariables.put("repository", repository);
		uriVariables.put("sha", sha);
		
		URI uri = new UriTemplate(buildUriString("/repos/{owner}/{repository}/statuses/{sha}")).expand(uriVariables);
		RequestEntity<StatusRequest> entity = RequestEntity.post(uri).contentType(MediaType.APPLICATION_JSON)
				.body(body);

		ResponseEntity<Status> responseEntity = getRestOperations().exchange(entity, Status.class);
		return responseEntity.getBody();
	}

	@Override
	public List<Status> listStatuses(String owner, String repository,String ref) {
		Map<String, Object> uriVariables = new HashMap<>();
		uriVariables.put("owner", owner);
		uriVariables.put("repository", repository);
		uriVariables.put("ref", ref);
		
		return getRestOperations().exchange(buildUri("/repos/{owner}/{repository}/commits/{ref}/statuses", uriVariables),
				HttpMethod.GET, null, statusListTypeRef).getBody();
	}

	/**
	 * 'ref' can be SHA, branch or tag.
	 */
	@Override
	public CombinedStatus getCombinedStatus(String owner, String repository, String ref) {
		Map<String, Object> uriVariables = new HashMap<>();
		uriVariables.put("owner", owner);
		uriVariables.put("repository", repository);
		uriVariables.put("ref", ref);

		return getRestOperations().exchange(buildUri("/repos/{owner}/{repository}/commits/{ref}/status", uriVariables),
				HttpMethod.GET, null, CombinedStatus.class).getBody();
	}

}
