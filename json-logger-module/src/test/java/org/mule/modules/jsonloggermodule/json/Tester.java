package org.mule.modules.jsonloggermodule.json;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import static org.junit.Assert.assertEquals;

public class Tester {
	 
	//@Test
	public void testExceptionMessage() throws JsonProcessingException {
		try {
			throw new IllegalArgumentException("Not allowed here", new NullPointerException("Value is null"));
		} catch (Exception e) {
			ExceptionMessage exceptionMessage = ExceptionMessage.builder().throwable(e).build();
			System.out.println(exceptionMessage.getMessage());
			System.out.println(exceptionMessage.getCause());
			System.out.println(exceptionMessage.getStacktrace());
			
			ObjectMapper om = new ObjectMapper();
			om.enable(SerializationFeature.INDENT_OUTPUT);
			System.out.println(om.writeValueAsString(exceptionMessage));
		}
	}
}
