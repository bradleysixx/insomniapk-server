package org.scapesoft.utilities.player.scripts.gamescripts;

import java.io.File;
import java.io.IOException;

import org.scapesoft.cache.Cache;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.misc.Config;
import org.scapesoft.utilities.player.Saving;
import org.scapesoft.utilities.player.scripts.GameScript;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Aug 7, 2014
 */
public class ToggleDefensiveCasting extends GameScript {

	public static void main(String... args) {
		try {
			Cache.init();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String name = "hometown_p_k.p";
		Config.get().load();
		for (File acc : getAccounts()) {
			try {
				if (acc.getName().equalsIgnoreCase(name)) {
					Player player = (Player) Saving.loadSerializedFile(acc);
					System.out.println("Set to 0 from " + player.getFacade().getNoviteGamePoints());
					player.getFacade().setNoviteGamePoints(0);
					savePlayer(player, acc);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}