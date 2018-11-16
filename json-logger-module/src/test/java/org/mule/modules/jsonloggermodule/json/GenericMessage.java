package org.mule.modules.jsonloggermodule.json;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
    "type", "message"
})
public class GenericMessage extends Message {
	
	@JsonProperty
	private String type;

	@JsonProperty
	private String message;

	public GenericMessage(String message) {
		super();
		this.type = "GENERIC";
		this.message = message;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public static GenericMessageBuilder builder() {
		return new GenericMessageBuilder();
	}
	public static class GenericMessageBuilder {
		private String message = null;
		private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	    
	    GenericMessageBuilder() {
	    }
	    
	    public GenericMessageBuilder message(String message) {
	    	this.message = message;
    		return this;
	    }
		public GenericMessageBuilder additionalProperties(String name, Object value) {
			this.additionalProperties.put(name, value);
			return this;
		}
		public GenericMessage build() {
			GenericMessage genericMessage = new GenericMessage(message);
			if(!additionalProperties.isEmpty()) {
				additionalProperties.forEach((name,value) -> genericMessage.setAdditionalProperty(name, value));
			}
			return genericMessage;
		}
	}
}
