package org.zalando.github;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Status {

	@JsonProperty("created_at")
	private Date createdAt;

	@JsonProperty("updated_at")
	private Date updatedAt;

	private String state;

	@JsonProperty("target_url")
	private String targetUrl;

	private String description;

	private Long id;

	private String url;

	private String context;

	private User creator;

}
