package org.scapesoft.api.event.command.impl;

import java.util.Iterator;
import java.util.List;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.World;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.console.gson.GsonHandler;
import org.scapesoft.utilities.console.gson.impl.NPCAutoSpawn;
import org.scapesoft.utilities.game.npc.NPCSpawning;
import org.scapesoft.utilities.game.player.Rights;


/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jul 31, 2014
 */
public class MassDeleteSpawns extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.OWNER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "mdn" };
	}

	@Override
	public void execute(Player player) {
		List<Integer> npcs = player.getRegion().getNPCsIndexes();
		if (npcs == null) {
			return;
		}
		List<NPCSpawning> spawns = ((NPCAutoSpawn) GsonHandler.getJsonLoader(NPCAutoSpawn.class)).load();
		Iterator<NPCSpawning> it = spawns.iterator();
		for (Integer index : npcs) {
			NPC npc = World.getNPCs().get(index);
			if (npc == null || !npc.withinDistance(player, 10))
				continue;
			while (it.hasNext()) {
				NPCSpawning spawn = it.next();
				if (spawn == null) {
					continue;
				}
				if (spawn.getId() == npc.getId() && spawn.getX() == npc.getStartTile().getX() && spawn.getY() == npc.getStartTile().getY() && spawn.getZ() == npc.getStartTile().getPlane()) {
					it.remove();
				}
			}
			((NPCAutoSpawn) GsonHandler.getJsonLoader(NPCAutoSpawn.class)).save(spawns);
			npc.finish();
			System.out.println("Removed a monster spawn! [" + npc + "]");
		}
	}

}
