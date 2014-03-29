package com.anttipatterns.jtet.request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.anttipatterns.jtet.response.Response;

public class Request {
	private String pathInfo;
	private volatile Response response;
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

	public String getPathInfo() {
		return pathInfo;
	}
	
	public Response getResponse() {
		if (response == null) {
			synchronized(this) {
				if (response == null) {
					response = new Response(servletResponse);
				}
			}
		}
		return response;
	}
}
