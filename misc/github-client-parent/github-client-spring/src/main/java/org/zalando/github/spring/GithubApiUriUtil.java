package org.zalando.github.spring;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.util.UriTemplate;

/**
 * 
 * @author jbellmann
 *
 */
public class GithubApiUriUtil {

	public UriTemplate buildUriTemplate(String path) {
		return new UriTemplate(buildUriString(path));
	}

	public URI buildUri(String path, Map<String, Object> uriVariables) {
		return new UriTemplate(buildUriString(path)).expand(uriVariables);
	}

	public URI buildUri(String path) {
		return buildUri(path, new HashMap<String, Object>(0));
	}

	public String buildUriString(String path) {
		return getApiBase() + path;
	}

	public String getApiBase() {
		return "https://api.github.com";
	}

}
