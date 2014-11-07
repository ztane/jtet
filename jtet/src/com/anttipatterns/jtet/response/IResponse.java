package com.anttipatterns.jtet.response;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

public interface IResponse {
	public default void writeTo(HttpServletResponse resp) throws IOException {
		resp.setStatus(getStatus());
		resp.setContentType(getContentType());
		
		String body = this.getBody();
		if (body != null) {
			PrintWriter writer = resp.getWriter();
			writer.write(body);
		}

		resp.flushBuffer();
	}

	public abstract String getBody();

	public abstract int getStatus();

	public abstract void setBody(String body);

	public abstract String getEncoding();

	public abstract void setEncoding(String encoding);

	public abstract String getContentType();

	public abstract void setContentType(String contentType);

	public abstract void setStatus(int i);
}