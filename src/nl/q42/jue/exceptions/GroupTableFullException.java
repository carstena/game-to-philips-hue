package nl.q42.jue.exceptions;

/**
 * Thrown when adding more than 16 groups (excluding all lights group) to a bridge.
 */
@SuppressWarnings("serial")
public class GroupTableFullException extends ApiException {
	public GroupTableFullException() {}
	
	public GroupTableFullException(String message) {
		super(message);
	}
}
