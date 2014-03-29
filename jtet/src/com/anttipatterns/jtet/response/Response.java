package com.anttipatterns.jtet.response;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

public class Response {
	private HttpServletResponse backingResponse;
	private int status = 200;
	private String body;
	private String encoding = "UTF-8";
	private String contentType = "text/html;charset=UTF-8"; 
	
	public Response() {
		
	}

	public Response(HttpServletResponse response) {
		this.backingResponse = response;
	}

	public void writeTo(HttpServletResponse resp) throws IOException {
		resp.setStatus(status);
		resp.setContentType(contentType);
		
		if (body != null) {
			PrintWriter writer = resp.getWriter();
			writer.write(body);
		}
		
		resp.flushBuffer();
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
}
