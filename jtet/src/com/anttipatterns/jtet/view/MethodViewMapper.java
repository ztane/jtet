package com.anttipatterns.jtet.view;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import com.anttipatterns.jtet.request.IRequest;

public class MethodViewMapper implements IViewCallable {
	IViewCallable methodWrapper;
	
	public MethodViewMapper(final Method viewCallable) {
		viewCallable.setAccessible(true);
		if (Modifier.isStatic(viewCallable.getModifiers())) {
			Class<?>[] types = viewCallable.getParameterTypes();
			
			if (types.length != 1 || types[0] != IRequest.class) {
				throw new RuntimeException("unable to map this method");				
			}
			
			this.methodWrapper = request -> {
				try {
					return viewCallable.invoke(null, new Object[] { request });
				}
				catch (ReflectiveOperationException e) {
					throw new RuntimeException("Exception when calling the view", e);
				}
			}; 
		}
		else {
			throw new RuntimeException("unable to map this method");
		}
	}
	
	@Override
	public Object handle(IRequest request) {
		return methodWrapper.handle(request);
	}
}
