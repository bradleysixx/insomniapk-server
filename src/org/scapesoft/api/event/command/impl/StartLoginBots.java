package org.scapesoft.api.event.command.impl;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.console.tools.LoginBot;
import org.scapesoft.utilities.game.player.Rights;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Aug 21, 2014
 */
public class StartLoginBots extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.OWNER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "loginbots" };
	}

	@Override
	public void execute(Player player) {
		LoginBot.start(Integer.parseInt(cmd[1]));
	}

}
