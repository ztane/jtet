package com.anttipatterns.jtet.request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.anttipatterns.jtet.adapter.IAdaptable;
import com.anttipatterns.jtet.config.Registry;
import com.anttipatterns.jtet.props.IPropSupport;
import com.anttipatterns.jtet.props.Key;
import com.anttipatterns.jtet.props.NoSuchPropertyException;
import com.anttipatterns.jtet.props.PropStorage;
import com.anttipatterns.jtet.response.IResponse;
import com.anttipatterns.jtet.response.Response;

public class Request implements IRequest, IAdaptable {
	private String pathInfo;
	private volatile IResponse response;
	private HttpServletResponse servletResponse;
	private Registry registry;

	public Request(Registry registry) {
		this.registry = registry;
	}
	
	public Request(Registry registry, HttpServletRequest req) {
		this.registry = registry;
		setFromRequest(req);
	}

	public Request(Registry registry, HttpServletRequest req, HttpServletResponse resp) {
		this.registry = registry;
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

	private PropStorage<IRequest> props = new PropStorage<>(this);
	public <T> T getProp(Key<IRequest, T> key) throws NoSuchPropertyException {
		return props.get(key);
	}

	public <T> void setProp(Key<IRequest, T> key, T value) {
		props.set(key, value);
	}

	@Override
	public HttpServletResponse getBackingResponse() {
		return servletResponse;
	}

	@Override
	public Registry getRegistry() {
		return registry;
	}
}
