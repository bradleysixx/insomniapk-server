package org.scapesoft.api.database;

public interface DatabaseConfiguration {

	/**
	 * Create a new database connection
	 *
	 * @return The new connection
	 */
	public DatabaseConnection newConnection();

}