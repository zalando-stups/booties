package org.zalando.github.spring.pagination;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestOperations;

public class PaginationTest {

	private final ParameterizedTypeReference<List<String>> issueListTypeRef = new ParameterizedTypeReference<List<String>>() {
	};

	private final URI uri = URI.create("https://api.github.com/organizations/12345678/issues?per_page=1");

	private final String LINK_HEADER = "<https://api.github.com/organizations/12345678/issues?per_page=1&page=2>; rel=\"next\", <https://api.github.com/organizations/12345678/issues?per_page=1&page=3>; rel=\"last\"";
	private final String LINK_HEADER_2 = "<https://api.github.com/organizations/12345678/issues?per_page=1&page=3>; rel=\"next\", <https://api.github.com/organizations/12345678/issues?per_page=1&page=3>; rel=\"last\"";
	private final String LINK_HEADER_LAST = "<https://api.github.com/organizations/12345678/issues?per_page=3&page=2>; rel=\"prev\"";

	@SuppressWarnings("unchecked")
	@Test
	public void pagination() {

		RestOperations restOperations = Mockito.mock(RestOperations.class);

		ResponseEntity<List<String>> firstResponse = first();
		ResponseEntity<List<String>> secondResponse = second();
		ResponseEntity<List<String>> thirdResponse = third();

		Mockito.when(restOperations.exchange(Mockito.any(URI.class), Mockito.any(HttpMethod.class),
				Mockito.any(HttpEntity.class), Mockito.any(issueListTypeRef.getClass())))
				.thenReturn(firstResponse, secondResponse, thirdResponse);

		List<String> result = new ArrayList<>();
		PagingIterator<List<String>> iter = new PagingIterator(restOperations, uri, issueListTypeRef);
		while (iter.hasNext()) {
			List<String> next = iter.next();
			result.addAll(next);
		}
		Assertions.assertThat(result.isEmpty()).isFalse();
		Assertions.assertThat(result.size()).isEqualTo(3);
		Assertions.assertThat(result).contains("firstElement", "secondElement", "thirdElement");
	}

	protected ResponseEntity<List<String>> first() {
		ResponseEntity<List<String>> result = Mockito.mock(ResponseEntity.class);
		Mockito.when(result.getHeaders()).thenReturn(buildHeaders(LINK_HEADER));
		Mockito.when(result.getBody()).thenReturn(Collections.singletonList("firstElement"));
		return result;
	}

	protected ResponseEntity<List<String>> second() {
		ResponseEntity<List<String>> result = Mockito.mock(ResponseEntity.class);
		Mockito.when(result.getHeaders()).thenReturn(buildHeaders(LINK_HEADER_2));
		Mockito.when(result.getBody()).thenReturn(Collections.singletonList("secondElement"));
		return result;
	}

	protected ResponseEntity<List<String>> third() {
		ResponseEntity<List<String>> result = Mockito.mock(ResponseEntity.class);
		Mockito.when(result.getHeaders()).thenReturn(buildHeaders(LINK_HEADER_LAST));
		Mockito.when(result.getBody()).thenReturn(Collections.singletonList("thirdElement"));

		return result;
	}

	protected HttpHeaders buildHeaders(String headerValue) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Link", headerValue);
		return headers;
	}
}
