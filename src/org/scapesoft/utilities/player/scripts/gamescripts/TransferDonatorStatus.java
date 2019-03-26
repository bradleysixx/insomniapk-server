package org.scapesoft.utilities.player.scripts.gamescripts;

import java.io.File;
import java.io.IOException;

import org.scapesoft.cache.Cache;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.game.player.ForumGroup;
import org.scapesoft.utilities.player.Saving;
import org.scapesoft.utilities.player.scripts.GameScript;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Dec 11, 2014
 */
@SuppressWarnings("unused")
public class TransferDonatorStatus extends GameScript {

	public static void main(String[] args) throws IOException {
		System.out.println(ForumGroup.ForumGroups.SUPPORT.ordinal());
		Cache.init();
		giveTotalPoints("volt.p", 10);
		//transferPoints("omfg_grapes.p", "volt.p");
	}
	
	private static void giveTotalPoints(String name, int amount) {
		for (File acc : getAccounts()) {
			if (acc.getName().equalsIgnoreCase(name)) {
				try {
					Player player = (Player) Saving.loadSerializedFile(acc);
					//player.getFacade().setTotalPointsPurchased(player.getFacade().getTotalPointsPurchased() + amount);
					System.out.println(player.getFacade().getTotalPointsPurchased());
					//savePlayer(player, acc, true);
					break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void transferPoints(String from, String to) {
		Object[] fromArray = null;
		for (File acc : getAccounts()) {
			if (acc.getName().equalsIgnoreCase(from)) {
				try {
					Player player = (Player) Saving.loadSerializedFile(acc);
					fromArray = new Object[] { player, acc };
					break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		Object[] toArray = null;
		for (File acc : getAccounts()) {
			if (acc.getName().equalsIgnoreCase(to)) {
				try {
					Player player = (Player) Saving.loadSerializedFile(acc);
					toArray = new Object[] { player, acc };
					break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if (fromArray == null || toArray == null) {
			System.out.println("[fromArray=" + fromArray + ", toArray=" + toArray + "] therefore stopped.");
			return;
		}
		Player toP = (Player) toArray[0];
		Player fromP = (Player) fromArray[0];
		toP.getFacade().setTotalPointsPurchased(toP.getFacade().getTotalPointsPurchased() + fromP.getFacade().getTotalPointsPurchased());
		fromP.getFacade().setTotalPointsPurchased(0);
		savePlayer(toP, (File) toArray[1], true);
		savePlayer(fromP, (File) fromArray[1], true);
	}

}
