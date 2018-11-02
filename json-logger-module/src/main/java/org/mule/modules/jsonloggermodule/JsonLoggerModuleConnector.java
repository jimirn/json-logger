package org.mule.modules.jsonloggermodule;

import javax.inject.Inject;

import org.apache.commons.beanutils.BeanUtils;
import org.mule.api.MuleEvent;
import org.mule.api.annotations.Config;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.param.Default;
import org.mule.api.annotations.param.Literal;
import org.mule.api.expression.ExpressionManager;
import org.mule.modules.jsonloggermodule.config.AbstractJsonLoggerConfig;
import org.mule.modules.pojos.LoggerProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@Connector(name="json-logger", friendlyName="JSON Logger")
public class JsonLoggerModuleConnector {

    @Config
    AbstractJsonLoggerConfig config;
    
    /**
     * Mule expression manager to resolve MEL expressions
     */
    @Inject
    ExpressionManager expressionManager;
    
    /** 
	 * Create the SLF4J logger
	 * jsonLogger: JSON output log
	 * log: Connector internal log 
	 */
    private static final Logger jsonLogger = LoggerFactory.getLogger("org.mule.modules.JsonLogger");
    private static final Logger log = LoggerFactory.getLogger("org.mule.modules.jsonloggermodule.JsonLoggerModuleConnector");
    
    private static final ObjectMapper om = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
   
    /**
     * Log a new entry
     */
    @Processor
    public void logger(
    		@Literal @Default(value="#[flowVars['timerVariable']]") String timerVariable, 
    		@Literal @Default(value="#[flowVars['timerDeltaVariable']]") String timerDeltaVariable,
    		LoggerProcessor loggerJson, 
    		MuleEvent event) {
    	
    	if (timerVariable == null) timerVariable = "#[flowVars['timerVariable']]";
		if (timerDeltaVariable == null) timerDeltaVariable = "#[flowVars['timerDeltaVariable']]";
		
		Long elapsed = null;
		Long current = System.currentTimeMillis();
		if (!expressionManager.parse(timerVariable, event).equals("null")) {
			elapsed = current - Long.parseLong(expressionManager.parse(timerVariable, event));
		} else {
			expressionManager.enrich(timerVariable, event, current); // Set the variable value to the current timestamp for the next elapsed calculation
		}

		Long elapsedDelta = null;
		if (!expressionManager.parse(timerDeltaVariable, event).equals("null")) {
			Long timerDelta = Long.parseLong(expressionManager.parse(timerDeltaVariable, event));
			elapsedDelta = current - timerDelta;
		}
		//always set timerDeltaVariable to current
		expressionManager.enrich(timerDeltaVariable, event, current);
			
		
		// Define JSON output formatting 
		ObjectWriter ow = null;
		if (config.getPrettyPrint() == true) {
			ow = om.writer().withDefaultPrettyPrinter();
		} else {
			ow = om.writer();
		}
		
		String logLine;
		
		// Copy default values from config into processor object
		try {
			
			if (elapsed != null) {
				loggerJson.setElapsed(Long.toString(elapsed));
			}
			if(elapsedDelta != null) {
				loggerJson.setElapsedDelta(Long.toString(elapsedDelta));
			}
			loggerJson.setThreadName(Thread.currentThread().getName());
			
			BeanUtils.copyProperties(loggerJson, config);
			
			BeanUtils.describe(loggerJson).forEach((k,v) ->{ 
				if (v != null && v.startsWith("#[")) {
					try {
						log.debug("Processing key: " + k);
						BeanUtils.setProperty(loggerJson, k, expressionManager.parse(v, event));
					} catch (Exception e) {
						log.error("Failed parsing expression for key: " + k);
					}
				}
			});	
			logLine = ow.writeValueAsString(loggerJson);
						
			doLog(loggerJson.getPriority().toString(), logLine);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/* Note: 
		 * This was added because when the payload before the JSON Logger is of type ByteArraySeekableStream
		 * (e.g. standard DW output object) and we pass #[payload] as the Message expression, then the stream 
		 * gets consumed but not reset which translates to an "empty stream" for the next processor.  
		 */
		if (event.getMessage().getPayload().getClass().getSimpleName().equals("ByteArraySeekableStream")) {
			log.debug("Payload is a ByteArraySeekableStream. Preemptively resetting the stream...");
			expressionManager.parse("#[payload.seek(0);]", event);
		}
    }
    
    /**
	 * Logs a line through the logging backend
	 * @param severity the severity
	 * @param logLine the line to log
	 */
	private void doLog(String priority, String logLine) {
		switch (priority) {
		case "TRACE":
			jsonLogger.trace(logLine);
			break;
		case "DEBUG":
			jsonLogger.debug(logLine);
			break;
		case "INFO":
			jsonLogger.info(logLine);
			break;
		case "WARN":
			jsonLogger.warn(logLine);
			break;
		case "ERROR":
			jsonLogger.error(logLine);
			break;
		}
	}
		
    public AbstractJsonLoggerConfig getConfig() {
        return config;
    }

    public void setConfig(AbstractJsonLoggerConfig config) {
        this.config = config;
    }
    
    public void setExpressionManager(ExpressionManager expressionManager) {
    	this.expressionManager = expressionManager;
    }
}