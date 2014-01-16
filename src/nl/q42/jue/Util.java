package nl.q42.jue;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Util {
	private Util() {}
	
	// This is used to check what byte size strings have, because the bridge doesn't natively support UTF-8
	public static int stringSize(String str) {
		try {
			return str.getBytes("utf-8").length;
		} catch (UnsupportedEncodingException e) {
			throw new UnsupportedOperationException("UTF-8 not supported");
		}
	}
	
	public static List<Light> idsToLights(List<String> ids) {
		List<Light> lights = new ArrayList<Light>();
		
		for (String id : ids) {
			Light light = new Light();
			light.setId(id);
			lights.add(light);
		}
		
		return lights;
	}
	
	public static List<String> lightsToIds(List<Light> lights) {
		List<String> ids = new ArrayList<String>();
		
		for (Light light : lights) {
			ids.add(light.getId());
		}
		
		return ids;
	}
	
	public static String quickMatch(String needle, String haystack) {
		Matcher m = Pattern.compile(needle).matcher(haystack);
		m.find();
		return m.group(1);
	}
}
