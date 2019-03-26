package org.scapesoft.api.event.command.impl;

import java.util.concurrent.TimeUnit;

import org.scapesoft.api.database.mysql.impl.VoteVerification;
import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.game.player.Rights;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jul 1, 2014
 */
public class CheckVote extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.PLAYER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "auth", "check" };
	}

	@Override
	public void execute(Player player) {
		Long lastTime = (Long) player.getTemporaryAttributtes().get("last_auth_sent");
		if (lastTime == null || (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - lastTime) > 10)) {
			VoteVerification.checkAuth(player, getCompleted(cmd, 1));
			player.getTemporaryAttributtes().put("last_auth_sent", System.currentTimeMillis());
		} else {
			player.sendMessage("You can only use this command once every 10 seconds...");
		}
	}

}