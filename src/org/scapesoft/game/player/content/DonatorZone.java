package org.scapesoft.game.player.content;

import java.util.List;

import org.scapesoft.game.ForceTalk;
import org.scapesoft.game.World;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.tasks.WorldTask;
import org.scapesoft.game.tasks.WorldTasksManager;
import org.scapesoft.utilities.misc.Utils;

public class DonatorZone {

	/**
	 * Teleports the player into the donator zone
	 * 
	 * @param player
	 */
	public static void enterDonatorzone(final Player player) {
		Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2582, 3910, 0));
		for (int regionId : player.getMapRegionsIds()) {
			List<Integer> npcIndexes = World.getRegion(regionId).getNPCsIndexes();
			if (npcIndexes != null) {
				for (int npcIndex : npcIndexes) {
					final NPC n = World.getNPCs().get(npcIndex);
					if (n == null || n.getId() != 5445)
						continue;
					WorldTasksManager.schedule(new WorldTask() {
						@Override
						public void run() {
							final int random = Utils.getRandom(3);
							if (random == 0)
								n.setNextForceTalk(new ForceTalk("Everyone welcome " + player.getDisplayName() + " to the donator zone."));
							else if (random == 1)
								n.setNextForceTalk(new ForceTalk(player.getDisplayName() + " has just joined the penguin zone."));
							else if (random == 2)
								n.setNextForceTalk(new ForceTalk("Ma boi " + player.getDisplayName() + " has just joined the penguin zone."));
							else if (random == 3)
								n.setNextForceTalk(new ForceTalk("Who else wouldnt want " + player.getDisplayName() + " from joining the penguin zone."));
						}
					}, 4);
				}
			}
		}
	}
}
