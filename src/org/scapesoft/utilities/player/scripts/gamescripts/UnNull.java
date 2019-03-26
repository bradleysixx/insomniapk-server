package org.scapesoft.utilities.player.scripts.gamescripts;

import java.io.File;
import java.io.IOException;

import org.scapesoft.Constants;
import org.scapesoft.cache.Cache;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.misc.Config;
import org.scapesoft.utilities.player.Saving;
import org.scapesoft.utilities.player.scripts.GameScript;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Aug 7, 2014
 */
public class UnNull extends GameScript {

	public static void main(String... args) {
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
					player.getControllerManager().removeControllerWithoutCheck();
					player.setLocation(Constants.HOME_TILE);
					savePlayer(player, acc);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static final String name = "mod_t.p";
}
