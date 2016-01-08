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

import java.net.URI;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestOperations;

import com.google.common.base.Optional;

/**
 * 
 * @author jbellmann
 *
 * @param <E>
 */
public class PagingIterator<E> implements Iterator<E> {

	private static final LinkRelationExtractor linkRelationExtractor = new LinkRelationExtractor();

	private final RestOperations restOperations;
	private URI uri;
	private Class<E> responseType;
	private ParameterizedTypeReference<E> parameterizedTypeReference;

	private E next;

	public PagingIterator(RestOperations restOperations, URI uri, Class<E> responseType) {
		Assert.notNull(restOperations, "restOperations should not be null");
		Assert.notNull(uri, "URI should not be null");
		Assert.notNull(responseType, "ResponseType should never be null");
		this.restOperations = restOperations;
		this.uri = uri;
		this.responseType = responseType;
	}

	public PagingIterator(RestOperations restOperations, URI uri,
			ParameterizedTypeReference<E> parameterizedTypeReference) {
		Assert.notNull(restOperations, "restOperations should not be null");
		Assert.notNull(uri, "URI should not be null");
		Assert.notNull(parameterizedTypeReference, "ParameteriedTypeReference should never be null");
		this.restOperations = restOperations;
		this.uri = uri;
		this.parameterizedTypeReference = parameterizedTypeReference;
	}

	@Override
	public boolean hasNext() {
		fetchFromGithub();
		return next != null;
	}

	@Override
	public E next() {
		fetchFromGithub();
		E r = next;
		if (r == null) {
			throw new NoSuchElementException();
		}
		next = null;
		return r;
	}

	protected void fetchFromGithub() {
		if (next != null) {
			return;
		}
		if (uri == null) {
			return;
		}

		ResponseEntity<E> entity = getReponseEntity();
		List<String> headerValues = entity.getHeaders().get("Link");
		if (headerValues == null) {
			uri = null;
		} else {
			String first = headerValues.get(0);
			if (StringUtils.hasText(first)) {
				Optional<LinkRelation> linkRelation = linkRelationExtractor.extractLinkRelation(first, LinkRelation.NEXT);
				if (linkRelation.isPresent()) {
					uri = URI.create(linkRelation.get().getLink());
				}else{
					uri = null;
				}
			}
		}

		next = entity.getBody();
	}

	protected ResponseEntity<E> getReponseEntity() {
		if (responseType != null) {
			return restOperations.exchange(uri, HttpMethod.GET, null, responseType);
		} else {
			return restOperations.exchange(uri, HttpMethod.GET, null, parameterizedTypeReference);
		}
	}

}
