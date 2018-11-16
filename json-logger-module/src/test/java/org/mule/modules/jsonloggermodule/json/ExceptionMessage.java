package org.mule.modules.jsonloggermodule.json;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
    "type", "message", "cause", "stacktrace"
})
public class ExceptionMessage extends Message {
	
	@JsonProperty
	private String type;

	@JsonProperty
	private String message;

	@JsonProperty
	private String cause;

	@JsonProperty
	private String stacktrace;

	public ExceptionMessage(String message, String cause, String stacktrace) {
		super();
		this.type = "EXCEPTION";
		this.message = message;
		this.cause = cause;
		this.stacktrace = stacktrace;
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
	
	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	public String getStacktrace() {
		return stacktrace;
	}

	public void setStacktrace(String stacktrace) {
		this.stacktrace = stacktrace;
	}

	public static ExceptionMessageBuilder builder() {
		return new ExceptionMessageBuilder();
	}
	public static class ExceptionMessageBuilder {
		private String message = null;
		private String cause = null;
		private String stacktrace = null;
		private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	    
		ExceptionMessageBuilder() {
	    }
	    
	    public ExceptionMessageBuilder message(String message) {
	    	this.message = message;
    		return this;
	    }
	    public ExceptionMessageBuilder cause(String cause) {
	    	this.cause = cause;
    		return this;
	    }
	    public ExceptionMessageBuilder stacktrace(String stacktrace) {
	    	this.stacktrace = stacktrace;
    		return this;
	    }
	    public ExceptionMessageBuilder throwable(Throwable throwable) {
	    	if(throwable != null) {
	    		try {
	    			this.message = ExceptionUtils.getMessage(throwable);
	    	        this.cause = ExceptionUtils.getRootCauseMessage(throwable);
	    	        this.stacktrace = ExceptionUtils.getStackTrace(throwable);
	    		} catch(Exception e) {
	    			
	    		}
	    	}
	    	return this;
	    }
		public ExceptionMessageBuilder additionalProperties(String name, Object value) {
			this.additionalProperties.put(name, value);
			return this;
		}
		public ExceptionMessage build() {
			ExceptionMessage exceptionMessage = new ExceptionMessage(message, cause, stacktrace);
			if(!additionalProperties.isEmpty()) {
				additionalProperties.forEach((name,value) -> exceptionMessage.setAdditionalProperty(name, value));
			}
			return exceptionMessage;
		}
	}
}
