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

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.web.util.UriTemplate;

public class UriTemplateTest {

	@Test
	public void checkParameters() {
		Map<String, Object> uriVariables = new HashMap<>();
		uriVariables.put("first", "eins");
		uriVariables.put("second", "zwei");
		uriVariables.put("bar", "baz");
		uriVariables.put("thing", "something");
		URI uri = new UriTemplate("http://example.org/{first}/path/{second}?foo={bar}&bar={thing}")
				.expand(uriVariables);

		String uriString = uri.toString();
		Assertions.assertThat(uriString).contains("foo=baz");
		Assertions.assertThat(uriString).contains("bar=something");
		Assertions.assertThat(uriString).contains("eins/path/zwei");
		System.out.println(uri.toString());
	}

}
