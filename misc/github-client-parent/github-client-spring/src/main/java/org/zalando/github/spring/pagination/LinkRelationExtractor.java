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

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

/**
 * 
 * @author jbellmann
 *
 */
public class LinkRelationExtractor {

	private static final Splitter splitter = Splitter.on(",");
	private static final Function<String, LinkRelation> transformer = new StringToLinkRelation();

	public Optional<LinkRelation> extractLinkRelation(String linkHeaderValue, String relation) {
		Iterable<String> splittedHeaderIterable = splitter.split(linkHeaderValue);
		Iterable<LinkRelation> linkRelations = Iterables.transform(splittedHeaderIterable, transformer);
		linkRelations = Iterables.filter(linkRelations, new RelationsPredicate(relation));
		return Optional.fromNullable(Iterables.getFirst(linkRelations, null));
	}

}
