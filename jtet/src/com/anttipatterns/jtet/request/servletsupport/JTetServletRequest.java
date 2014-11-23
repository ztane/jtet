package com.anttipatterns.jtet.request.servletsupport;

//
//========================================================================
//Copyright (c) 1995-2014 Mort Bay Consulting Pty. Ltd.
//------------------------------------------------------------------------
//All rights reserved. This program and the accompanying materials
//are made available under the terms of the Eclipse Public License v1.0
//and Apache License v2.0 which accompanies this distribution.
//
//  The Eclipse Public License is available at
//  http://www.eclipse.org/legal/epl-v10.html
//
//  The Apache License v2.0 is available at
//  http://www.opensource.org/licenses/apache2.0.php
//
//You may elect to redistribute this code under either of these licenses.
//========================================================================
//

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.util.MultiMap;

import com.google.common.collect.ArrayListMultimap;

/* ------------------------------------------------------------ */
/**
* Jetty Request.
* <p>
* Implements {@link javax.servlet.http.HttpServletRequest} from the <code>javax.servlet.http</code> package.
* </p>
* <p>
* The standard interface of mostly getters, is extended with setters so that the request is mutable by the handlers that it is passed to. This allows the
* request object to be as lightweight as possible and not actually implement any significant behavior. For example
* <ul>
*
* <li>The {@link Request#getContextPath()} method will return null, until the request has been passed to a {@link ContextHandler} which matches the
* {@link Request#getPathInfo()} with a context path and calls {@link Request#setContextPath(String)} as a result.</li>
*
* <li>the HTTP session methods will all return null sessions until such time as a request has been passed to a
* {@link org.eclipse.jetty.server.session.SessionHandler} which checks for session cookies and enables the ability to create new sessions.</li>
*
* <li>The {@link Request#getServletPath()} method will return null until the request has been passed to a <code>org.eclipse.jetty.servlet.ServletHandler</code>
* and the pathInfo matched against the servlet URL patterns and {@link Request#setServletPath(String)} called as a result.</li>
* </ul>
*
* A request instance is created for each connection accepted by the server and recycled for each HTTP request received via that connection.
* An effort is made to avoid reparsing headers and cookies that are likely to be the same for requests from the same connection.
*
* <p>
* The form content that a request can process is limited to protect from Denial of Service attacks. The size in bytes is limited by
* {@link ContextHandler#getMaxFormContentSize()} or if there is no context then the "org.eclipse.jetty.server.Request.maxFormContentSize" {@link Server}
* attribute. The number of parameters keys is limited by {@link ContextHandler#getMaxFormKeys()} or if there is no context then the
* "org.eclipse.jetty.server.Request.maxFormKeys" {@link Server} attribute.
*
*
*/
public class JTetServletRequest extends HttpServletRequestWrapper implements ExtendedHttpServletRequest {
	private ArrayListMultimap<String, String> _query;
	private ArrayListMultimap<String, String> _form;
	public JTetServletRequest(HttpServletRequest wrapped) {
		super(wrapped);
		if (! (wrapped instanceof Request)) {
			throw new IllegalArgumentException("The wrapped request is not a Jetty Request");
		}
	}
		
	protected ArrayListMultimap<String, String> parseQuery() {
		ServletRequest wrapped = getRequest();
		ArrayListMultimap<String, String> tmp = ArrayListMultimap.create();
		if (wrapped instanceof Request) {
			Request req = (Request)wrapped;
			MultiMap<String> queryParams = req.getQueryParameters();
			for (Map.Entry<String, ? extends Iterable<String>> entry : queryParams.entrySet()) {
			    tmp.putAll(entry.getKey(), entry.getValue());
			}
		}
		return tmp;
	}
	
	/* (non-Javadoc)
	 * @see com.anttipatterns.jtet.request.servletsupport.ExtendedHttpServletRequest#query()
	 */
	@Override
	public ArrayListMultimap<String, String> query() {
		if (_query == null) {
			_query = parseQuery();
		}
		return _query;
	}
	
	/* (non-Javadoc)
	 * @see com.anttipatterns.jtet.request.servletsupport.ExtendedHttpServletRequest#form()
	 */
	@Override
	public ArrayListMultimap<String, String> form() {
		if (_form == null) {
			_form = parseForm();
		}
		return _form;
	}
	
	protected ArrayListMultimap<String, String> parseForm() {
		ArrayListMultimap<String, String> rv = ArrayListMultimap.create();
		
		ArrayListMultimap<String, String> GET = query();
		if (getMethod().equals("POST")) {
			for (Entry<String, String[]> entry: getParameterMap().entrySet()) {
				String key = entry.getKey();
				if (GET.containsKey(key)) {
					List<String> dupes = new ArrayList<>(GET.get(key));
					for (String value: entry.getValue()) {
						if (! dupes.remove(value)) {
							// It was not in GET...
							rv.put(key, value);
						}
					}
				}
				else {
					rv.putAll(key, Arrays.asList(entry.getValue()));
				}
			}
		}

		return rv;
	}
}
