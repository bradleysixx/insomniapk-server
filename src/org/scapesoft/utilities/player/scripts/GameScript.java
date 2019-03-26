package org.scapesoft.utilities.player.scripts;

import java.io.File;
import java.io.IOException;

import org.scapesoft.game.item.Item;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.player.Saving;

/**
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2012-12-30
 */
public abstract class GameScript {

	public static File[] getAccounts() {
		File dir = new File(Saving.PATH);
		return dir.listFiles();
	}

	public static Player getPlayer(File file) throws ClassNotFoundException, IOException {
		return (Player) Saving.loadSerializedFile(file);
	}

	public static void savePlayer(Player player, File account, boolean... print) {
		try {
			Saving.storeSerializableClass(player, account);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (print.length > 0 && print[0]) {
			System.out.println("Saved " + account + "");
		}
	}

	public static Item[] getBankItems(Player player) {
		return player.getBank().generateContainer();
	}

}
