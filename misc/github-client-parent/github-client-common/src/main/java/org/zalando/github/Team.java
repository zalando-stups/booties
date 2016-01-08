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
package org.zalando.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Team {

	private long id;

	private String url;

	private String name;

	private String slug;

	private String description;

	private String privacy;

	private String permission;

	@JsonProperty("members_url")
	private String membersUrl;

	@JsonProperty("repositories_url")
	private String repositoriesUrl;

	@JsonProperty("members_count")
	private long membersCount;

	@JsonProperty("repos_count")
	private long reposCount;

	private Organization organization;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPrivacy() {
		return privacy;
	}

	public void setPrivacy(String privacy) {
		this.privacy = privacy;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public String getMembersUrl() {
		return membersUrl;
	}

	public void setMembersUrl(String membersUrl) {
		this.membersUrl = membersUrl;
	}

	public String getRepositoriesUrl() {
		return repositoriesUrl;
	}

	public void setRepositoriesUrl(String repositoriesUrl) {
		this.repositoriesUrl = repositoriesUrl;
	}

	public long getMembersCount() {
		return membersCount;
	}

	public void setMembersCount(long membersCount) {
		this.membersCount = membersCount;
	}

	public long getReposCount() {
		return reposCount;
	}

	public void setReposCount(long reposCount) {
		this.reposCount = reposCount;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

}
