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
 * @since Jun 30, 2014
 */
public class SpawnsClearer extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.OWNER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "clearspawns" };
	}

	@Override
	public void execute(Player player) {
		List<Integer> npcIndexes = player.getRegion().getNPCsIndexes();
		if (npcIndexes == null)
			return;
		for (Integer index : npcIndexes) {
			NPC npc = World.getNPCs().get(index);
			if (npc == null || !npc.withinDistance(player))
				continue;
			List<NPCSpawning> spawns = ((NPCAutoSpawn) GsonHandler.getJsonLoader(NPCAutoSpawn.class)).load();
			Iterator<NPCSpawning> it = spawns.iterator();
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
		}
	}

}
