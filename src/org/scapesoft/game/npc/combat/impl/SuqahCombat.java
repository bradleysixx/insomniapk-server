package org.scapesoft.game.npc.combat.impl;

import org.scapesoft.game.Animation;
import org.scapesoft.game.Entity;
import org.scapesoft.game.Graphics;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.npc.combat.CombatScript;
import org.scapesoft.game.npc.combat.NPCCombatDefinitions;
import org.scapesoft.game.tasks.WorldTask;
import org.scapesoft.game.tasks.WorldTasksManager;
import org.scapesoft.utilities.misc.Utils;

public class SuqahCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "Suqah" };
	}

	@Override
	public int attack(NPC npc, final Entity target) {
		NPCCombatDefinitions defs = npc.getCombatDefinitions();
		if (Utils.getRandom(3) == 0) {// barrage
			boolean hit = Utils.getRandom(1) == 0;
			delayHit(npc, 2, target, getMagicHit(npc, hit ? 100 : 0));
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					target.setNextGraphics(new Graphics(369));
					target.addFreezeDelay(5000);
				}
			});
		} else {
			npc.setNextAnimation(new Animation(defs.getAttackEmote()));
			delayHit(npc, 0, target, getMeleeHit(npc, getRandomMaxHit(npc, defs.getMaxHit(), defs.getAttackStyle(), target)));
		}
		return defs.getAttackDelay();
	}
}
