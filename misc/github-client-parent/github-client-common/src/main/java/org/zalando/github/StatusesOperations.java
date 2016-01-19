package org.zalando.github;

import java.util.List;

public interface StatusesOperations {

	Status createStatus(String owner, String repository, String sha, StatusRequest body);

	List<Status> listStatuses(String ref);
	
	CombinedStatus getCombinedStatus(String owner, String repository, String ref);

}
