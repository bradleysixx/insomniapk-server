package org.scapesoft.api.event.command.impl;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.game.player.Rights;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Aug 20, 2014
 */
public class SetGoldPoints extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.OWNER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "setgp" };
	}

	@Override
	public void execute(Player player) {
		player.getFacade().setGoldPoints(Integer.parseInt(cmd[1]));
		player.sendMessage("I now have " + player.getFacade().getGoldPoints() + " gold points");
	}

}
