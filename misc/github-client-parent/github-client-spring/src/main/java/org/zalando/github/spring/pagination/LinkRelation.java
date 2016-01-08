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

import com.google.common.base.MoreObjects;

/**
 * Internal representation of an relation.
 * 
 * @author jbellmann
 *
 */
class LinkRelation {

	public static final String NEXT = "rel=\"next\"";

	public static final String PREV = "rel=\"previous\"";

	public static final String LAST = "rel=\"last\"";

	public static final String FIRST = "rel=\"first\"";

	private final String link;

	private final String relation;

	public LinkRelation(String link, String relation) {
		this.link = link;
		this.relation = relation;
	}

	public String getLink() {
		return link;
	}

	public String getRelation() {
		return relation;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("link", link).add("relation", relation).toString();
	}
}
