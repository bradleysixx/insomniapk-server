package org.scapesoft.api.event.command.impl;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.game.player.Rights;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Dec 31, 2014
 */
public class SetWildernessPoints extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.OWNER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "setwp" };
	}

	@Override
	public void execute(Player player) {
		player.getFacade().setWildernessPoints(Integer.parseInt(cmd[1]));
	}

}
