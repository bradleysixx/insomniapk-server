package org.scapesoft.api.event.command.impl;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.World;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.game.player.Rights;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Apr 3, 2014
 */
public class TeleportToPlayer extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.SUPPORT;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "teleto" };
	}

	@Override
	public void execute(Player player) {
		if (player.isSupporter() || player.getRights() > 0) {
			String name = getCompleted(cmd, 1).replaceAll("_", " ");
			Player target = World.getPlayerByDisplayName(name);
			if (target != null) {
				player.setNextWorldTile(target);
				player.sendMessage("You have teleported to " + target.getDisplayName());
			} else {
				player.sendMessage("No such player.");
			}
		}
	}

}
