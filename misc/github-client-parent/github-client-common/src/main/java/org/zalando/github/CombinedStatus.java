package org.zalando.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CombinedStatus {

	private String state;

	private String sha;

	private Long totalCount;
}
