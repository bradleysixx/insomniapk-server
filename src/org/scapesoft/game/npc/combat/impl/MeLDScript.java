package org.scapesoft.game.npc.combat.impl;

import org.scapesoft.game.Animation;
import org.scapesoft.game.Entity;
import org.scapesoft.game.Graphics;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.npc.combat.CombatScript;
import org.scapesoft.utilities.misc.Utils;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jun 29, 2014
 */
public class MeLDScript extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 4510 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		int random = Utils.random(4);
		int damage = Utils.random(150, 275);
		switch (random) {
		case 1:
		case 2:
			npc.setNextAnimation(MAGIC_ANIMATION);
			target.setNextGraphics(MAGIC_GFX);
			delayHit(npc, 1, target, getMagicHit(npc, damage));
			break;
		default:
			npc.setNextAnimation(MELEE_ANIMATION);
			delayHit(npc, 0, target, getMeleeHit(npc, damage));
			break;
		}
		return 3;
	}
	
	private static final Animation MELEE_ANIMATION = new Animation(1884);
	private static final Animation MAGIC_ANIMATION = new Animation(811);
	private static final Graphics MAGIC_GFX = new Graphics(76);

}
