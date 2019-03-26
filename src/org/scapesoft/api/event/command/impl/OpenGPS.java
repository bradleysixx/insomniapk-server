package org.scapesoft.api.event.command.impl;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.api.event.listeners.interfaces.GoldPointShopListener;
import org.scapesoft.api.event.listeners.interfaces.GoldPointShopListener.GoldShop;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.dialogues.impl.GoldPointShopD;
import org.scapesoft.utilities.game.player.Rights;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Jan 17, 2015
 */
public class OpenGPS extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.OWNER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "opengps" };
	}

	@Override
	public void execute(Player player) {
		GoldPointShopListener.openShop(player, GoldShop.RARES);
		player.getDialogueManager().startDialogue(GoldPointShopD.class);
	}

}
