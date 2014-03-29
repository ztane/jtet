package com.anttipatterns.jtet.config;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import com.anttipatterns.jtet.request.Request;
import com.anttipatterns.jtet.route.NoSuchRouteException;
import com.anttipatterns.jtet.route.Route;
import com.anttipatterns.jtet.view.View;

public class Registry {
	private Map<String, Object> properties = new HashMap<>();
	private Map<String, Route> routeMapping = new HashMap<>();
	private List<Route> routes = new ArrayList<>();
	private List<View> views = new ArrayList<>();
	
	public void setProperty(String name, Object value) {
		properties.put(name, value);
	}
	
	public Object getProperty(String name) {
		return properties.get(name);
	}
	
	public void addRoute(Route r) {
		String rname = r.getName();
		if (routeMapping.containsKey(rname)) {
			throw new NameConflictException(
				"Route with name '" + rname + "' already exists");
		}		

		routeMapping.put(rname, r);
		routes.add(r);
	}
	
	public void removeRoute(Route r) {
		routes.remove(r);
	}
	
	public void addView(View v) {
		views.add(v);
		
		String routeName = v.getRouteName();
		if (routeName != null) {
			Route r = routeMapping.get(routeName);
			if (r == null) {
				throw new NoSuchRouteException(
					"Route '" + routeName + "' does not exist");
			}
			
			r.addView(v);
		}
	}
	
	public void removeView(View v) {
		views.remove(v);
	}

	public Route getMatchingRoute(Request req) {
		String pathInfo = req.getPathInfo();
		for (Route r: routes) {
			Matcher m = r.matchesUrl(pathInfo);
			if (m != null) {
				return r;
			}
		}
		
		return null;
	}
}
