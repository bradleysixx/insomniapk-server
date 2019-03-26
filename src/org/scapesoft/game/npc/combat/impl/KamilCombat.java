package org.scapesoft.game.npc.combat.impl;

import org.scapesoft.game.Animation;
import org.scapesoft.game.Entity;
import org.scapesoft.game.ForceTalk;
import org.scapesoft.game.Graphics;
import org.scapesoft.game.World;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.npc.combat.CombatScript;
import org.scapesoft.game.npc.combat.NPCCombatDefinitions;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.misc.Utils;

/**
 * 
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2012-12-12
 */
public class KamilCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "Kamil" };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int damage = getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.MAGE, target);
		if (Utils.random(10) > 5) {
			npc.setNextForceTalk(new ForceTalk("Sallamakar Ro!"));
			npc.setNextAnimation(new Animation(1979));
			World.sendProjectile(npc, target, 368, 60, 32, 50, 50, 0, 0);
			delayHit(npc, 2, target, getMagicHit(npc, damage));
			if (damage > 0 && target.getFrozenBlockedDelay() < Utils.currentTimeMillis()) {
				if (target instanceof Player) {
					((Player) target).sendMessage("You have been frozen!");
				}
				target.addFreezeDelay(5000, true);
				target.setNextGraphics(new Graphics(369));
			}
		} else {
			npc.setNextAnimation(new Animation(defs.getAttackEmote()));
			delayHit(npc, 0, target, getMeleeHit(npc, damage));
		}
		return defs.getAttackDelay();
	}

}
