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
