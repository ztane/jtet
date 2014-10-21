package com.anttipatterns.jtet.utils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class ClassUtils {
	private static void getAllSupers(Class<?> cls,
			final LinkedHashSet<Class<?>> interfacesFound) {
		while (cls != null) {
			final Class<?>[] interfaces = cls.getInterfaces();
			for (final Class<?> i : interfaces) {
				if (interfacesFound.add(i)) {
					getAllSupers(i, interfacesFound);
				}
			}
			
			cls = cls.getSuperclass();
			if (cls != null) {
				interfacesFound.add(cls);
			}
		}
	}
	
	public static List<Class<?>> getAllSupers(Class<?> cls) {
		if (cls == null) {
			return null;
		}
		final LinkedHashSet<Class<?>> found = new LinkedHashSet<Class<?>>();
		getAllSupers(cls, found);
		return new ArrayList<>(found);
	}
}
