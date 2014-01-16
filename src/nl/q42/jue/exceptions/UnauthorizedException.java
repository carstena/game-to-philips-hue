package nl.q42.jue.exceptions;

/**
 * Thrown when the specified user is no longer whitelisted on the bridge. 
 */
@SuppressWarnings("serial")
public class UnauthorizedException extends ApiException {
	public UnauthorizedException() {}
	
	public UnauthorizedException(String message) {
		super(message);
	}
}
