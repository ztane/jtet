JTet - an intelligent pyramid-like web framework for Java
=========================================================

To build
--------

You need the following libraries as requisites:

  * guava-16.0.1.jar
  * javassist-3.12.1.GA.jar
  * javax.servlet-api-3.1.0.jar
  * jetty-all-9.1.3.v20140225.jar
  * reflections-0.9.9-RC1.jar
  * slf4j-api-1.7.6.jar
  * slf4j-simple-1.7.6.jar

In Eclipse, you can create a project called jtet-requisites (referenced automatically)
to contain these requisites.

To run
------

Nothing much is completed yet, but the following program does "work", returning a text/html page from
http://localhost:5321/

	package com.anttipatterns.jtet.test;
	
	import com.anttipatterns.jtet.Application;
	import com.anttipatterns.jtet.config.Configurator;
	
	public class TestApplication {
		public static void main(String[] args) {
			Configurator config = new Configurator();
			config.addRoute("foo").pattern("/");
			config.addRoute("bar").pattern("/{abc}");
			config.addRoute("baz").pattern("/{abc}/{def:ghi*}");
	
			config.scan("com.anttipatterns.jtet.test.views");
			
			Application application = config.createApplication();
			application.serve(54321);
		}
	}

	package com.anttipatterns.jtet.test.views;

	import com.anttipatterns.jtet.request.Request;
	import com.anttipatterns.jtet.view.ViewConfig;
	
	public class ViewTest {
		@ViewConfig(routeName="foo")
		public static String foobar(Request request) {
			return "Hello world";
		}
	}
