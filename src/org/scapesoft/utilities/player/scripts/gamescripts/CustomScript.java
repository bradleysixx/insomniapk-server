package org.scapesoft.utilities.player.scripts.gamescripts;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.scapesoft.cache.Cache;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.Skills;
import org.scapesoft.utilities.player.Saving;
import org.scapesoft.utilities.player.scripts.GameScript;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Aug 22, 2014
 */
@SuppressWarnings("unused")
public class CustomScript extends GameScript {

	public static void main(String[] args) throws IOException {
		Cache.init();
		long start = System.currentTimeMillis();
		for (File acc : getAccounts()) {
			try {
				Player player = (Player) Saving.loadSerializedFile(acc);
				if (player != null) {
					System.out.println(name + " has bought $" + player.getFacade().getTotalPointsPurchased());
					break;
				}
			} catch (Exception e) {
				System.out.println(acc);
				e.printStackTrace();
			}
		}
	}

	private static final String name = "arthas.p";

}