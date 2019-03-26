package org.scapesoft.utilities.player.scripts.gamescripts;

import java.io.File;
import java.io.IOException;

import org.scapesoft.cache.Cache;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.misc.Config;
import org.scapesoft.utilities.player.Saving;
import org.scapesoft.utilities.player.scripts.GameScript;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Sep 7, 2014
 */
public class ReferralRewardsGiver extends GameScript {

	public static void main(String[] args) {
		try {
			Cache.init();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Config.get().load();
		for (File acc : getAccounts()) {
			try {
				Player player = (Player) Saving.loadSerializedFile(acc);
				if (acc.getName().equalsIgnoreCase(name)) {
					giveReward(player, 5);
					savePlayer(player, acc);
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void giveReward(Player player, int referrals) {
		switch (referrals) {
		case 5:
			player.getBank().addItem(995, 5000000, false);
			player.getBank().addItem(10944, 20, false);
			player.getLoyaltyManager().addPoints(2000);
			break;
		}
	}
	
	private static String name = "fewlsz.p";

}
