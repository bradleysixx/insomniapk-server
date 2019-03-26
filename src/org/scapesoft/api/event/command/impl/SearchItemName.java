package org.scapesoft.api.event.command.impl;

import org.scapesoft.Constants;
import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.item.Item;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.game.player.Rights;
import org.scapesoft.utilities.misc.ChatColors;
import org.scapesoft.utilities.misc.Utils;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Mar 20, 2014
 */
public class SearchItemName extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.OWNER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "itemn" };
	}

	@Override
	public void execute(Player player) {
		StringBuilder bldr = new StringBuilder();
		for (int i = 0; i < Utils.getItemDefinitionsSize(); i++) {
			Item item = new Item(i);
			if (item.getDefinitions().getName().toLowerCase().contains(getCompleted(cmd, 1).toLowerCase())) {
				bldr.append(i + ", ");
				player.getPackets().sendMessage(99, "[<col=FF0000>ITEM</col>] <col=" + ChatColors.LIGHT_BLUE + ">" + item.getDefinitions().getName() + "</col> - ID: " + item.getId() + "", player);
			}
		}
		if (Constants.DEBUG) {
			System.out.println(bldr.toString());
		}
	}

}
