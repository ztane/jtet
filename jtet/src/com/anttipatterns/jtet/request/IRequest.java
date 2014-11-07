package com.anttipatterns.jtet.request;

import javax.servlet.http.HttpServletResponse;

import com.anttipatterns.jtet.response.IResponse;

public interface IRequest {
	public abstract String getPathInfo();
	public abstract IResponse getResponse();
	public abstract HttpServletResponse getBackingResponse();
}