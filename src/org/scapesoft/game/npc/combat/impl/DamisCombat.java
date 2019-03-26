package org.scapesoft.game.npc.combat.impl;

import org.scapesoft.game.Animation;
import org.scapesoft.game.Entity;
import org.scapesoft.game.ForceTalk;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.npc.combat.CombatScript;
import org.scapesoft.game.npc.combat.NPCCombatDefinitions;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.misc.Utils;

/**
 * 
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2012-12-15
 */
public class DamisCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 1975 };
	}

	private String[] MESSAGES = { "Taste my blade and feel my wrath!", "You are no match for my power!", "Your pathetic skills won't help you now!" };

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		if (target instanceof Player) {
			((Player) target).getPrayer().drainPrayer(40);
		}
		npc.setNextAnimation(new Animation(defs.getAttackEmote()));
		npc.setNextForceTalk(new ForceTalk(MESSAGES[Utils.random(MESSAGES.length)]));
		delayHit(npc, 0, target, getMeleeHit(npc, getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.MELEE, target)));
		return defs.getAttackDelay() - 1;
	}

}
