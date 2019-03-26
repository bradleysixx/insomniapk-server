package org.scapesoft.api.event.command.impl;

import org.scapesoft.api.event.EventManager;
import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.game.player.Rights;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Mar 22, 2014
 */
public class ReloadEvents extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.OWNER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "rle" };
	}

	@Override
	public void execute(Player player) {
		EventManager.get().load();
	}

}
