package org.scapesoft.game.player.dialogues.impl;

import org.scapesoft.api.event.listeners.interfaces.GoldPointShopListener;
import org.scapesoft.api.event.listeners.interfaces.GoldPointShopListener.GoldShop;
import org.scapesoft.game.player.dialogues.Dialogue;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Jan 17, 2015
 */
public class GoldPointShopD extends Dialogue {

	@Override
	public void start() {
		sendShopOptions();
	}

	@Override
	public void run(int interfaceId, int option) {
		switch (stage) {
		case -1:
			switch (option) {
			case FIRST:
				GoldPointShopListener.refreshShop(player, GoldShop.MEMBER_ONLY_STOCK);
				break;
			case SECOND:
				GoldPointShopListener.refreshShop(player, GoldShop.RARES);
				break;
			case THIRD:
				GoldPointShopListener.refreshShop(player, GoldShop.WEAPONS);
				break;
			case FOURTH:
				GoldPointShopListener.refreshShop(player, GoldShop.UNTRADEABLES);
				break;
			case FIFTH:
				GoldPointShopListener.refreshShop(player, GoldShop.ARMOUR);
				break;
			}
			sendShopOptions();
			break;
		}
	}

	@Override
	public void finish() {
	}

	public void sendShopOptions() {
		sendOptionsDialogue("Select a Shop", "Member Only Stock", "Rares", "Weapons", "Untradeables", "Armour");
		stage = -1;
	}

}
