package org.scapesoft.api.event.command.impl;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.game.player.Rights;

/**
 * @author Tyluur<itstyluur@gmail.com
 * @since Oct 26, 2014
 */
public class ToggleYell extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.PLAYER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "toggleyell" };
	}

	@Override
	public void execute(Player player) {
		player.getFacade().setYellOff(!player.getFacade().hasYellOff());
		player.sendMessage("You have now " + (player.getFacade().hasYellOff() ? "dis" : "en") + "abled the yell channel.");
	}

}
