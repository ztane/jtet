package com.anttipatterns.jtet.request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.anttipatterns.jtet.adapter.IAdaptable;
import com.anttipatterns.jtet.props.IPropSupport;
import com.anttipatterns.jtet.props.Key;
import com.anttipatterns.jtet.props.NoSuchPropertyException;
import com.anttipatterns.jtet.props.PropStorage;
import com.anttipatterns.jtet.response.IResponse;
import com.anttipatterns.jtet.response.Response;

public class Request implements IRequest, IAdaptable, IPropSupport<Request> {
	private String pathInfo;
	private volatile IResponse response;
	private HttpServletResponse servletResponse;

	public Request() {
		
	}
	
	public Request(HttpServletRequest req) {
		setFromRequest(req);
	}

	public Request(HttpServletRequest req, HttpServletResponse resp) {
		setFromRequest(req);
		this.servletResponse = resp;
	}

	private void setFromRequest(HttpServletRequest req) {
		pathInfo = req.getPathInfo();
		if (pathInfo == null) {
			pathInfo = "/";
		}
		if (! pathInfo.startsWith("/")) {
			pathInfo = "/" + pathInfo;
		}		
	}

	@Override
	public String getPathInfo() {
		return pathInfo;
	}

	@Override
	public IResponse getResponse() {
		if (response == null) {
			synchronized(this) {
				if (response == null) {
					response = new Response(servletResponse);
				}
			}
		}
		return response;
	}

	private PropStorage<Request> props = new PropStorage<>(this);
	public <T> T getProp(Key<Request, T> key) throws NoSuchPropertyException {
		return props.get(key);
	}

	public <T> void setProp(Key<Request, T> key, T value) {
		props.set(key, value);
	}

	@Override
	public HttpServletResponse getBackingResponse() {
		return servletResponse;
	}
}
