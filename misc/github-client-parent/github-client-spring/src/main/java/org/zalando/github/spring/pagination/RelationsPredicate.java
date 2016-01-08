package org.zalando.github.spring.pagination;

import org.springframework.util.Assert;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

/**
 * Filter for {@link Iterables} of {@link LinkRelation}
 * 
 * @author jbellmann
 *
 */
class RelationsPredicate implements Predicate<LinkRelation> {

	private final String relation;

	public RelationsPredicate(String relation) {
		Assert.notNull(relation, "'relation' should never be null");
		Assert.hasText(relation, "'relation' should never be empty");
		this.relation = relation;
	}

	@Override
	public boolean apply(LinkRelation input) {
		return relation.equals(input.getRelation());
	}

}