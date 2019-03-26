package org.scapesoft.api.event.command.impl;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.game.player.Rights;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Mar 19, 2014
 */
public class AdminClientTele extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.ADMINISTRATOR;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "tele" };
	}

	@Override
	public void execute(Player player) {
		cmd = cmd[1].split(",");
		int plane = Integer.valueOf(cmd[0]);
		int x = Integer.valueOf(cmd[1]) << 6 | Integer.valueOf(cmd[3]);
		int y = Integer.valueOf(cmd[2]) << 6 | Integer.valueOf(cmd[4]);
		player.setNextWorldTile(new WorldTile(x, y, plane));
	}

}
