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
import java.util.Map;

import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriTemplate;

public abstract class AbstractGithubTemplate {

	private RestOperations restOperations;

	public AbstractGithubTemplate(RestOperations restOperations) {
		this.restOperations = restOperations;
	}

	public RestOperations getRestOperations() {
		return restOperations;
	}

	protected UriTemplate buildUriTemplate(String path) {
		return new UriTemplate(buildUriString(path));
	}

	protected URI buildUri(String path, Map<String, Object> uriVariables) {
		return new UriTemplate(buildUriString(path)).expand(uriVariables);
	}

	protected URI buildUri(String path) {
		return buildUri(path, new HashMap<String, Object>(0));
	}

	protected String buildUriString(String path) {
		return "https://api.github.com" + path;
	}

}
