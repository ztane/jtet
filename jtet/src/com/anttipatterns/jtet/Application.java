package com.anttipatterns.jtet;

import javax.servlet.Servlet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.anttipatterns.jtet.config.Registry;

public class Application {
	private Registry registry;

	public Application(Registry registry) {
		this.registry = registry;
	}
	
	public Servlet makeServlet() {
		return new ApplicationServlet(registry);
	}
	
	public void serve(int port) {
	    Server server = new Server(port);

	    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
	    context.setContextPath("/");
	    server.setHandler(context);
	
	    context.addServlet(new ServletHolder(makeServlet()), "/*");

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
