package org.scapesoft.api.event.command.impl;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.World;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.game.player.Rights;

/**
 * @author Jonathan
 */
public class MassURLMessage extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.OWNER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "sendurlmsg" };
	}

	@Override
	public void execute(final Player player) {
		for (Player pl : World.getPlayers()) {
			if (pl == null) {
				continue;
			}
			try {
				pl.getPackets().sendURLMessage(getCompleted(cmd, 2), cmd[1]);
			} catch (Exception e) {
				player.sendMessage("Format ::sendurl link text");
			}
		}
	}

}