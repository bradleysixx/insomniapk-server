package org.scapesoft.game.npc.combat.impl;

import java.util.ArrayList;
import java.util.List;

import org.scapesoft.game.Animation;
import org.scapesoft.game.Entity;
import org.scapesoft.game.Graphics;
import org.scapesoft.game.Hit;
import org.scapesoft.game.World;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.npc.combat.CombatScript;
import org.scapesoft.game.npc.combat.NPCCombatDefinitions;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.tasks.WorldTask;
import org.scapesoft.game.tasks.WorldTasksManager;
import org.scapesoft.utilities.misc.Utils;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jul 5, 2014
 */
public class Wildywyrm extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 3334 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();

		int random = Utils.random(3);
		for (final Entity t : getPossibleTargets(npc)) {
			switch (random) {
			case 1:
			case 2:
				npc.setNextAnimation(RANGE_ANIMATION);
				delayHit(npc, 0, t, getRangeHit(npc, getRandomMaxHit(npc, Utils.random(50, 400), NPCCombatDefinitions.RANGE, t)));
				World.sendProjectile(npc, t, 2313, 41, 16, 41, 30, 16, 0);
				if (Utils.percentageChance(10))
					t.getPoison().makePoisoned(60);
				break;
			default:
				npc.setNextAnimation(MAGE_ANIMATION);
				final Hit hit = getMagicHit(npc, getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.MAGE, t));
				delayHit(npc, 1, t, hit);
				World.sendProjectile(npc, t, 2315, 41, 16, 41, 30, 16, 0);
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						if (Utils.getRandom(10) == 0 && t.getFreezeDelay() < Utils.currentTimeMillis()) {
							t.addFreezeDelay(3000);
							t.setNextGraphics(new Graphics(369));
							if (t.isPlayer()) {
								t.player().stopAll();
							}
						} else if (hit.getDamage() != 0) {
							t.setNextGraphics(new Graphics(2315));
						}
					}
				}, 1);
				break;
			}

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

	private static final Animation RANGE_ANIMATION = new Animation(12791);
	private static final Animation MAGE_ANIMATION = new Animation(12795);

}
