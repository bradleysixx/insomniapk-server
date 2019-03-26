package org.scapesoft.utilities.player.scripts.gamescripts;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;

import org.scapesoft.cache.Cache;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.content.achievements.Achievement;
import org.scapesoft.game.player.content.achievements.AchievementManager;
import org.scapesoft.utilities.player.Saving;
import org.scapesoft.utilities.player.scripts.GameScript;

/**
 * @author Tyluur<itstyluur@gmail.com
 * @since Oct 7, 2014
 */
public class AchievementRewardsGiver extends GameScript {

	public static void main(String[] args) throws IOException {
		Cache.init();
		AchievementManager.load();
		for (File acc : getAccounts()) {
			try {
				if (acc.getName().equalsIgnoreCase(name)) {
					Player player = (Player) Saving.loadSerializedFile(acc);
					if (player != null) {
						Iterator<Entry<String, Achievement>> it$ = AchievementManager.getAchievements().entrySet().iterator();
						while (it$.hasNext()) {
							Entry<String, Achievement> entry = it$.next();
							if (player.getAchievementManager().completeAchievement(entry.getValue())) {
								player.getAchievementManager().updateAmount(entry.getValue().getKey(), player.getAchievementManager().getAmount(entry.getValue().getKey()) - 1);
								player.getAchievementManager().removeAchievement(entry.getValue());
								System.out.println("Reduced achievement count of " + entry.getValue().getClass().getSimpleName() + " on " + acc + ".");
							}
						}
					}
					player.getFacade().setNoviteGamePoints(0);
					savePlayer(player, acc);
				}
			} catch (Exception e) {
				System.out.println("Error with: " + acc);
			}
		}
	}

	private static final String name = "j4gex.p";

}
