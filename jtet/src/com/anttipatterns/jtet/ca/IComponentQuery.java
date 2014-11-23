package com.anttipatterns.jtet.ca;

import com.anttipatterns.jtet.ca.ComponentRegistry.Arguments;

public interface IComponentQuery {

	public abstract <T> T queryAdapter(Class<T> targetClass, Arguments args,
			String name, T defaultValue);

	public abstract <T> T queryAdapter(Class<T> targetClass, Arguments args,
			T defaultValue);

	public abstract <T> T getAdapter(Class<T> targetClass, Arguments args,
			String name);

	public abstract <T> T getAdapter(Class<T> targetClass, Arguments args);

	public abstract <T> T getUtility(Class<T> utility);
	public abstract <T> T getUtility(Class<T> utility, String name);

	public abstract <T> T queryUtility(Class<T> utility, T defaultCValue);
	public abstract <T> T queryUtility(Class<T> utility, String name, T defaultCValue);
}