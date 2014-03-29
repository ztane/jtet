package com.anttipatterns.jtet;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.anttipatterns.jtet.config.Registry;
import com.anttipatterns.jtet.request.Request;
import com.anttipatterns.jtet.response.Response;
import com.anttipatterns.jtet.route.Route;
import com.anttipatterns.jtet.view.View;

public class Application {
	private Registry registry;

	public Application(Registry registry) {
		this.registry = registry;
	}

	private class ApplicationServlet extends HttpServlet {
		private static final long serialVersionUID = -2942740800774905790L;
		
		@Override
		protected void doGet(HttpServletRequest req, HttpServletResponse resp)
				throws ServletException, IOException {
			
			Request request = new Request(req, resp);
			Route r = registry.getMatchingRoute(request);
			Iterator<View> i = r.getViewIterator();
			while (i.hasNext()) {
				View v = i.next();
				Object returnValue = v.handle(request);
				
				if (returnValue != null && returnValue instanceof String) {
					Response response = request.getResponse();
					response.setBody((String)returnValue);
					response.writeTo(resp);
				}
				
				else {
					if (returnValue == null) {
						throw new RuntimeException("Return value from view was null");
					}
					throw new RuntimeException("Unable to handle responses of type " + returnValue.getClass());
				}
				
				break;
			}
		}
	};
	
	public void serve(int port) {
	    Server server = new Server(port);
    
	    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
	    context.setContextPath("/");
	    server.setHandler(context);
	
	    context.addServlet(new ServletHolder(new ApplicationServlet()), "/*");
	
	    try {
			server.start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	    try {
			server.join();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
