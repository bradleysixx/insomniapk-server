package org.scapesoft.api.event.command.impl;

import java.util.Iterator;
import java.util.List;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.cache.loaders.NPCDefinitions;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.console.gson.GsonHandler;
import org.scapesoft.utilities.console.gson.impl.NPCAutoSpawn;
import org.scapesoft.utilities.console.gson.impl.NPCAutoSpawn.Direction;
import org.scapesoft.utilities.game.npc.NPCSpawning;
import org.scapesoft.utilities.game.player.Rights;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jun 14, 2014
 */
public class ChangeDirection extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.OWNER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "changedir" };
	}

	@Override
	public void execute(Player player) {
		Direction direction = Direction.valueOf(getCompleted(cmd, 1));
		List<NPCSpawning> spawns = ((NPCAutoSpawn) GsonHandler.getJsonLoader(NPCAutoSpawn.class)).load();
		Iterator<NPCSpawning> it = spawns.iterator();
		while (it.hasNext()) {
			NPCSpawning spawn = it.next();
			if (spawn == null)
				continue;
			if (spawn.getX() == player.getX() && spawn.getY() == player.getY() && spawn.getZ() == player.getPlane()) {
				System.out.println("Direction: " + direction + ", Found " + spawn.getId() + " at your coords[" + NPCDefinitions.getNPCDefinitions(spawn.getId()).getName() + "]");
				spawn.setDirection(direction);
			}
		}
		((NPCAutoSpawn) GsonHandler.getJsonLoader(NPCAutoSpawn.class)).save(spawns);
	}

}