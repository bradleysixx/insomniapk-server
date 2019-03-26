package org.scapesoft.game.npc.combat.impl;

import org.scapesoft.game.Animation;
import org.scapesoft.game.Entity;
import org.scapesoft.game.ForceTalk;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.npc.combat.CombatScript;
import org.scapesoft.game.npc.combat.NPCCombatDefinitions;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.content.slayer.Slayer;
import org.scapesoft.utilities.misc.Utils;

public class BansheeCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "Banshee", "Mighty banshee" };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		NPCCombatDefinitions def = npc.getCombatDefinitions();
		if (!Slayer.hasEarmuffs(target)) {
			Player targetPlayer = (Player) target;
			if (!targetPlayer.getPrayer().isMeleeProtecting()) {
				int randomSkill = Utils.random(0, 6);
				int currentLevel = targetPlayer.getSkills().getLevel(randomSkill);
				targetPlayer.getSkills().set(randomSkill, currentLevel < 5 ? 0 : currentLevel - 5);
				targetPlayer.getPackets().sendGameMessage("The screams of the banshee make you feel slightly weaker.");
				npc.setNextForceTalk(new ForceTalk("*EEEEHHHAHHH*"));
			}
			delayHit(npc, 0, target, getMeleeHit(npc, targetPlayer.getMaxHitpoints() / 10));
			//TODO player emote hands on ears
		} else {
			delayHit(npc, 0, target, getMeleeHit(npc, getRandomMaxHit(npc, npc.getMaxHit(), def.getAttackStyle(), target)));
		}
		npc.setNextAnimation(new Animation(def.getAttackEmote()));
		return def.getAttackDelay();
	}
}
