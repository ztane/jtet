package com.anttipatterns.jtet.response;

import com.anttipatterns.jtet.request.IRequest;

public class HttpNotFound extends HttpException {
	private static final long serialVersionUID = 1L;

	public HttpNotFound(IRequest request) {
		super(request);
		setStatus(404);
		setBody("Error 404 - Not found");
	}
}
