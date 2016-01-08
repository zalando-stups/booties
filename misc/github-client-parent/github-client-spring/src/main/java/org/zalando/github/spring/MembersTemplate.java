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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestOperations;
import org.zalando.github.MembersOperations;
import org.zalando.github.User;
import org.zalando.github.spring.pagination.PagingIterator;

/**
 * 
 * @author jbellmann
 *
 */
public class MembersTemplate extends AbstractGithubTemplate implements MembersOperations {

	private final ParameterizedTypeReference<List<User>> userListTypeRef = new ParameterizedTypeReference<List<User>>() {
	};

	public MembersTemplate(RestOperations restOperations, GithubApiUriUtil uriUtil) {
		super(restOperations, uriUtil);
	}

	public MembersTemplate(RestOperations restOperations) {
		super(restOperations);
	}

	@Override
	public List<User> listMembers(String organization) {
		Map<String, Object> uriVariables = new HashMap<>();
		uriVariables.put("organization", organization);

		return fetchUsers("/orgs/{organization}/members?per_page=25", uriVariables);
	}

	@Override
	public List<User> listPublicMembers(String organization) {
		Map<String, Object> uriVariables = new HashMap<>();
		uriVariables.put("organization", organization);

		return fetchUsers("/orgs/{organization}/public_members?per_page=25", uriVariables);
	}

	protected List<User> fetchUsers(String path, Map<String, Object> uriVariables) {
		List<User> usersList = new ArrayList<User>();
		Iterator<List<User>> iter = new PagingIterator<>(getRestOperations(), buildUri(path, uriVariables),
				userListTypeRef);
		while (iter.hasNext()) {
			usersList.addAll(iter.next());
		}
		return usersList;
	}

	@Override
	public boolean isMemberOfOrganization(String organization, String username) {
		Map<String, Object> uriVariables = new HashMap<>();
		uriVariables.put("organization", organization);
		uriVariables.put("username", username);

		HttpStatus status = HttpStatus.NOT_FOUND;
		try {

			ResponseEntity<Void> responseEntity = getRestOperations()
					.getForEntity(buildUri("/orgs/{organization}/members/{username}", uriVariables), Void.class);
			status = responseEntity.getStatusCode();
		} catch (HttpClientErrorException e) {
			// skip
		}
		return HttpStatus.NO_CONTENT.equals(status) ? true : false;
	}

	@Override
	public boolean isPublicMemberOfOrganization(String organization, String username) {
		Map<String, Object> uriVariables = new HashMap<>();
		uriVariables.put("organization", organization);
		uriVariables.put("username", username);
		HttpStatus status = HttpStatus.NOT_FOUND;
		try {

			ResponseEntity<Void> responseEntity = getRestOperations()
					.getForEntity(buildUri("/orgs/{organization}/public_members/{username}", uriVariables), Void.class);
			status = responseEntity.getStatusCode();
		} catch (HttpClientErrorException e) {
			// skip
		}
		return HttpStatus.NO_CONTENT.equals(status) ? true : false;
	}

	@Override
	public void removeFromOrganization(String organization, String username) {
		Map<String, Object> uriVariables = new HashMap<>();
		uriVariables.put("organization", organization);
		uriVariables.put("username", username);
		getRestOperations().delete(buildUri("/orgs/{organization}/members/{username}", uriVariables));
	}

}
