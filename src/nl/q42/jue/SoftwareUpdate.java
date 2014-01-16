package nl.q42.jue;

/**
 * Details of a bridge firmware update.
 */
public class SoftwareUpdate {
	private int updatestate;
	private String url;
	private String text;
	private boolean notify;
	
	/**
	 * Returns the state of the update.
	 * TODO: Actual meaning currently undocumented
	 * @return state of update
	 */
	public int getUpdateState() {
		return updatestate;
	}
	
	/**
	 * Returns the url of the changelog.
	 * @return changelog url
	 */
	public String getUrl() {
		return url;
	}
	
	/**
	 * Returns a description of the update.
	 * @return update description
	 */
	public String getText() {
		return text;
	}
	
	/**
	 * Returns if there will be a notification about this update.
	 * @return true for notification, false otherwise
	 */
	public boolean isNotified() {
		return notify;
	}
}
