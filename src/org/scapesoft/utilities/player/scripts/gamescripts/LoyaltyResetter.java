package org.scapesoft.utilities.player.scripts.gamescripts;

import java.io.File;
import java.io.IOException;

import org.scapesoft.cache.Cache;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.content.achievements.AchievementManager;
import org.scapesoft.utilities.player.Saving;
import org.scapesoft.utilities.player.scripts.GameScript;

/**
 * @author Tyluur<itstyluur@gmail.com
 * @since Oct 7, 2014
 */
public class LoyaltyResetter extends GameScript {

	public static void main(String[] args) throws IOException {
		Cache.init();
		AchievementManager.load();
		for (File acc : getAccounts()) {
			try {
				Player player = (Player) Saving.loadSerializedFile(acc);
				if (player != null) {
					player.getLoyaltyManager().forcePoints(0);
					player.getLoyaltyManager().clearAll();
					player.getAppearence().forceTitle(-1);
					savePlayer(player, acc);
				}
			} catch (Exception e) {
				System.out.println("Error with: " + acc);
			}
		}
	}

}
