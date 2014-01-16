package nl.q42.jue;

@SuppressWarnings("unused")
class CreateUserRequest {
	private String username;
	private String devicetype;
	
	public CreateUserRequest(String username, String devicetype) {
		if (Util.stringSize(devicetype) > 40) {
			throw new IllegalArgumentException("Device type can be at most 40 characters long");
		}
		
		if (Util.stringSize(username) < 10 || Util.stringSize(username) > 40) {
			throw new IllegalArgumentException("Username must be between 10 and 40 characters long");
		}
		
		this.username = username;
		this.devicetype = devicetype;
	}
	
	public CreateUserRequest(String devicetype) {
		if (Util.stringSize(devicetype) > 40) {
			throw new IllegalArgumentException("Device type can be at most 40 characters long");
		}
		
		this.devicetype = devicetype;
	}
}
