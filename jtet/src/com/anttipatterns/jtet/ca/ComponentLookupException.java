package com.anttipatterns.jtet.ca;

public class ComponentLookupException extends RuntimeException {
	private static final long serialVersionUID = 1184665841111136023L;

	private String name;
	private Object adapted;
	private Class<?> adapterInterface;

	public ComponentLookupException(Class<?> adapterInterface, Object adapted,
			String name) {
		super(
				String.format("Unable to adapt %s to %s (name \"%s\")",
						adapted, adapterInterface.getSimpleName(), name));

		this.adapterInterface = adapterInterface;
		this.adapted = adapted;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Object getAdapted() {
		return adapted;
	}

	public Class<?> getAdapterInterface() {
		return adapterInterface;
	}
}
