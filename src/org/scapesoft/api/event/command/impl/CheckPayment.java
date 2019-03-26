package org.scapesoft.api.event.command.impl;

import java.util.concurrent.TimeUnit;

import org.scapesoft.api.database.mysql.impl.ForumIntegration;
import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.game.player.Rights;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jul 7, 2014
 */
public class CheckPayment extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.PLAYER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "claim" };
	}

	@Override
	public void execute(Player player) {
		Long lastTime = (Long) player.getTemporaryAttributtes().get("last_payment_verif");
		if (lastTime == null || (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - lastTime) > 10)) {
			ForumIntegration.claimGoldPoints(player);
			player.getTemporaryAttributtes().put("last_payment_verif", System.currentTimeMillis());
		} else {
			player.sendMessage("You can only use this command once every 10 seconds...");
		}
	}

}
