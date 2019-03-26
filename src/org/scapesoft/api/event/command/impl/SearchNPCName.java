package org.scapesoft.api.event.command.impl;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.cache.loaders.NPCDefinitions;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.game.player.Rights;
import org.scapesoft.utilities.misc.Utils;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Mar 20, 2014
 */
public class SearchNPCName extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.OWNER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "nn" };
	}

	@Override
	public void execute(Player player) {
		for (int i = 0; i < Utils.getNPCDefinitionsSize(); i++) {
			NPCDefinitions npcDefs = NPCDefinitions.getNPCDefinitions(i);
			if (npcDefs != null) {
				if (npcDefs.getName().toLowerCase().contains(getCompleted(cmd, 1))) {
					StringBuilder bldr = new StringBuilder();
					for (String o : npcDefs.options) {
						bldr.append(o + ", ");
					}
					player.getPackets().sendMessage(99, "NPC[id=" + i + ", lvl=" + npcDefs.combatLevel + ", name=" + npcDefs.getName() + ", size=" + npcDefs.size + ", options=" + bldr.toString() + "]", player);
				}
			}
		}
	}

}
