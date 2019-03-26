package org.scapesoft.api.event.command.impl;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.game.player.Rights;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Nov 29, 2014
 */
public class ChangeYellColour extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.PLAYER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "changeyellhex" };
	}

	@Override
	public void execute(Player player) {
		String hex = getCompleted(cmd, 1);
		if (hex.length() > 15) {
			player.sendMessage("Your hex is too long! It must be under 15 characters.");
			return;
		}
		hex = hex.trim();
		player.getFacade().setYellColour(hex);
		player.sendMessage("You have just changed your yell hex to: " + hex);
	}

}
