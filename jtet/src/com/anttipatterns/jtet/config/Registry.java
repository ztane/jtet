package com.anttipatterns.jtet.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import com.anttipatterns.jtet.ca.ComponentRegistry;
import com.anttipatterns.jtet.ca.ComponentRegistry.Arguments;
import com.anttipatterns.jtet.ca.IComponentQuery;
import com.anttipatterns.jtet.ca.IComponentRegistry;
import com.anttipatterns.jtet.props.IPropSupport;
import com.anttipatterns.jtet.props.Key;
import com.anttipatterns.jtet.props.NoSuchPropertyException;
import com.anttipatterns.jtet.props.PropStorage;
import com.anttipatterns.jtet.request.IRequest;
import com.anttipatterns.jtet.route.NoSuchRouteException;
import com.anttipatterns.jtet.route.Route;
import com.anttipatterns.jtet.settings.ISettings;
import com.anttipatterns.jtet.tween.ITween;
import com.anttipatterns.jtet.view.View;

public class Registry implements IComponentQuery, IPropSupport<Registry> {
	private IComponentRegistry components = new ComponentRegistry();
	private Map<String, Route> routeMapping = new HashMap<>();
	private List<Route> routes = new ArrayList<>();
	private List<View> views = new ArrayList<>();
	protected List<ITween> tweens = new ArrayList<>();
	
	public List<ITween> getTweens() {
		return Collections.unmodifiableList(tweens);
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

	public Route getMatchingRoute(IRequest req) {
		String pathInfo = req.getPathInfo();
		for (Route r: routes) {
			Matcher m = r.matchesUrl(pathInfo);
			if (m != null) {
				return r;
			}
		}
		
		return null;
	}
	
	public IComponentRegistry components() {
		return components;
	}

	@Override
	public <T> T queryAdapter(Class<T> targetClass, Arguments args,
			String name, T defaultValue) {
		return components.queryAdapter(targetClass, args, name, defaultValue);
	}

	@Override
	public <T> T queryAdapter(Class<T> targetClass, Arguments args,
			T defaultValue) {
		return components.queryAdapter(targetClass, args, defaultValue);
	}

	@Override
	public <T> T getAdapter(Class<T> targetClass, Arguments args, String name) {
		return components.getAdapter(targetClass, args, name);
	}

	@Override
	public <T> T getAdapter(Class<T> targetClass, Arguments args) {
		return components.getAdapter(targetClass, args);
	}

	@Override
	public <T> T getUtility(Class<T> utility) {
		return getUtility(utility);
	}

	@Override
	public <T> T getUtility(Class<T> utility, String name) {
		return getUtility(utility, name);
	}

	@Override
	public <T> T queryUtility(Class<T> utility, T defaultCValue) {
		return components.queryUtility(utility, defaultCValue);
	}

	@Override
	public <T> T queryUtility(Class<T> utility, String name, T defaultCValue) {
		return components.queryUtility(utility, name, defaultCValue);
	}

	public ISettings getSettings() {
		return null;
	}
	
	public static <T> Key<Registry, T> key(Class<? extends T> cls, String name) {
		return Key.key(cls, name);
	}

	private PropStorage<Registry> props = new PropStorage<>(this);

	@Override
	public <T> T getProp(Key<Registry, T> key) throws NoSuchPropertyException {
		return props.get(key);
	}

	@Override
	public <T> void setProp(Key<Registry, T> key, T value) {
		props.set(key, value);
	}
}
