package com.anttipatterns.jtet.config;

public class NameConflictException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public NameConflictException(String msg) {
    	super(msg);
    }
}