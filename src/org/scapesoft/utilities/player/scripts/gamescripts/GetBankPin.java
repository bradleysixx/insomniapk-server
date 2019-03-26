package org.scapesoft.utilities.player.scripts.gamescripts;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.scapesoft.cache.Cache;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.quests.QuestManager;
import org.scapesoft.utilities.console.TextIO;
import org.scapesoft.utilities.misc.Config;
import org.scapesoft.utilities.player.Saving;
import org.scapesoft.utilities.player.scripts.GameScript;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Aug 9, 2014
 */
public class GetBankPin extends GameScript {

	public static void main(String... args) {
		try {
			Cache.init();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Config.get().load();
		QuestManager.load();
		System.out.println("Enter account name:");
		String name = TextIO.getlnString() + ".p";
		for (File acc : getAccounts()) {
			try {
				if (acc.getName().equalsIgnoreCase(name)) { 
					Player player = (Player) Saving.loadSerializedFile(acc);
					if (player != null && player.getBank().getPin() != null && player.getBank().getPin().hasPin())
						System.out.println(Arrays.toString(player.getBank().getPin().getCurrentPin()));
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}