package com.anttipatterns.jtet.handler;

import com.anttipatterns.jtet.request.IRequest;
import com.anttipatterns.jtet.response.IResponse;

public interface IHandler {
	public IResponse handle(IRequest request);
}
