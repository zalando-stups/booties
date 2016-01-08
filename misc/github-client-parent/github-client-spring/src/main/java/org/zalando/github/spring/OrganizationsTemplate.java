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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.web.client.RestOperations;
import org.zalando.github.ExtOrganization;
import org.zalando.github.Organization;
import org.zalando.github.OrganizationUpdate;
import org.zalando.github.OrganizationsOperations;
import org.zalando.github.spring.pagination.PagingIterator;

public class OrganizationsTemplate extends AbstractGithubTemplate implements OrganizationsOperations {

	private final ParameterizedTypeReference<List<Organization>> orgaListTypeRef = new ParameterizedTypeReference<List<Organization>>() {
	};

	public OrganizationsTemplate(RestOperations restOperations) {
		super(restOperations);
	}
	
	public  OrganizationsTemplate(RestOperations restOperations, GithubApiUriUtil githubApiUriUtil) {
		super(restOperations, githubApiUriUtil);
	}

	@Override
	public List<Organization> listAllOranizations() {
		return listOrganizations("/organizations?per_page=100");
	}

	@Override
	public List<Organization> listOrganizations() {

		return listOrganizations("/user/orgs?per_page=25");
	}

	@Override
	public List<Organization> listUserOrganizations(String username) {
		Map<String, Object> uriVariables = new HashMap<>();
		uriVariables.put("username", username);

		return listOrganizations("/user/{username}/orgs?per_page=25", uriVariables);
	}

	protected List<Organization> listOrganizations(String path) {
		return listOrganizations(path, Collections.emptyMap());
	}

	protected List<Organization> listOrganizations(String path, Map<String, Object> uriVariables) {
		List<Organization> result = new ArrayList<Organization>();
		Iterator<List<Organization>> iter = new PagingIterator<>(getRestOperations(), buildUri(path, uriVariables), orgaListTypeRef);
		while(iter.hasNext()){
			result.addAll(iter.next());
		}
		return result;
	}

	@Override
	public ExtOrganization getOrganization(String organization) {
		Map<String, Object> uriVariables = new HashMap<>();
		uriVariables.put("organization", organization);
		return getRestOperations().getForObject(buildUri("/orgs/{organization}", uriVariables), ExtOrganization.class);
	}

	@Override
	public ExtOrganization updateOrganization(OrganizationUpdate update, String organization) {
		Map<String, Object> uriVariables = new HashMap<>();
		uriVariables.put("organization", organization);

		RequestEntity<OrganizationUpdate> entity = RequestEntity.patch(buildUri("/orgs/{organization}", uriVariables))
				.contentType(MediaType.APPLICATION_JSON).body(update);

		return getRestOperations().exchange(entity, ExtOrganization.class).getBody();
	}

}
