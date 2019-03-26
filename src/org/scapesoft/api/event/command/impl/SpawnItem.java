package org.scapesoft.api.event.command.impl;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.item.Item;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.game.player.Rights;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Mar 19, 2014
 */
public class SpawnItem extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.OWNER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "item" };
	}

	@Override
	public void execute(Player player) {
		String[] cmd = command.split(" ");
		try {
			Item item = new Item(Integer.parseInt(cmd[1]), cmd.length == 2 ? 1 : Integer.parseInt(cmd[2]));
			player.getInventory().addItem(item);
		} catch (Exception e) {
			player.sendMessage("Format- ::item id amount");
		}
	}

}
