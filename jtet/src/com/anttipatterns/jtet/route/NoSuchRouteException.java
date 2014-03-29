package com.anttipatterns.jtet.route;

public class NoSuchRouteException extends RuntimeException {
	public NoSuchRouteException(String message) {
		super(message);
	}
}
