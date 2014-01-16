package nl.q42.jue.exceptions;

/**
 * Thrown when operating on a group, light or user that doesn't exist.
 */
@SuppressWarnings("serial")
public class EntityNotAvailableException extends ApiException {
	public EntityNotAvailableException() {}
	
	public EntityNotAvailableException(String message) {
		super(message);
	}
}
