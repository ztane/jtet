package com.anttipatterns.jtet.renderers;

import com.anttipatterns.jtet.config.Registry;
import com.anttipatterns.jtet.settings.ISettings;

public class RendererHelper implements IRendererInfo {
	private final String name;
	private final String pkg;
	private final Registry registry;
	private final Class<?> annotatedClass;

	public RendererHelper(String name, Class<?> annotatedClass, Registry registry) {
		this.name = name;
		this.annotatedClass = annotatedClass;
		this.pkg = annotatedClass.getPackage().getName();
		this.registry = registry;
	}
	
	private volatile IRenderer renderer = null;
	public synchronized IRenderer getRenderer() {
		if (renderer == null) {
			IRendererFactory factory = registry.getUtility(IRendererFactory.class, name);
			renderer = factory.create(this);
		}
		return renderer;
	}
	
	@Override
	public String getName() {
		return name;
	}
	@Override
	public String getPackage() {
		return pkg;
	}
	
	@Override
	public Class<?> getAnnotatedClass() {
		return annotatedClass;
	}
	
	@Override
	public Registry getRegistry() {
		return registry;
	}
	@Override
	public ISettings getSettings() {
		return registry.getSettings();
	}
}
