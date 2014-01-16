package nl.q42.jue;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.reflect.TypeToken;

class ErrorResponse {
	public final static Type gsonType = new TypeToken<List<ErrorResponse>>(){}.getType();
	
	public class Error {
		private Integer type;
		private String address;
		private String description;
	}
	
	private Error error;
	
	public Integer getType() {
		return error.type;
	}
	
	public String getAddress() {
		return error.address;
	}
	
	public String getDescription() {
		return error.description;
	}
}
