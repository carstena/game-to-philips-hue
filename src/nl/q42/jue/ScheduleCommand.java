package nl.q42.jue;

import com.google.gson.JsonElement;


/**
 * Information about a scheduled command.
 */
public class ScheduleCommand {
	private String address;
	private String method;
	private JsonElement body;
	
	ScheduleCommand(String address, String method, JsonElement body) {
		this.address = address;
		this.method = method;
		this.body = body;
	}
	
	/**
	 * Returns the relative request url.
	 * @return request url
	 */
	public String getAddress() {
		return address;
	}
	
	/**
	 * Returns the request method.
	 * Can be GET, PUT, POST or DELETE.
	 * @return request method
	 */
	public String getMethod() {
		return method;
	}
	
	/**
	 * Returns the request body.
	 * @return request body
	 */
	public String getBody() {
		return body.toString();
	}
}
