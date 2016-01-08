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
