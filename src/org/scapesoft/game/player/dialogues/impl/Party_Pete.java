package org.scapesoft.game.player.dialogues.impl;

import org.scapesoft.api.event.listeners.interfaces.GoldPointShopListener;
import org.scapesoft.api.event.listeners.interfaces.GoldPointShopListener.GoldShop;
import org.scapesoft.game.player.dialogues.Dialogue;
import org.scapesoft.utilities.console.gson.GsonHandler;
import org.scapesoft.utilities.console.gson.impl.ShopsLoader;
import org.scapesoft.utilities.misc.ChatColors;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Apr 4, 2014
 */
public class Party_Pete extends Dialogue {

	@Override
	public void start() {
		sendOptionsDialogue("Select an Option", "Purchase <col=" + ChatColors.BLUE + ">Vote</col> Rewards", "Purchase <col=" + ChatColors.BLUE + ">GoldPoint</col> Rewards", "Purchase <col=" + ChatColors.BLUE + ">Achievement</col> Rewards", "Purchase <col=" + ChatColors.BLUE + ">Loyalty</col> Rewards");
	}

	@Override
	public void run(int interfaceId, int option) {
		end();
		switch (option) {
		case FIRST:
			GsonHandler.<ShopsLoader> getJsonLoader(ShopsLoader.class).openShop(player, "Vote Exchange");
			break;
		case SECOND:
			end();
			GoldPointShopListener.openShop(player, GoldShop.RARES);
			player.getDialogueManager().startDialogue(GoldPointShopD.class);
			//DonationShopListener.display(player);
			//JsonHandler.<ShopsLoader> getJsonLoader(ShopsLoader.class).openShop(player, "Gold Points Rewards");
			break;
		case THIRD:
			GsonHandler.<ShopsLoader> getJsonLoader(ShopsLoader.class).openShop(player, "Achievement Rewards");
			break;
		case FOURTH:
			player.getLoyaltyManager().displayStore();
			break;
		}
	}

	@Override
	public void finish() {

	}

}
