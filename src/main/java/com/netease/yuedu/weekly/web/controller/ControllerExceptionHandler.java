package com.netease.yuedu.weekly.web.controller;

import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {
	
	private static Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

	@ExceptionHandler(ParseException.class)
	public String handleParseException(ParseException e) {
		logError(e);
		return "redirect:/error/400.html";
	}
	
	@ExceptionHandler(TypeMismatchException.class)
	public String handleNumberFormatException(TypeMismatchException e) {
		logError(e);
		return "redirect:/error/400.html";
	}
	
	@ExceptionHandler(Exception.class)
	public String handleUnknownException(Exception e) {
		logError(e);
		return "redirect:/error/500.html";
	}
	
	private void logError(Exception e) {
		logger.error("", e);
	}
}
