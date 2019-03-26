package org.scapesoft.utilities.player.scripts.gamescripts;

import java.io.File;
import java.io.IOException;

import org.scapesoft.cache.Cache;
import org.scapesoft.game.player.Bank;
import org.scapesoft.game.player.Equipment;
import org.scapesoft.game.player.Inventory;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.console.gson.GsonHandler;
import org.scapesoft.utilities.misc.Config;
import org.scapesoft.utilities.player.Saving;
import org.scapesoft.utilities.player.scripts.GameScript;

/**
 * @author Tyluur<itstyluur@gmail.com
 * @since Oct 6, 2014
 */
public class EconomyResetter extends GameScript {

	public static void main(String... args) {
		try {
			Cache.init();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		GsonHandler.initialize();
		Config.get().load();
		for (File acc : getAccounts()) {
			if (USING_NAME && acc.getName().equalsIgnoreCase(NAME)) {
				try {
					Player player = (Player) Saving.loadSerializedFile(acc);
					player.setBank(new Bank());
					player.setInventory(new Inventory());
					player.setEquipment(new Equipment());
					player.setFamiliar(null);
					player.getLoyaltyManager().forcePoints(0);
					player.getAppearence().forceTitle(-1);
					player.getFacade().setGoldPoints(0);
					player.getFacade().setTotalPointsPurchased(0);
					savePlayer(player, acc);
				} catch (Exception e) {
					e.printStackTrace();
					System.err.println("Error with account: " + acc);
				}
			}
		}
	}
	
	private static final String NAME = "jasper.p";
	private static final boolean USING_NAME = true;

}