package org.scapesoft.api.event.command.impl;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.World;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.game.player.Rights;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jul 21, 2014
 */
public class GetIP extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.ADMINISTRATOR;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "getip" };
	}

	@Override
	public void execute(Player player) {
		final String name = getCompleted(cmd, 1).replaceAll("_", " ");
		Player target = World.getPlayer(name);
		if (target == null) {
			player.sendMessage("No user by name: " + name);
			return;
		}
		player.sendMessage(target.getUsername() + "'s ip is: " + target.getSession().getIP());
	}

}
