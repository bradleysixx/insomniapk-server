package org.scapesoft.game.npc.combat.impl;

import java.util.ArrayList;
import java.util.List;

import org.scapesoft.game.Animation;
import org.scapesoft.game.Entity;
import org.scapesoft.game.Graphics;
import org.scapesoft.game.World;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.npc.combat.CombatScript;
import org.scapesoft.game.npc.combat.NPCCombatDefinitions;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.misc.ChatColors;
import org.scapesoft.utilities.misc.Utils;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jul 5, 2014
 */
public class SeaTrollQueen extends CombatScript {

	private static final Animation ATTACK_ANIMATION = new Animation(3991);
	private static final Graphics ATTACK_GRAPHICS = new Graphics(2702);

	@Override
	public Object[] getKeys() {
		return new Object[] { 3847 };
	}

	@Override
	public int attack(NPC npc, Entity entity) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();

		for (Entity target : getPossibleTargets(npc)) {
			if (Utils.percentageChance(10)) {
				if (target.isPlayer()) {
					target.player().getPrayer().closeAllPrayers();
					target.player().sendMessage("<col=" + ChatColors.MAROON + ">The Queen disables your prayers temporarily!");
					target.player().getTemporaryAttributtes().put("PrayerBlocked", 5000 + Utils.currentTimeMillis());
					target.player().getPrayer().recalculatePrayer();
					target.player().setPrayerDelay(5000);// 5 seconds
				}
			}
			delayHit(npc, 2, target, getMagicHit(npc, getRandomMaxHit(npc, 500, NPCCombatDefinitions.MAGE, target)));
			World.sendProjectile(npc, target, 2706, 18, 18, 50, 50, 0, 0);

			npc.setNextAnimation(ATTACK_ANIMATION);
			npc.setNextGraphics(ATTACK_GRAPHICS);
		}
		return defs.getAttackDelay();
	}

	public List<Entity> getPossibleTargets(NPC npc) {
		List<Entity> targets = new ArrayList<Entity>();
		List<Integer> indexes = npc.getRegion().getPlayerIndexes();
		if (indexes != null) {
			for (Integer index : indexes) {
				Player player = World.getPlayers().get(index);
				if (player == null || !player.withinDistance(npc, 15)) {
					continue;
				}
				targets.add(player);
			}
		}
		return targets;
	}
}
