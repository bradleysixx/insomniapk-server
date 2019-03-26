package org.scapesoft.api.event.command.impl;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.World;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.game.player.Rights;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Apr 3, 2014
 */
public class TeleToNpc extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.OWNER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[]{"teleton"};
	}

	@Override
	public void execute(Player player) {
		NPC n = World.getNPCs().get(Integer.parseInt(cmd[1]));
		if (n == null) {
			player.sendMessage("Npc not found!");
			return;
		}
		player.setNextWorldTile(n);
	}

}
