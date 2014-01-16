package nl.q42.jue;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

/**
 * A whitelisted user.
 */
public class User {
	@SerializedName("last use date")
	private Date lastUseDate;
	
	@SerializedName("create date")
	private Date createDate;
	
	private String name;
	
	/**
	 * Returns the last time a command was issued as this user.
	 * @return time of last command by this user
	 */
	public Date getLastUseDate() {
		return lastUseDate;
	}
	
	/**
	 * Returns the date this user was created.
	 * @return creation date of user
	 */
	public Date getCreationDate() {
		return createDate;
	}
	
	/**
	 * Returns the username of this user.
	 * @return username
	 */
	public String getUsername() {
		return name;
	}
}
