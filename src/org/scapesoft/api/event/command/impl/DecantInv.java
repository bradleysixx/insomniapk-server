package org.scapesoft.api.event.command.impl;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.item.Decanting;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.game.player.Rights;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Nov 5, 2014
 */
public class DecantInv extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.OWNER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "decantinv" };
	}

	@Override
	public void execute(Player player) {
		Decanting.decantInventory(player, Integer.parseInt(cmd[1]));
	}

}
