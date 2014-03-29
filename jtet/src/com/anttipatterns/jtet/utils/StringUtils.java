package com.anttipatterns.jtet.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
	public static final String[] rsplit(String s, String sep, int count) {
		ArrayList<String> collection = new ArrayList<>();
		
		if (count == -1) {
			count = Integer.MAX_VALUE;
		}

        int lastidx = s.length(), idx = 0;
        while ((idx = s.lastIndexOf(sep, lastidx - 1)) != -1)
        {
            if (collection.size() >= count) {
                break;
            }
            
            String sub = s.substring(idx + 1, lastidx);
            collection.add(0, sub);
            lastidx = idx;
        }
        collection.add(0, s.substring(0, lastidx));

        return collection.toArray(new String[collection.size()]);
	}
	
	public static String[] reSplitWithSep(String s, Pattern sep, int count) {
		ArrayList<String> collection = new ArrayList<>();
				
		if (count == -1) {
			count = Integer.MAX_VALUE;
		}

		int len = s.length();
		int idx = 0;
		Matcher matcher = sep.matcher(s);
		
		while (matcher.find()) {
			collection.add(s.substring(idx, matcher.start()));
			collection.add(s.substring(matcher.start(), matcher.end()));
			idx = matcher.end();
			
			count -= 1;
			if (count <= 0) {
				break;
			}
		}
		
		collection.add(s.substring(idx, len));
		
		return collection.toArray(new String[collection.size()]);
	}

	public static String join(List<String> patterns) {
		if (patterns == null || patterns.isEmpty()) {
			return "";
		}
		StringBuilder result = new StringBuilder();
		for (String s: patterns) {
			result.append(s);
		}
		
		return result.toString();
	}
}
