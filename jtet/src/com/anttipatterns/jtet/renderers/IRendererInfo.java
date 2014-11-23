package com.anttipatterns.jtet.renderers;

import com.anttipatterns.jtet.config.Registry;
import com.anttipatterns.jtet.settings.ISettings;

public interface IRendererInfo {
	String getName();
	String getPackage();
	Class<?> getAnnotatedClass();
	Registry getRegistry();
	ISettings getSettings();
}