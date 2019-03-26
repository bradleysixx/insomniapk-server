package org.scapesoft.api.event.command.impl;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.game.player.Rights;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Mar 21, 2014
 */
public class TeleCoordinates extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.MODERATOR;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "xtele" };
	}

	@Override
	public void execute(Player player) {
		String[] cmd = command.split(" ");
		int x = Integer.parseInt(cmd[1]);
		int y = Integer.parseInt(cmd[2]);
		int z = cmd.length > 3 ? Integer.parseInt(cmd[3]) : 0;
		player.setNextWorldTile(new WorldTile(x, y, z));
	}

}
