package org.scapesoft.utilities.game.npc;

import java.util.List;

import org.scapesoft.game.World;
import org.scapesoft.utilities.console.gson.GsonHandler;
import org.scapesoft.utilities.console.gson.impl.NPCAutoSpawn;

public final class NPCSpawns {

	public static final void loadNPCSpawns(int regionId) {
		NPCAutoSpawn autoSpawn = GsonHandler.getJsonLoader(NPCAutoSpawn.class);
		List<NPCSpawning> spawns = autoSpawn.getSpawns(regionId);
		if (spawns == null) {
			return;
		}
		for (NPCSpawning spawn : spawns) {
			World.spawnNPC(spawn.getId(), spawn.getTile(), -1, true);
		}
	}
}
