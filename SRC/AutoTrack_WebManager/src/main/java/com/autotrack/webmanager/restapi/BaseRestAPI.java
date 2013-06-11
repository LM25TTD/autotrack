package com.autotrack.webmanager.restapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public class BaseRestAPI {

	Logger logger = LoggerFactory.getLogger(BaseRestAPI.class);
	
	@ExceptionHandler(Throwable.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Message handleInternalServerError(Throwable exception) {
		logger.error("Occurred a problem during request processing.", exception);
		return new Message(
				"Occurred a problem during your request. Please contact the support team.");
	}
	
		
}