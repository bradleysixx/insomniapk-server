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

public class CaveHorrerCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "Cave horror" };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		NPCCombatDefinitions def = npc.getCombatDefinitions();
		if (!Slayer.hasWitchWoodIcon(target)) {
			Player targetPlayer = (Player) target;
			int randomSkill = Utils.random(0, 6);
			int currentLevel = targetPlayer.getSkills().getLevel(randomSkill);
			targetPlayer.getSkills().set(randomSkill, currentLevel < 5 ? 0 : currentLevel - 5);
			targetPlayer.getPackets().sendGameMessage("The screams of the cave horrer make you feel slightly weaker.");
			npc.setNextForceTalk(new ForceTalk("*OOOoooAHHHH*"));
			delayHit(npc, 0, target, getMeleeHit(npc, targetPlayer.getMaxHitpoints() / 3));
		} else {
			delayHit(npc, 0, target, getMeleeHit(npc, getRandomMaxHit(npc, npc.getMaxHit(), def.getAttackStyle(), target)));
		}
		npc.setNextAnimation(new Animation(def.getAttackEmote()));
		return def.getAttackDelay();
	}
}
