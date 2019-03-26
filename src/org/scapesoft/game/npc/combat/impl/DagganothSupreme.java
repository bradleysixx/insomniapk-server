package org.scapesoft.game.npc.combat.impl;

import org.scapesoft.game.Animation;
import org.scapesoft.game.Entity;
import org.scapesoft.game.Graphics;
import org.scapesoft.game.World;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.npc.combat.CombatScript;
import org.scapesoft.game.npc.combat.NPCCombatDefinitions;
import org.scapesoft.utilities.game.npc.NPCCombatDefinitionsL;

/**
 * 
 * @author Tyluur <itstyluur@gmail.com>
 * @since Aug 6, 2013
 */
public class DagganothSupreme extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 2881, 2882, 2883 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = NPCCombatDefinitionsL.getNPCCombatDefinitions(npc.getId());
		int damage = 0;
		switch (npc.getId()) {
		case 2881: // supreme (range)
			damage = getRandomMaxHit(npc, 300, NPCCombatDefinitions.RANGE, target);
			npc.setNextAnimation(new Animation(defs.getAttackEmote()));
			npc.setNextGraphics(new Graphics(defs.getAttackGfx()));
			delayHit(npc, 2, target, getRangeHit(npc, damage));
			World.sendProjectile(npc, target, 475, 41, 16, 41, 35, 16, 0);
			break;
		case 2882: // prime (mage)
			damage = getRandomMaxHit(npc, 300, NPCCombatDefinitions.MAGE, target);
			npc.setNextAnimation(new Animation(defs.getAttackEmote()));
			npc.setNextGraphics(new Graphics(defs.getAttackGfx()));
			delayHit(npc, 2, target, getMagicHit(npc, damage));
			World.sendProjectile(npc, target, 2707, 41, 16, 41, 35, 16, 0);
			break;
		case 2883: // rex (melee)
			damage = getRandomMaxHit(npc, 300, NPCCombatDefinitions.MELEE, target);
			npc.setNextAnimation(new Animation(defs.getAttackEmote()));
			delayHit(npc, 0, target, getMeleeHit(npc, damage));
			break;
		}
		return defs.getAttackDelay();
	}

}
