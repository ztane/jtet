package com.anttipatterns.jtet.view;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import com.anttipatterns.jtet.request.Request;

public class MethodViewMapper implements IViewMapper {
	IViewMapper methodWrapper;
	
	public MethodViewMapper(final Method viewCallable) {
		viewCallable.setAccessible(true);
		if (Modifier.isStatic(viewCallable.getModifiers())) {
			Class<?>[] types = viewCallable.getParameterTypes();
			
			if (types.length != 1 || types[0] != Request.class) {
				throw new RuntimeException("unable to map this method");				
			}
			
			setMethodWrapper(new IViewMapper() {
				@Override
				public Object handle(Request request) {
					try {
						return viewCallable.invoke(null, new Object[] { request });
					}
					catch (ReflectiveOperationException e) {
						throw new RuntimeException("Exception when calling the view", e);
					}
				}
			}); 
		}
		else {
			throw new RuntimeException("unable to map this method");
		}
	}
	
	private void setMethodWrapper(IViewMapper methodWrapper) {
		this.methodWrapper = methodWrapper;
	}

	@Override
	public Object handle(Request request) {
		return methodWrapper.handle(request);
	}
}
