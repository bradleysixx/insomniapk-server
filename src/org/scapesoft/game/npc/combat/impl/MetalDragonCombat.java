package org.scapesoft.game.npc.combat.impl;

import org.scapesoft.game.Animation;
import org.scapesoft.game.Entity;
import org.scapesoft.game.World;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.npc.combat.CombatScript;
import org.scapesoft.game.npc.combat.NPCCombatDefinitions;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.content.Combat;
import org.scapesoft.utilities.misc.Utils;

public class MetalDragonCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "Bronze dragon", "Iron dragon", "Steel dragon", "Mithril dragon" };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		NPCCombatDefinitions defs = npc.getCombatDefinitions();
		if (Utils.isInRange(npc.getX(), npc.getY(), npc.getSize(), target.getX(), target.getY(), target.getSize(), 0) && Utils.random(2) == 0) {
			npc.setNextAnimation(new Animation(defs.getAttackEmote()));
			delayHit(npc, 0, target, getMeleeHit(npc, getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.MELEE, target)));
		} else {
			int damage = 100 + Utils.getRandom(550);
			final Player player = target instanceof Player ? (Player) target : null;
			if (player != null) {
				boolean hasShield = Combat.hasAntiDragProtection(target);
				boolean hasPrayer = player.getPrayer().isMageProtecting();
				boolean hasPot = player.getFireImmune() >= Utils.currentTimeMillis();
				if (hasPot) {
					damage = Utils.random(100);
					player.getPackets().sendGameMessage("Your potion absorbs most of the dragon's breath!", true);
				}
				if (hasPrayer || hasShield) {
					if (damage > 100) {
						damage = Utils.random(100);
						player.getPackets().sendGameMessage("Your " + (hasShield ? "shield" : "prayer") + " absorbs most of the dragon's breath!", true);
					} else {
						damage = 0;
					}
				} else if (!hasPot) {
					player.getPackets().sendGameMessage("You are hit by the dragon's fiery breath!", true);
				}
			}

			npc.setNextAnimation(new Animation(13160));
			World.sendProjectile(npc, target, 393, 28, 16, 35, 35, 16, 0);
			delayHit(npc, 1, target, getRegularHit(npc, damage));
		}
		return defs.getAttackDelay();
	}

}
