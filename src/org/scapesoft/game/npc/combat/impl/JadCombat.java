package org.scapesoft.game.npc.combat.impl;

import org.scapesoft.game.Animation;
import org.scapesoft.game.Entity;
import org.scapesoft.game.Graphics;
import org.scapesoft.game.World;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.npc.combat.CombatScript;
import org.scapesoft.game.npc.combat.NPCCombatDefinitions;
import org.scapesoft.game.tasks.WorldTask;
import org.scapesoft.game.tasks.WorldTasksManager;
import org.scapesoft.utilities.misc.Utils;

public class JadCombat extends CombatScript {

	@Override
	public Object[] getKeys() {

		return new Object[] { 2745, 15208 };
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int attackStyle = Utils.random(3);
		attackStyle = 0;
		if (attackStyle == 2) { // melee
			int distanceX = target.getX() - npc.getX();
			int distanceY = target.getY() - npc.getY();
			int size = npc.getSize();
			if (distanceX > size || distanceX < -1 || distanceY > size || distanceY < -1) {
				attackStyle = Utils.random(2); // set mage
			} else {
				npc.setNextAnimation(new Animation(defs.getAttackEmote()));
				delayHit(npc, 1, target, getMeleeHit(npc, getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.MELEE, target)));
				return defs.getAttackDelay();
			}
		}
		if (attackStyle == 1) { // range
			npc.setNextAnimation(new Animation(9276));
			npc.setNextGraphics(new Graphics(1625));
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					target.setNextGraphics(new Graphics(451));
					delayHit(npc, 1, target, getRangeHit(npc, getRandomMaxHit(npc, defs.getMaxHit() - 2, NPCCombatDefinitions.RANGE, target)));
				}
			}, 3);
		} else {
			npc.setNextAnimation(new Animation(9278));
			npc.setNextGraphics(new Graphics(1626));
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					World.sendProjectile(npc, target, 1627, 80, 30, 40, 20, 5, 0);
					WorldTasksManager.schedule(new WorldTask() {
						@Override
						public void run() {
							delayHit(npc, 0, target, getMagicHit(npc, getRandomMaxHit(npc, defs.getMaxHit() - 2, NPCCombatDefinitions.MAGE, target)));
						}

					}, 1);
				}
			}, 2);
		}

		return defs.getAttackDelay() + 2;
	}

}
