package nl.q42.jue.exceptions;

/**
 * Thrown when trying to change the state of a light that is off.
 */
@SuppressWarnings("serial")
public class DeviceOffException extends ApiException {
	public DeviceOffException() {}
	
	public DeviceOffException(String message) {
		super(message);
	}
}
