package org.zalando.github;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class StatusRequest {

	private String state;

	@JsonProperty("target_url")
	private String targetUrl;

	private String description;

	private String context = "default";

	protected StatusRequest(String state) {
		this.state = state;
	}

	public static StatusRequest pendingStatusRequest() {
		return new StatusRequest("pending");
	}

	public static StatusRequest successStatusRequest() {
		return new StatusRequest("success");
	}

	public static StatusRequest errorStatusRequest() {
		return new StatusRequest("error");
	}

	public static StatusRequest failureStatusRequest() {
		return new StatusRequest("failure");
	}
}
