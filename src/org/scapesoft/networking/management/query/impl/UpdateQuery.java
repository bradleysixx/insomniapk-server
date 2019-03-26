package org.scapesoft.networking.management.query.impl;

import org.scapesoft.game.World;
import org.scapesoft.networking.management.query.ServerQuery;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Nov 22, 2014
 */
public class UpdateQuery extends ServerQuery {

	@Override
	public String[] getQueryListeners() {
		return new String[] { "update" };
	}

	@Override
	public String getDescription() {
		return "Shuts down the server after 60 seconds.";
	}

	@Override
	public String onRequest() {
		World.safeShutdown(false, 60);
		return "Sent successfully... Server shutting down in 60.";
	}

}
