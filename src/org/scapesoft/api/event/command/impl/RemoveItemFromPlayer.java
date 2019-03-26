package org.scapesoft.api.event.command.impl;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.World;
import org.scapesoft.game.item.Item;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.game.player.Rights;
import org.scapesoft.utilities.player.Saving;

public class RemoveItemFromPlayer extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.OWNER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "removeitem" };
	}

	@Override
	public void execute(Player player) {
		String[] cmd = command.split(" ");
		String name = cmd[1].replaceAll("_", " ");
		int targetId = Integer.parseInt(cmd[2]);
		Player target = World.getPlayerByDisplayName(name);
		if (target == null) {
			target = Saving.loadPlayer(name);
		}
		if (target == null) {
			player.sendMessage("No such player!");
			return;
		}
		target.removeItemCompletely(new Item(targetId));
	}

}
