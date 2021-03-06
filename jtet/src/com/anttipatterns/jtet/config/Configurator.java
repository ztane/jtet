package com.anttipatterns.jtet.config;

import static com.anttipatterns.jtet.ca.ComponentRegistry.argTypes;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import com.anttipatterns.jtet.Application;
import com.anttipatterns.jtet.adapter.IAdaptable;
import com.anttipatterns.jtet.annotations.JTetAnnotation;
import com.anttipatterns.jtet.persistence.ISessionFactory;
import com.anttipatterns.jtet.persistence.SessionFactory;
import com.anttipatterns.jtet.request.IRequest;
import com.anttipatterns.jtet.response.IResponse;
import com.anttipatterns.jtet.route.Route;
import com.anttipatterns.jtet.route.Route.RouteConfigurator;
import com.anttipatterns.jtet.tween.ITween;
import com.anttipatterns.jtet.view.View;
import com.anttipatterns.jtet.view.View.ViewConfigurator;
import com.anttipatterns.jtet.view.ViewConfig;
import com.anttipatterns.jtet.view.ViewConfigs;

public class Configurator implements IAdaptable {
	private List<Route> uncommittedRoutes = new ArrayList<>();
	private List<View> uncommittedViews = new ArrayList<>();
	private boolean committed = true;
	private Registry registry;
	
	public Configurator() {
		registry = new Registry();
		setDefaults(registry);
	}
	
	public Configurator(Registry registry) {
		this.registry = registry;
		setDefaults(registry);
	}
	
	protected void setDefaults(Registry registry) {
		registry.components().registerAdapter(
			IResponse.class,
			argTypes(IRequest.class, String.class),
			(request, body) -> {
				IResponse rv = request.getResponse();
				rv.setBody(body);
				rv.setEncoding("UTF-8");
				rv.setContentType("text/html");
				return rv;
			},
			""
		);
	}

	public RouteConfigurator addRoute(String name) {
		Route r = new Route(name);
		uncommittedRoutes.add(r);
		committed = false;
		return r.getConfigurator();
	}
	
	public ViewConfigurator addView(Object viewCallable) {
		View v = new View(viewCallable);
		uncommittedViews.add(v);
		committed = false;
		return v.getConfigurator();
	}
	
	public Application createApplication() {
		commit();
		
		return new Application(registry);
	}

	public void configurePersistence(String persistenceName) {
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(persistenceName);
		ISessionFactory sessions = new SessionFactory(entityManagerFactory);
		registry.setProp(com.anttipatterns.jtet.persistence.Persistence.SESSION_FACTORY, sessions);
	}
	
	public void addTween(ITween tween) {
		registry.tweens.add(tween);
	}
	
	public void commit() {
		if (committed) {
			return;
		}
		
		for (Route r: uncommittedRoutes) {
			registry.addRoute(r);
		}
		uncommittedRoutes.clear();
		for (View v: uncommittedViews) {
			registry.addView(v);
		}
		uncommittedViews.clear();
		committed = true;
	}

	public void scan(String thePackage) {
	     Reflections reflections = new Reflections(new ConfigurationBuilder()
	     	.filterInputsBy(new FilterBuilder().includePackage(thePackage))
	     	.setUrls(ClasspathHelper.forPackage(thePackage))
	     	.setScanners(new SubTypesScanner(), new TypeAnnotationsScanner(), new MethodAnnotationsScanner()));

	     Collection<Method> views = reflections.getMethodsAnnotatedWith(ViewConfigs.class);
	     for (Method m: views) {
	    	 ViewConfigs vcs = m.getAnnotation(ViewConfigs.class);
	    	 for (ViewConfig vc: vcs.value()) {
	    		 addView(m, vc);
	    	 }	
	     }

	     views = reflections.getMethodsAnnotatedWith(ViewConfig.class);
	     for (Method m: views) {
    		 addView(m, m.getAnnotation(ViewConfig.class));
	     }
	}

	private void addView(Method m, ViewConfig vc) {
	   	 ViewConfigurator c = addView(m);
	
	   	 if (vc.renderer() != JTetAnnotation.NULL) {
	   		 c.renderer(vc.renderer());
	   	 }
	   	 
	   	 if (vc.routeName() != JTetAnnotation.NULL) {
	   		 c.routeName(vc.routeName());
	   	 }
	}
}
