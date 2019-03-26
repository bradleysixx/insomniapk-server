package org.scapesoft.api.event.command.impl;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.game.player.Rights;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jul 1, 2014
 */
public class Vote extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.PLAYER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "vote" };
	}

	@Override
	public void execute(Player player) {
		player.getPackets().sendOpenURL("http://scapesoft.org/app/vote");
	}

}
