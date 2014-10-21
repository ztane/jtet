package com.anttipatterns.jtet.view;

import com.anttipatterns.jtet.request.IRequest;

public interface IViewCallable {
	public Object handle(IRequest request);
}
