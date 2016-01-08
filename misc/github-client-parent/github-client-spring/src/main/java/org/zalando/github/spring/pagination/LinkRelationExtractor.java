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
