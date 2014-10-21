package com.anttipatterns.jtet.view;

import com.anttipatterns.jtet.request.IRequest;

public class NullViewMapper implements IViewCallable {
	@Override
	public Context handle(IRequest request) {
		return new Context();
	}
}
