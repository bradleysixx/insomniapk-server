package org.scapesoft.api.event.command.impl;

import org.scapesoft.api.event.command.CommandHandler;
import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.game.player.Rights;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Mar 19, 2014
 */
public class ReloadCommands extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.OWNER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "rlc" };
	}

	@Override
	public void execute(Player player) {
		CommandHandler.get().initialize();
		player.sendMessage("Successfully reloaded commands.");
	}

}