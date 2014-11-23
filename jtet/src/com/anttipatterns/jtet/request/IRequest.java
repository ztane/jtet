package com.anttipatterns.jtet.request;

import javax.servlet.http.HttpServletResponse;

import com.anttipatterns.jtet.config.Registry;
import com.anttipatterns.jtet.props.IPropSupport;
import com.anttipatterns.jtet.response.IResponse;

public interface IRequest extends IPropSupport<IRequest> {
	public abstract String getPathInfo();
	public abstract IResponse getResponse();
	public abstract HttpServletResponse getBackingResponse();
	public abstract Registry getRegistry();
}