package com.anttipatterns.jtet.props;

public interface IPropSupport<Target extends IPropSupport<?>> {
    default public <T> T queryProp(Key<Target, T> key) {
		return queryProp(key, null);
	}

	default public <T> T queryProp(Key<Target, T> key, T defaultValue) {
		try {
			return getProp(key);
		} catch (NoSuchPropertyException e) {
			return defaultValue;
		}
	}

	public <T> T getProp(Key<Target, T> key) throws NoSuchPropertyException;
	public <T> void setProp(Key<Target, T> key, T value);
}
