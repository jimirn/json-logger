package org.mule.modules.jsonloggermodule.json;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
    "type", "name"
})
public class HttpRequestMessage extends Message {
	
	@JsonProperty
	private String type;

	@JsonProperty
	private String name;
	
	public HttpRequestMessage(String name) {
		super();
		this.type = "HTTP_REQUEST";
		this.name = name;
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
	
	public static HttpRequestMessageBuilder builder() {
		return new HttpRequestMessageBuilder();
	}
	public static class HttpRequestMessageBuilder {
		private String name;
		private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	    
		HttpRequestMessageBuilder() {
	    }
		public HttpRequestMessageBuilder name(String name) {
			this.name = name;
			return this;
		}
		public HttpRequestMessageBuilder additionalProperties(String name, String value) {
			this.additionalProperties.put(name, value);
			return this;
		}
		public HttpRequestMessage build() {
			HttpRequestMessage httpRequestMessage = new HttpRequestMessage(name);
			if(!additionalProperties.isEmpty()) {
				additionalProperties.forEach((name,value) -> httpRequestMessage.setAdditionalProperty(name, value));
			}
			return httpRequestMessage;
		}
	}
}
