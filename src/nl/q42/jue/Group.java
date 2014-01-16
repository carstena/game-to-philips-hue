package nl.q42.jue;

import java.lang.reflect.Type;
import java.util.Map;

import com.google.gson.reflect.TypeToken;

/**
 * Basic group information.
 */
public class Group {
	public final static Type gsonType = new TypeToken<Map<String, Group>>(){}.getType();
	
	private String id;
	private String name;
	
	Group() {
		this.id = "0";
		this.name = "Lightset 0";
	}
	
	void setName(String name) {
		this.name = name;
	}
	
	void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Returns if the group can be modified.
	 * Currently only returns false for the all lights pseudo group.
	 * @return modifiability of group
	 */
	public boolean isModifiable() {
		return !id.equals("0");
	}
	
	/**
	 * Returns the id of the group.
	 * @return id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Returns the name of the group.
	 * @return name
	 */
	public String getName() {
		return name;
	}
}
