package com.anttipatterns.jtet.response;

import com.anttipatterns.jtet.request.IRequest;

public class HttpInternalServerError extends HttpException {
	private static final long serialVersionUID = 1L;

	public HttpInternalServerError(IRequest request) {
		super(request);
		setStatus(500);
		setBody("Error 500 - Internal server error");
	}
}
