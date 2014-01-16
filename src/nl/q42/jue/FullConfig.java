package nl.q42.jue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Container for all data on a bridge.
 */
public class FullConfig {
	private Map<String, FullLight> lights;
	private Map<String, FullGroup> groups;
	private Config config;
	
	/**
	 * Returns detailed information about all lights known to the bridge.
	 * @return detailed lights list
	 */
	public List<FullLight> getLights() {
		ArrayList<FullLight> lightsList = new ArrayList<FullLight>();
		
		for (String id : lights.keySet()) {
			FullLight light = lights.get(id);
			light.setId(id);
			lightsList.add(light);
		}
		
		return lightsList;
	}
	
	/**
	 * Returns detailed information about all groups on the bridge.
	 * @return detailed groups list
	 */
	public List<FullGroup> getGroups() {		
		ArrayList<FullGroup> groupsList = new ArrayList<FullGroup>();
		
		for (String id : groups.keySet()) {
			FullGroup group = groups.get(id);
			group.setId(id);
			groupsList.add(group);
		}
		
		return groupsList;
	}
	
	/**
	 * Returns bridge configuration.
	 * Use HueBridge.getConfig() if you only need this.
	 * @return bridge configuration
	 */
	public Config getConfig() {
		return config;
	}
}
