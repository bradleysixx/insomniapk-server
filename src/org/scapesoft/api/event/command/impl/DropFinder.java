package org.scapesoft.api.event.command.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.scapesoft.Constants;
import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.api.event.listeners.interfaces.Scrollable;
import org.scapesoft.cache.loaders.ItemDefinitions;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.console.gson.impl.NPCDropManager;
import org.scapesoft.utilities.game.npc.drops.Drop;
import org.scapesoft.utilities.game.player.Rights;
import org.scapesoft.utilities.misc.ChatColors;
import org.scapesoft.utilities.misc.Utils;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Apr 4, 2014
 */
public class DropFinder extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.MODERATOR;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "finddrop" };
	}

	@Override
	public void execute(Player player) {
		try {
			player.lock();
			String itemToFind = getCompleted(cmd, 1).replaceAll("_", " ");
			Map<String, List<Drop>> dropMap = NPCDropManager.getDropMap();
			Iterator<Entry<String, List<Drop>>> it = dropMap.entrySet().iterator();

			List<String> results = new ArrayList<String>();

			while (it.hasNext()) {
				Entry<String, List<Drop>> entry = it.next();
				List<Drop> drops = entry.getValue();
				String name = entry.getKey();
				for (Drop drop : drops) {
					int itemId = drop.getItemId();
					String itemName = ItemDefinitions.getItemDefinitions(itemId).name;
					String rate = Utils.formatPlayerNameForDisplay(String.valueOf(drop.getRate()));

					if (itemName.toLowerCase().contains(itemToFind.toLowerCase())) {
						results.add("<col=" + ChatColors.PURPLE + ">" + name + ": <col=" + ChatColors.MAROON + ">" + drop.getMaxAmount() + "</col>x " + itemName + " <col=" + ChatColors.MAROON + ">" + rate);
					}
				}
			}
			player.unlock();

			Scrollable.sendScroll(player, "Drop Results for " + itemToFind + " (" + results.size() + ")", results.toArray(new String[results.size()]));
		} catch (Exception e) {
			if (!Constants.isVPS) {
				e.printStackTrace();
			}
			player.sendMessage("Invalid format! Use ::finddrop [ITEMNAME]. E.G ::finddrop visage");
		}
	}

}
