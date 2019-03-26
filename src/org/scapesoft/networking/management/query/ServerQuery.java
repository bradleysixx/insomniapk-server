package org.scapesoft.networking.management.query;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Nov 21, 2014
 */
public abstract class ServerQuery {

	/**
	 * The array of listeners that apply to this query
	 * 
	 * @return
	 */
	public abstract String[] getQueryListeners();

	/**
	 * The description of the query
	 */
	public abstract String getDescription();
	
	/**
	 * What the server query will send on request
	 */
	public abstract String onRequest();

	/**
	 * Getting the query
	 * 
	 * @return
	 */
	public String getQuery() {
		return query;
	}

	/**
	 * Setting the query
	 * 
	 * @param query
	 */
	public void setQuery(String query) {
		this.query = query;
	}

	/**
	 * The instance of the query
	 */
	private String query;
}
