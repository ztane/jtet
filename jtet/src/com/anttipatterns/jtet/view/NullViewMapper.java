package com.anttipatterns.jtet.view;

import com.anttipatterns.jtet.request.Request;

public class NullViewMapper implements IViewMapper {
	@Override
	public Context handle(Request request) {
		return new Context();
	}
}
