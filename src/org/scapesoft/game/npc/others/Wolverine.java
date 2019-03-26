package org.scapesoft.game.npc.others;

import java.util.Random;

import org.scapesoft.game.WorldTile;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.player.Player;

public class Wolverine extends NPC {

	/**
	 *
	 */
	private static final long serialVersionUID = 8926040124067867297L;

	public Wolverine(Player target, int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
		setCombatLevel(target.getSkills().getCombatLevel() + new Random().nextInt(100) + 100);
		int hitpoints = 1000 + this.getCombatLevel() + target.getSkills().getCombatLevel() / 2 + new Random().nextInt(10);
		super.getCombatDefinitions().setHitpoints(hitpoints);
		setHitpoints(hitpoints);
		setRandomWalk(true);
		setForceAgressive(true);
		setAttackedBy(target);
		setAtMultiArea(true);
		setNextFaceEntity(target);
	}
}