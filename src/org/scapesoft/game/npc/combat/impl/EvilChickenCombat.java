package org.scapesoft.game.npc.combat.impl;

import org.scapesoft.game.Animation;
import org.scapesoft.game.Entity;
import org.scapesoft.game.ForceTalk;
import org.scapesoft.game.Graphics;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.npc.combat.CombatScript;
import org.scapesoft.game.npc.combat.NPCCombatDefinitions;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.misc.Utils;

public class EvilChickenCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "Evil Chicken" };
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		npc.setNextAnimation(new Animation(defs.getAttackEmote()));
		switch (Utils.getRandom(5)) {
			case 0:
				npc.setNextForceTalk(new ForceTalk("Bwuk"));
				break;
			case 1:
				npc.setNextForceTalk(new ForceTalk("Bwuk bwuk bwuk"));
				break;
			case 2:
				String name = "";
				if (target instanceof Player) {
					name = ((Player) target).getDisplayName();
				}
				npc.setNextForceTalk(new ForceTalk("Flee from me, " + name));
				break;
			case 3:
				name = "";
				if (target instanceof Player) {
					name = ((Player) target).getDisplayName();
				}
				npc.setNextForceTalk(new ForceTalk("Begone, " + name));
				break;
			case 4:
				npc.setNextForceTalk(new ForceTalk("Bwaaaauuuuk bwuk bwuk"));
				break;
			case 5:
				npc.setNextForceTalk(new ForceTalk("MUAHAHAHAHAAA!"));
				break;
		}
		target.setNextGraphics(new Graphics(337));
		delayHit(npc, 0, target, getMagicHit(npc, getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.MAGE, target)));
		return defs.getAttackDelay();
	}
}
