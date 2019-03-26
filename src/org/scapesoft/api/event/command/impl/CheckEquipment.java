package org.scapesoft.api.event.command.impl;

import java.util.ArrayList;
import java.util.List;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.api.event.listeners.interfaces.Scrollable;
import org.scapesoft.game.World;
import org.scapesoft.game.item.Item;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.game.player.Rights;
import org.scapesoft.utilities.misc.Utils;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jul 10, 2014
 */
public class CheckEquipment extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.MODERATOR;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "checkequip", "checke" };
	}

	@Override
	public void execute(Player player) {
		String name = getCompleted(cmd, 1).replaceAll(" ", "_");
		if (World.containsPlayer(name)) {
			Player target = World.getPlayer(name);
			List<String> messages = new ArrayList<String>();
			messages.add(target.getUsername() + "'s equipment has: ");
			for (Item item : target.getEquipment().getItems().toArray()) {
				if (item == null)
					continue;
				messages.add(Utils.format(item.getAmount()) + "x " + item.getName() + ". [" + item.getId() + "]");
			}
			Scrollable.sendQuestScroll(player, name + "'s equipment", messages.toArray(new String[messages.size()]));
			
		} else {
			player.sendMessage(name + " is an invalid name for a player.");
		}
	}

}
