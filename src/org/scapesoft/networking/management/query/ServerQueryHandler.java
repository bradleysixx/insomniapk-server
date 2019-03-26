package org.scapesoft.networking.management.query;

import java.util.HashMap;
import java.util.Map;

import org.scapesoft.utilities.misc.FileClassLoader;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Nov 21, 2014
 */
public class ServerQueryHandler {

	/**
	 * Loading up all server queries
	 */
	public static void load() {
		for (Object clazz : FileClassLoader.getClassesInDirectory(ServerQueryHandler.class.getPackage().getName() + ".impl")) {
			try {
				ServerQuery skeleton = (ServerQuery) clazz;
				if (skeleton.getQueryListeners() != null) {
					for (String parameter : skeleton.getQueryListeners()) {
						getServerQueries().put(parameter.toLowerCase(), skeleton);
					}
				} else {
					throw new IllegalStateException("Could not register " + skeleton.getClass().getCanonicalName() + "; no parameters");
				}
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}

	/**
	 * Handles the input of a query
	 * 
	 * @param query
	 *            The query
	 */
	public static String handleQuery(String query) {
		String[] split = query.split(" ");
		ServerQuery serverQuery = getServerQueries().get(split[0].toLowerCase());
		if (serverQuery == null) {
			return "No such query " + query + "...";
		} else {
			serverQuery.setQuery(query);
			return serverQuery.onRequest();
		}
	}

	public static Map<String, ServerQuery> getServerQueries() {
		return SERVER_QUERIES;
	}

	/**
	 * The map of server queries
	 */
	private static final Map<String, ServerQuery> SERVER_QUERIES = new HashMap<>();
}
