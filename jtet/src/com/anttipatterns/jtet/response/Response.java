package com.anttipatterns.jtet.response;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

public class Response implements IResponse {
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

	/* (non-Javadoc)
	 * @see com.anttipatterns.jtet.response.IResponse#writeTo(javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void writeTo(HttpServletResponse resp) throws IOException {
		resp.setStatus(status);
		resp.setContentType(contentType);
		
		if (body != null) {
			PrintWriter writer = resp.getWriter();
			writer.write(body);
		}
		
		resp.flushBuffer();
	}

	/* (non-Javadoc)
	 * @see com.anttipatterns.jtet.response.IResponse#setBody(java.lang.String)
	 */
	@Override
	public void setBody(String body) {
		this.body = body;
	}

	/* (non-Javadoc)
	 * @see com.anttipatterns.jtet.response.IResponse#getEncoding()
	 */
	@Override
	public String getEncoding() {
		return encoding;
	}

	/* (non-Javadoc)
	 * @see com.anttipatterns.jtet.response.IResponse#setEncoding(java.lang.String)
	 */
	@Override
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/* (non-Javadoc)
	 * @see com.anttipatterns.jtet.response.IResponse#getContentType()
	 */
	@Override
	public String getContentType() {
		return contentType;
	}

	/* (non-Javadoc)
	 * @see com.anttipatterns.jtet.response.IResponse#setContentType(java.lang.String)
	 */
	@Override
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public HttpServletResponse getBackingResponse() {
		return backingResponse;
	}
}
