package org.scapesoft.game.npc.combat.impl;

import org.scapesoft.game.Animation;
import org.scapesoft.game.Entity;
import org.scapesoft.game.ForceTalk;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.npc.combat.CombatScript;
import org.scapesoft.game.npc.combat.NPCCombatDefinitions;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.misc.Utils;

public class BorkCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "Bork" };
	}

	public boolean spawnOrk = false;

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions cdef = npc.getCombatDefinitions();
		if (npc.getHitpoints() <= (cdef.getHitpoints() * 0.4) && !spawnOrk) {
			Player player = (Player) target;
			npc.setNextForceTalk(new ForceTalk("Come to my aid, brothers!"));
			player.getControllerManager().startController("BorkControler", 1, npc);
			spawnOrk = true;
		}
		npc.setNextAnimation(new Animation(Utils.getRandom(1) == 0 ? cdef.getAttackEmote() : 8757));
		delayHit(npc, 0, target, getMeleeHit(npc, getRandomMaxHit(npc, cdef.getMaxHit(), -1, target)));
		return cdef.getAttackDelay();
	}

}
