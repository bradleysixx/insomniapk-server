package org.scapesoft.api.event.command.impl;

import org.Server;
import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.World;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.game.player.Rights;

/**
 * @author Jonathan
 * 
 */
public class ForceRestart extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.OWNER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[]{"forceshutdown"};
	}

	@Override
	public void execute(final Player player) {
		for (Player p2 : World.getPlayers()) {
			if (p2 == null || !p2.hasStarted()) {
				continue;
			}
			p2.realFinish();
		}
		Server.shutdown();
	}

}