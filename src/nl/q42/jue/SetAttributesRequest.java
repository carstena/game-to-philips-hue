package nl.q42.jue;

import java.util.List;

@SuppressWarnings("unused")
class SetAttributesRequest {
	private String name;
	private List<String> lights;
	
	public SetAttributesRequest(String name) {
		this(name, null);
	}
	
	public SetAttributesRequest(List<Light> lights) {
		this(null, lights);
	}
	
	public SetAttributesRequest(String name, List<Light> lights) {
		if (name != null && Util.stringSize(name) > 32) {
			throw new IllegalArgumentException("Name can be at most 32 characters long");
		} else if (lights != null && (lights.size() == 0 || lights.size() > 16)) {
			throw new IllegalArgumentException("Group cannot be empty and cannot have more than 16 lights");
		}
		
		this.name = name;
		if (lights != null) this.lights = Util.lightsToIds(lights);
	}
}
