package org.scapesoft.api.event.command.impl;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.World;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.game.player.Rights;
import org.scapesoft.utilities.player.Saving;

/**
 * @author Jonathan
 * 
 */
public class SetGoldPointsPlayer extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.OWNER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "setgoldpointsother" };
	}

	@Override
	public void execute(Player player) {
		String[] cmd = command.split(" ");
		String name = cmd[1].replaceAll("_", " ");
		Player target = World.getPlayerByDisplayName(name);
		if (target == null) {
			target = Saving.loadPlayer(name);
		}
		if (target == null) {
			player.sendMessage("No such player!");
			return;
		}
		target.getFacade().setGoldPoints(Integer.parseInt(cmd[2]));
		player.sendMessage(target.getUsername() + " now has " + target.getFacade().getGoldPoints() + " gold points!");
	}

}