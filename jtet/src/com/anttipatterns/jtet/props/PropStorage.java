package com.anttipatterns.jtet.props;

import java.util.Map;

public class PropStorage<Target> {
	private Map<Key<Target, ?>, Object> map;
	private Object container;
	
	public PropStorage(Target container) {
		this.container = container;
	}
	
	public <T> T get(Key<Target, T> key) throws NoSuchPropertyException {
		Object value = map.get(key);
		if (value == null) {
			throw new NoSuchPropertyException(String.format("No element %s in %s", key, container));
		}
		return key.cast(value);
	}
	
	public <T> void set(Key<Target, T> key, T value) {
		map.put(key, value);
	}
}
