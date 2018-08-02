package org.mule.modules.jsonloggermodule.json;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
    "type", "name"
})
public class HttpRequestConsumerMessage extends Message {
	
	@JsonProperty
	private String type;

	@JsonProperty
	private String name;
	
	public HttpRequestConsumerMessage(String name) {
		super();
		this.type = "HTTP_REQUEST_CONSUMER";
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
	
	public static HttpRequestConsumerMessageBuilder builder() {
		return new HttpRequestConsumerMessageBuilder();
	}
	public static class HttpRequestConsumerMessageBuilder {
		private String name;
		private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	    
		HttpRequestConsumerMessageBuilder() {
	    }
		public HttpRequestConsumerMessageBuilder name(String name) {
			this.name = name;
			return this;
		}
		public HttpRequestConsumerMessageBuilder additionalProperties(String name, String value) {
			this.additionalProperties.put(name, value);
			return this;
		}
		public HttpRequestConsumerMessage build() {
			HttpRequestConsumerMessage httpRequestConsumerMessage = new HttpRequestConsumerMessage(name);
			if(!additionalProperties.isEmpty()) {
				additionalProperties.forEach((name,value) -> httpRequestConsumerMessage.setAdditionalProperty(name, value));
			}
			return httpRequestConsumerMessage;
		}
	}
}
