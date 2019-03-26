package org.scapesoft.api.event.command.impl;

import java.util.List;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.Hit;
import org.scapesoft.game.World;
import org.scapesoft.game.Hit.HitLook;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.game.player.Rights;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jun 30, 2014
 */
public class KillLocalNPCS extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.OWNER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "killnpcs" };
	}

	@Override
	public void execute(Player player) {
		List<Integer> indexes = player.getRegion().getNPCsIndexes();
		for (Integer index : indexes) {
			NPC npc = World.getNPCs().get(index);
			if (npc == null || !npc.getDefinitions().hasAttackOption())
				continue;
			npc.applyHit(new Hit(player, npc.getHitpoints(),
					HitLook.DESEASE_DAMAGE));
		}
	}

}