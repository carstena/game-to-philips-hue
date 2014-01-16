package nl.q42.jue;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.google.gson.reflect.TypeToken;

class SuccessResponse {
	public final static Type gsonType = new TypeToken<List<SuccessResponse>>(){}.getType();
	
	public Map<String, Object> success;
}
