package com.anttipatterns.jtet.adapter;


public class UnadaptableException extends Exception {
	public UnadaptableException(IAdaptable adaptable, Class<?> type,
			String name) {
		super(String.format(
			"%s cannot be adapted to %s (name %s)",
			adaptable, type.getName(), name
		));
	}

	private static final long serialVersionUID = -6087999706265691323L;
}
