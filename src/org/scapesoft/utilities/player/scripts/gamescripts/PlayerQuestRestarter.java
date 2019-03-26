package org.scapesoft.utilities.player.scripts.gamescripts;

import java.io.File;
import java.io.IOException;

import org.scapesoft.cache.Cache;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.quests.QuestManager;
import org.scapesoft.game.player.quests.impl.Desert_Treasure;
import org.scapesoft.utilities.misc.Config;
import org.scapesoft.utilities.player.Saving;
import org.scapesoft.utilities.player.scripts.GameScript;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jul 15, 2014
 */
public class PlayerQuestRestarter extends GameScript {

	public static void main(String... args) {
		try {
			Cache.init();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Config.get().load();
		QuestManager.load();
		for (File acc : getAccounts()) {
			try {
				Player player = (Player) Saving.loadSerializedFile(acc);
				if (acc.getName().equalsIgnoreCase(name)) {
					if (player != null) {
						Desert_Treasure dt = (Desert_Treasure) player.getQuestManager().getProgressedQuest(QuestManager.getQuest(Desert_Treasure.class).getName());
						if (dt != null) {
							player.getFacade().getDesertTreasureKills().clear();
						}
					}
					savePlayer(player, acc);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static final String name = "sorryboutcha.p";

}
