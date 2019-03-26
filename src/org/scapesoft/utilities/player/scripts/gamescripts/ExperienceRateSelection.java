package org.scapesoft.utilities.player.scripts.gamescripts;

import java.io.File;
import java.io.IOException;

import org.scapesoft.cache.Cache;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.player.Saving;
import org.scapesoft.utilities.player.scripts.GameScript;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jul 10, 2014
 */
public class ExperienceRateSelection extends GameScript {

	public static void main(String[] args) throws IOException {
		Cache.init();
		for (File acc : getAccounts()) {
			try {
				Player player = (Player) Saving.loadSerializedFile(acc);
				if (player != null) {
					player.setUsername(acc.getName());
					player.getFacade().setModifier(new double[3]);
				}
				savePlayer(player, acc);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}