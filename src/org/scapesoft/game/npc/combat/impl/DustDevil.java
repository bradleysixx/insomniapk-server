package org.scapesoft.game.npc.combat.impl;

import org.scapesoft.game.Animation;
import org.scapesoft.game.Entity;
import org.scapesoft.game.Hit;
import org.scapesoft.game.Hit.HitLook;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.npc.combat.CombatScript;
import org.scapesoft.game.npc.combat.NPCCombatDefinitions;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.content.slayer.Slayer;
import org.scapesoft.utilities.misc.Utils;

public class DustDevil extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 1624 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		NPCCombatDefinitions def = npc.getCombatDefinitions();
		if (!Slayer.hasMask(target)) {
			Player targetPlayer = (Player) target;
			targetPlayer.applyHit(new Hit(npc, Utils.random(20), HitLook.REGULAR_DAMAGE));
			targetPlayer.getPackets().sendGameMessage("The dust devil's smoke suffocates you.");
			delayHit(npc, 1, target, getMeleeHit(npc, targetPlayer.getMaxHitpoints() / 4));
		} else {
			delayHit(npc, 1, target, getMeleeHit(npc, getRandomMaxHit(npc, npc.getMaxHit(), def.getAttackStyle(), target)));
		}
		npc.setNextAnimation(new Animation(def.getAttackEmote()));
		return def.getAttackDelay();
	}

}
