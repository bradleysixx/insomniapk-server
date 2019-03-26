package org.scapesoft.networking.management.query.impl;

import org.scapesoft.game.World;
import org.scapesoft.networking.management.query.ServerQuery;
import org.scapesoft.utilities.misc.ServerInformation;
import org.scapesoft.utilities.misc.Utils;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Nov 21, 2014
 */
public class InformationQuery extends ServerQuery {

	@Override
	public String[] getQueryListeners() {
		return new String[] { "serverinfo" };
	}

	@Override
	public String getDescription() {
		return "Prints out information about the server";
	}

	@Override
	public String onRequest() {
		StringBuilder bldr = new StringBuilder();
		Runtime runtime = Runtime.getRuntime();
		long inUse = Runtime.getRuntime().totalMemory();

		bldr.append("Uptime: " + ServerInformation.get().getGameUptime() + ". Ticks: " + World.getGameWorker().getDelay() + "ms\n");
		bldr.append("\tCores: " + runtime.availableProcessors() + ". Memory Usage: " + ServerInformation.get().readable(inUse, true) + ". Free Memory: " + Utils.format(runtime.freeMemory()) + ". Maximum Memory: " + Utils.format(runtime.maxMemory()) + ". Total Available: " + Utils.format(runtime.totalMemory()));
		return bldr.toString();
	}

}
