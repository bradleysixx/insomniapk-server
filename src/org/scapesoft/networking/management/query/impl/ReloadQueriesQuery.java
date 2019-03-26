package org.scapesoft.networking.management.query.impl;

import org.scapesoft.networking.management.query.ServerQuery;
import org.scapesoft.networking.management.query.ServerQueryHandler;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Nov 21, 2014
 */
public class ReloadQueriesQuery extends ServerQuery {

	@Override
	public String[] getQueryListeners() {
		return new String[] { "rlq" };
	}

	@Override
	public String getDescription() {
		return "Reloads the queries";
	}

	@Override
	public String onRequest() {
		ServerQueryHandler.getServerQueries().clear();
		ServerQueryHandler.load();
		return "Loaded " + ServerQueryHandler.getServerQueries().size() + " queries...";
	}

}
