package com.anttipatterns.jtet;

import static com.anttipatterns.jtet.ca.ComponentRegistry.arguments;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.anttipatterns.jtet.config.Registry;
import com.anttipatterns.jtet.handler.IHandler;
import com.anttipatterns.jtet.request.IRequest;
import com.anttipatterns.jtet.request.Request;
import com.anttipatterns.jtet.response.HttpException;
import com.anttipatterns.jtet.response.HttpInternalServerError;
import com.anttipatterns.jtet.response.HttpNotFound;
import com.anttipatterns.jtet.response.IResponse;
import com.anttipatterns.jtet.route.Route;
import com.anttipatterns.jtet.tween.ITween;
import com.anttipatterns.jtet.view.View;
import com.google.common.collect.Lists;

public class ApplicationServlet extends HttpServlet {
	private static final long serialVersionUID = -2942740800774905790L;
	private Registry registry;
	private IHandler requestHandler = new InnerHandler();
	
	public ApplicationServlet(Registry registry) {
		this.registry = registry;
	}

	@Override
	public void init() throws ServletException {		
		super.init();
		
		for (ITween tween: Lists.reverse(registry.getTweens())) {
			requestHandler = tween.wrap(requestHandler, registry);
		}
	}
	
	@Override
	public void destroy() {
		super.destroy();
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {			
		doProcess(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doProcess(req, resp);
	}

	private void doProcess(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		IRequest request = new Request(registry, req, resp);
		handle(request);
	}

	protected class InnerHandler implements IHandler {
		public IResponse handle(IRequest request) {
			Route r = registry.getMatchingRoute(request);
			if (r == null) {
				throw new HttpNotFound(request);
			}
			
			Iterator<View> i = r.getViewIterator();
			while (i.hasNext()) {
				View v = i.next();
				Object responseValue = v.handle(request);
				IResponse response = renderResponseValue(request, responseValue, v.getRenderer());
				return response;
			}
			
			throw new HttpNotFound(request);
		}
	}
	
	private void handle(IRequest request) throws IOException {
		IResponse response;
		
		try {
			response = requestHandler.handle(request);
			response.writeTo(request.getBackingResponse());
			return;
		}
		catch (HttpException e) {
			e.writeTo(request.getBackingResponse());
		}
		catch (Exception e) {
			System.err.println(e);
			response = new HttpInternalServerError(request);
			response.writeTo(request.getBackingResponse());
		}
	}

	protected IResponse renderResponseValue(IRequest request, Object responseValue,
			String renderer) {
		if (responseValue == null) {
			throw new RuntimeException("Return value from view was null");
		}
		
		if (renderer.isEmpty()) {
			return registry.getAdapter(IResponse.class, arguments(request, responseValue));
		}

		throw new RuntimeException("Not yet able to handle non-empty renderers");	
	}
};
