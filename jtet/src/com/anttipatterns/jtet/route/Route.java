package com.anttipatterns.jtet.route;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.anttipatterns.jtet.utils.StringUtils;
import com.anttipatterns.jtet.view.View;

public class Route {
	private String name;
	private String pattern;
	private List<String> placeHolders = new ArrayList<>();
	private String regexString;
	private String generatorString;
	private Pattern compiledRegex;
	private List<View> views = new ArrayList<View>();
	
	private final static Pattern STAR_AT_THE_END = Pattern.compile("\\*\\w*$");
	private final static Pattern ROUTE_RE = Pattern.compile("(\\{[_a-zA-Z][^{}]*(?:\\{[^{}]*\\}[^{}]*)*\\})");
	
	public class RouteConfigurator {
		private RouteConfigurator() {
			
		}
		
		public RouteConfigurator pattern(String pattern) {
			Route.this.pattern = pattern;
			compilePattern();
			return this;
		}
	}
	
	public Route(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public RouteConfigurator getConfigurator() {
		return new RouteConfigurator();
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
		compilePattern();
	}
	
	public Matcher matchesUrl(String url) {
		Matcher m = compiledRegex.matcher(url);
		if (m.matches()) {
			return m;
		}
		
		return null;
	}
	
	private String escapeGen(String s) {
		// TODO: quote segment
		return s.replace("%", "%%");
	}
	
	private void compilePattern() {
		String pattern = this.pattern;
		if (! pattern.startsWith("/")) {
			pattern = "/" + pattern;
		}
		
		String remainder = null;
		if (STAR_AT_THE_END.matcher(pattern).find()) {
			String[] parts = StringUtils.rsplit(pattern, "*", 1);
			pattern = parts[0];
			remainder = parts[1];
		}
		
	    List<String> parts = Arrays.asList(
    		StringUtils.reSplitWithSep(pattern, ROUTE_RE, -1)
	    );
	    
	    Iterator<String> iterParts = parts.iterator();	
	    
		List<String> gens = new ArrayList<>();
		List<String> patterns = new ArrayList<>();

		String first = iterParts.next();
		patterns.add(Pattern.quote(first));
		gens.add(escapeGen(first));
		
		while (iterParts.hasNext()) {
			name = iterParts.next();
			
			// remove {}
			name = name.substring(1, name.length() - 1);
			
			String reg = "[^/]+";
			if (name.indexOf(":") != -1) {
				String p[] = name.split(":", 2);
				name = p[0];
				reg = p[1];
			}
			
	        gens.add("%s");
	        placeHolders.add(name);
	        patterns.add("(?<" + name + ">" + reg + ")");

	        if (iterParts.hasNext()) {
		        String part = iterParts.next();
		        if (! part.isEmpty()) {
		        	patterns.add(Pattern.quote(part));
		        	gens.add(escapeGen(part));
		        }
	        }
		}
		
		if (remainder != null) {
	        patterns.add("(?<" + remainder + ">.*?)");
	        gens.add("%s");
	        placeHolders.add(remainder);
		}
		
	    regexString = StringUtils.join(patterns) + "$";
		generatorString = StringUtils.join(gens);
		compiledRegex = Pattern.compile(regexString);
		
		System.out.println(regexString + " - " + generatorString);
	}
	
	public void addView(View v) {
		views.add(v);
	}
	
	public void removeView(View v) {
		views.remove(v);
	}

	public Iterator<View> getViewIterator() {
		return views.iterator();
	}
}
