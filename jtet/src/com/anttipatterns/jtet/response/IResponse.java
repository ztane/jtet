package com.anttipatterns.jtet.response;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

public interface IResponse {

	public abstract void writeTo(HttpServletResponse resp) throws IOException;

	public abstract void setBody(String body);

	public abstract String getEncoding();

	public abstract void setEncoding(String encoding);

	public abstract String getContentType();

	public abstract void setContentType(String contentType);

}