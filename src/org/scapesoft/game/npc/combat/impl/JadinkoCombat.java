package org.scapesoft.game.npc.combat.impl;

import org.scapesoft.game.Animation;
import org.scapesoft.game.Entity;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.npc.combat.CombatScript;
import org.scapesoft.game.npc.combat.NPCCombatDefinitions;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.misc.Utils;

public class JadinkoCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 13820, 13821, 13822 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int distanceX = target.getX() - npc.getX();
		int distanceY = target.getY() - npc.getY();
		int size = npc.getSize();
		if (target instanceof Player) {
			Player player = (Player) target;
			if (player.getPrayer().usingPrayer(0, 17) || player.getPrayer().usingPrayer(1, 7)) {
				npc.setForceFollowClose(true);
				meleeAttack(npc, target);
				return defs.getAttackDelay();
			} else {
				npc.setForceFollowClose(false);
				if ((distanceX > size || distanceX < -1 || distanceY > size || distanceY < -1)) {
					rangeAttack(npc, target);
					return defs.getAttackDelay();
				} else {
					switch (Utils.random(2)) {
						case 0:
							rangeAttack(npc, target);
							break;
						case 1:
						default:
							meleeAttack(npc, target);
					}
				}
				return defs.getAttackDelay();
			}
		} else {
			return defs.getAttackDelay();
		}
	}

	private void rangeAttack(NPC npc, Entity target) {
		npc.setNextAnimation(new Animation(npc.getId() == 13820 ? 3031 : 3215));
		delayHit(npc, 2, target, getMagicHit(npc, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), NPCCombatDefinitions.MAGE, target)));
	}

	private void meleeAttack(NPC npc, Entity target) {
		npc.setNextAnimation(new Animation(npc.getId() == 13820 ? 3009 : 3214));
		delayHit(npc, 0, target, getMeleeHit(npc, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), NPCCombatDefinitions.MELEE, target)));
	}
}
