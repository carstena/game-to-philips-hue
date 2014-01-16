package nl.q42.jue;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.reflect.TypeToken;

class PortalDiscoveryResult {
	public final static Type gsonType = new TypeToken<List<PortalDiscoveryResult>>(){}.getType();
	
	private String internalipaddress;
	
	public String getIPAddress() {
		return internalipaddress;
	}
}
