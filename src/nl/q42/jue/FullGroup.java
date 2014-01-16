package nl.q42.jue;

import java.util.List;

/**
 * Detailed group information.
 */
public class FullGroup extends Group {
	private State action;
	private List<String> lights;
	
	FullGroup() {}
	
	/**
	 * Returns the last sent state update to the group.
	 * This does not have to reflect the current state of the group.
	 * @return last state update
	 */
	public State getAction() {
		return action;
	}
	
	/**
	 * Returns a list of the lights in the group.
	 * @return lights in the group
	 */
	public List<Light> getLights() {
		return Util.idsToLights(lights);
	}
}
