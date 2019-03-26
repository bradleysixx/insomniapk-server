package org.scapesoft.game.npc.combat.impl;

import org.scapesoft.game.Animation;
import org.scapesoft.game.Entity;
import org.scapesoft.game.World;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.npc.combat.CombatScript;
import org.scapesoft.game.npc.combat.NPCCombatDefinitions;

/**
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since Aug 6, 2013
 */
public class Spinolyp extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 2892, 2894, 2896 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int damage = getRandomMaxHit(npc, 300, NPCCombatDefinitions.MAGE, target);
		npc.setNextAnimation(new Animation(defs.getAttackEmote()));
		delayHit(npc, 2, target, getMagicHit(npc, damage));
		World.sendProjectile(npc, target, 94, 45, 50, 100, 10, 35, 0);
		// Projectile.create(attacker.getCentreLocation(),
		// victim.getCentreLocation(), 94, 45, 50, clientSpeed, 10, 35,
		// victim.getProjectileLockonIndex(), 10, 48);
		return defs.getAttackDelay();
	}

}
