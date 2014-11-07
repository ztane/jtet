package com.anttipatterns.jtet.response;

import com.anttipatterns.jtet.request.IRequest;

public class HttpException extends Exception implements IResponse {
	private static final long serialVersionUID = 1L;
	protected IRequest request;
	private String body;
	private String encoding;
	private String contentType;
	private int status;

	public HttpException(IRequest request) {
		this.request = request;
		this.setStatus(400);
		this.setContentType("text/html");
		this.setEncoding("UTF-8");
	}

	@Override
	public void setBody(String body) {
		this.body = body;
	}

	@Override
	public String getEncoding() {
		return this.encoding;
	}

	@Override
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	@Override
	public String getContentType() {
		return this.contentType;
	}

	@Override
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	@Override
	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String getBody() {
		return body;
	}

	@Override
	public int getStatus() {
		return status;
	}

}
