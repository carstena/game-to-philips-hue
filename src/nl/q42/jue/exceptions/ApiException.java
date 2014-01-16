package nl.q42.jue.exceptions;

/**
 * Thrown when the API returns an unknown error.
 */
@SuppressWarnings("serial")
public class ApiException extends Exception {
	public ApiException() {}
	
	public ApiException(String message) {
		super(message);
	}
}