package nl.q42.jue;

import java.lang.reflect.Type;
import java.util.Map;

import com.google.gson.reflect.TypeToken;

/**
 * Basic light information.
 */
public class Light {
	public final static Type gsonType = new TypeToken<Map<String, Light>>(){}.getType();
	
	private String id;
	private String name;
	
	Light() {}
	
	void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Returns the id of the light.
	 * @return id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Returns the name of the light.
	 * @return name
	 */
	public String getName() {
		return name;
	}
}
