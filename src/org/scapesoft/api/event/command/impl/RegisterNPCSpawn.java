package org.scapesoft.api.event.command.impl;

import java.util.List;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.World;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.console.gson.GsonHandler;
import org.scapesoft.utilities.console.gson.impl.NPCAutoSpawn;
import org.scapesoft.utilities.console.gson.impl.NPCAutoSpawn.Direction;
import org.scapesoft.utilities.game.npc.NPCSpawning;
import org.scapesoft.utilities.game.player.Rights;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Mar 21, 2014
 */
public class RegisterNPCSpawn extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.OWNER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "n" };
	}

	@Override
	public void execute(Player player) {
		NPCAutoSpawn autospawn = GsonHandler.getJsonLoader(NPCAutoSpawn.class);
		List<NPCSpawning> spawns = autospawn.load();
		int id = Integer.parseInt(cmd[1]);
		Direction direction = cmd.length > 2 ? Direction.getDirection(cmd[2]) : Direction.NORTH;
		spawns.add(new NPCSpawning(player.getX(), player.getY(), player.getPlane(), id, direction));
		autospawn.save(spawns);
		World.spawnNPC(id, player, -1, true);
	}

}