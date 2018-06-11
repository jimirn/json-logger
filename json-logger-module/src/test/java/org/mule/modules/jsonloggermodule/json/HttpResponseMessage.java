package org.mule.modules.jsonloggermodule.json;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
    "type", "name", "status", "reason", "response"
})
public class HttpResponseMessage extends Message {
	
	@JsonProperty
	private String type;
	@JsonProperty
	private String name;
	@JsonProperty
	private String status;
	@JsonProperty
	private String reason;
	@JsonProperty
	private String response;
	
	public HttpResponseMessage(String name, String status, String reason, String response) {
		super();
		this.type = "HTTP_RESPONSE";
		this.name = name;
		this.status = status;
		this.reason = reason;
		this.response = response;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	
	public static HttpResponseMessageBuilder builder() {
		return new HttpResponseMessageBuilder();
	}
	public static class HttpResponseMessageBuilder {
		private String name;
		private String status;
		private String reason;
		private String response;
		private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	    
		HttpResponseMessageBuilder() {
	    }
		public HttpResponseMessageBuilder name(String name) {
			this.name = name;
			return this;
		}
		public HttpResponseMessageBuilder status(String status) {
			this.status = status;
			return this;
		}
		public HttpResponseMessageBuilder reason(String reason) {
			this.reason = reason;
			return this;
		}
		public HttpResponseMessageBuilder response(String response) {
			this.response = response;
			return this;
		}
		public HttpResponseMessageBuilder additionalProperties(String name, String value) {
			this.additionalProperties.put(name, value);
			return this;
		}
		public HttpResponseMessage build() {
			HttpResponseMessage httpResponseMessage = new HttpResponseMessage(name, status, reason, response);
			if(!additionalProperties.isEmpty()) {
				additionalProperties.forEach((name,value) -> httpResponseMessage.setAdditionalProperty(name, value));
			}
			return httpResponseMessage;
		}
	}
}