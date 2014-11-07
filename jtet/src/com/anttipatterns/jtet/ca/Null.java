package com.anttipatterns.jtet.ca;

public class Null {
	public static final Null NULL = new Null();
	private Null() { }

	@Override
	public String toString() {
		return "(null)";
	}
}
