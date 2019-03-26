package org.scapesoft.api.event.command.impl;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.World;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.game.player.Rights;
import org.scapesoft.utilities.misc.Utils;

public final class ServerInformation extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.OWNER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "server", "serverinfo", "ticks" };
	}

	@Override
	public void execute(Player player) {
		Runtime runtime = Runtime.getRuntime();
		long inUse = Runtime.getRuntime().totalMemory();

		player.sendMessage("Uptime: " + org.scapesoft.utilities.misc.ServerInformation.get().getGameUptime() + ". Ticks: " + World.getGameWorker().getDelay() + "ms\n");
		player.sendMessage("Cores: " + runtime.availableProcessors() + ". Memory Usage: " + org.scapesoft.utilities.misc.ServerInformation.get().readable(inUse, true) + ". Free Memory: " + Utils.format(runtime.freeMemory()) + ". Maximum Memory: " + Utils.format(runtime.maxMemory()) + ". Total Available: " + Utils.format(runtime.totalMemory()));
	}

}