package com.anttipatterns.jtet.request.servletsupport;

import javax.servlet.http.HttpServletRequest;

import com.google.common.collect.ArrayListMultimap;

public interface ExtendedHttpServletRequest extends HttpServletRequest {
	public abstract ArrayListMultimap<String, String> query();
	public abstract ArrayListMultimap<String, String> form();
}