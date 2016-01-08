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
package org.zalando.github.spring.pagination;

import org.springframework.util.StringUtils;

import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

class StringToLinkRelation implements Function<String, LinkRelation> {

	private static final Splitter splitter = Splitter.on(";");

	@Override
	public LinkRelation apply(String input) {
		Iterable<String> splittResult = splitter.split(input);
		String link = Iterables.get(splittResult, 0);
		link = StringUtils.trimAllWhitespace(link);
		link = StringUtils.trimLeadingCharacter(link, '<');
		link = StringUtils.trimTrailingCharacter(link, '>');
		String relation = Iterables.get(splittResult, 1);
		relation = StringUtils.trimAllWhitespace(relation);
		return new LinkRelation(link, relation);
	}

}
