package org.scapesoft.api.event.command.impl;

import org.scapesoft.Constants;
import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.World;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.game.player.Rights;
import org.scapesoft.utilities.misc.Config;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Aug 2, 2014
 */
public class ChangeConfig extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.ADMINISTRATOR;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "changeconfig" };
	}

	@Override
	public void execute(Player player) {
		Config.get().changeConfig(cmd[1], cmd[2]);
		switch (cmd[1].toLowerCase()) {
		case "double_exp":
			Constants.isDoubleExp = Config.get().getBoolean("double_exp");
			World.sendWorldMessage("Double experience is now " + (Constants.isDoubleExp ? "en" : "dis") + "abled!", false, false);
			break;
		case "double_votes":
			Constants.isDoubleVotes = Config.get().getBoolean("double_votes");
			World.sendWorldMessage("Double vote tokens is now " + (Constants.isDoubleVotes ? "en" : "dis") + "abled!", false, false);
			break;
		default:
			player.sendMessage("No such config " + cmd[1]);
			break;
		}
	}

}
