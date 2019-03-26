package org.scapesoft.api.event.command.impl;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.World;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.game.player.Rights;

public final class MassNpcs extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.OWNER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "mnpc" };
	}

	@Override
	public void execute(Player player) {
		try {
			int npcId = Integer.parseInt(cmd[1]);
			int perimiter = Integer.parseInt(cmd[2]);
			for (int i = (player.getX() - perimiter); i < (player.getX() + perimiter); i++) {
				for (int x = (player.getY() - perimiter); x < (player.getY() + perimiter); x++) {
					WorldTile tile = new WorldTile(i, x, player.getPlane());
					NPC n = World.spawnNPC(npcId, tile, -1, true, true);
					n.getTemporaryAttributtes().put("droploot", false);
				}
			}
		} catch (NumberFormatException e) {
			player.getPackets().sendPanelBoxMessage("Use: ::mnpc id(Integer)");
		}
	}

}