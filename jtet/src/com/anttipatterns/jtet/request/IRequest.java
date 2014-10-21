package com.anttipatterns.jtet.request;

import com.anttipatterns.jtet.response.IResponse;

public interface IRequest {
	public abstract String getPathInfo();
	public abstract IResponse getResponse();
}