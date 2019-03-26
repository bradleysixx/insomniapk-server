package org.scapesoft.game.npc.fightcave;

import org.scapesoft.game.Animation;
import org.scapesoft.game.Entity;
import org.scapesoft.game.Graphics;
import org.scapesoft.game.Hit;
import org.scapesoft.game.World;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.Hit.HitLook;
import org.scapesoft.game.npc.combat.NPCCombatDefinitions;
import org.scapesoft.game.tasks.WorldTask;
import org.scapesoft.game.tasks.WorldTasksManager;

public class TzKekCaves extends FightCavesNPC {

	/**
	 *
	 */
	private static final long serialVersionUID = -2560511278422028773L;

	public TzKekCaves(int id, WorldTile tile) {
		super(id, tile);
	}

	@Override
	public void sendDeath(Entity source) {
		final NPCCombatDefinitions defs = getCombatDefinitions();
		resetWalkSteps();
		getCombat().removeTarget();
		setNextAnimation(null);
		final WorldTile tile = this;
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					setNextAnimation(new Animation(defs.getDeathEmote()));
					setNextGraphics(new Graphics(2924 + getSize()));
				} else if (loop >= defs.getDeathDelay()) {
					reset();
					new FightCavesNPC(2738, tile);
					if (World.canMoveNPC(getPlane(), tile.getX() + 1, tile.getY(), 1)) {
						tile.moveLocation(1, 0, 0);
					} else if (World.canMoveNPC(getPlane(), tile.getX() - 1, tile.getY(), 1)) {
						tile.moveLocation(-1, 0, 0);
					} else if (World.canMoveNPC(getPlane(), tile.getX(), tile.getY() - 1, 1)) {
						tile.moveLocation(0, -1, 0);
					} else if (World.canMoveNPC(getPlane(), tile.getX(), tile.getY() + 1, 1)) {
						tile.moveLocation(0, 1, 0);
					}
					new FightCavesNPC(2738, tile);
					finish();
					stop();
				}
				loop++;
			}
		}, 0, 1);
	}

	@Override
	public void removeHitpoints(Hit hit) {
		super.removeHitpoints(hit);
		if (hit.getLook() != HitLook.MELEE_DAMAGE || hit.getSource() == null) {
			return;
		}
		hit.getSource().applyHit(new Hit(this, 10, HitLook.REGULAR_DAMAGE));
	}
}
