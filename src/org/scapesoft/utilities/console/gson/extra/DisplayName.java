package org.scapesoft.utilities.console.gson.extra;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Dec 3, 2014
 */
public class DisplayName {

	public DisplayName(String loginName, List<String> displayNames) {
		this.loginName = loginName;
		this.displayNames = displayNames;
	}
	
	public DisplayName(String loginName, String displayName) {
		this.loginName = loginName;
		this.displayNames = new ArrayList<>();
		this.displayNames.add(displayName);
	}

	/**
	 * Getting the list of display names, the one at the top of the list (the
	 * last index) is the current display name
	 * 
	 * @return
	 */
	public List<String> getDisplayNames() {
		return displayNames;
	}

	/**
	 * The login name
	 * 
	 * @return
	 */
	public String getLoginName() {
		return loginName;
	}

	/**
	 * The login name
	 */
	private final String loginName;
	
	/**
	 * The list of display names
	 */
	private final List<String> displayNames;

}
