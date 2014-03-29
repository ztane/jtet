package com.anttipatterns.jtet.view;

import java.lang.reflect.Method;

import com.anttipatterns.jtet.request.Request;


public class View {
	private String routeName;
	private String renderer;
	private Object viewCallable;
	private IViewMapper viewMapper = new NullViewMapper();
	
	public class ViewConfigurator {
		private ViewConfigurator() { }
		
		public ViewConfigurator renderer(String renderer) {
			View.this.setRenderer(renderer);
			return this;
		}
		
		public ViewConfigurator routeName(String routeName) {
			View.this.setRouteName(routeName);
			return this;
		}
	}

	public View(Object viewCallable) {
		this.setViewCallable(viewCallable);
	}

	public ViewConfigurator getConfigurator() {
		return new ViewConfigurator();
	}

	public String getRenderer() {
		return renderer;
	}

	public void setRenderer(String renderer) {
		this.renderer = renderer;
	}

	public Object getViewCallable() {
		return viewCallable;
	}

	public void setViewCallable(Object viewCallable) {
		this.viewCallable = viewCallable;

		if (viewCallable instanceof Method) {
			viewMapper = new MethodViewMapper((Method)viewCallable);
		}
	}

	public String getRouteName() {
		return routeName;
	}

	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}

	public Object handle(Request request) {
		return this.viewMapper.handle(request);
	}	
}
