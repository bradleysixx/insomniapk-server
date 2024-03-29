package org.scapesoft.game.npc.others;

import org.scapesoft.game.Graphics;
import org.scapesoft.game.Hit;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.Hit.HitLook;
import org.scapesoft.game.npc.NPC;

public class MercenaryMage extends NPC {

	/**
	 *
	 */
	private static final long serialVersionUID = 727740250131640435L;

	public MercenaryMage(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		setLureDelay(0);
		setCapDamage(500);
		setCombatLevel(65000);
		setRun(true);
		setForceMultiAttacked(true);
		setForceAgressive(true);
	}

	@Override
	public double getMagePrayerMultiplier() {
		return 0.6;
	}

	@Override
	public double getRangePrayerMultiplier() {
		return 0.6;
	}

	@Override
	public double getMeleePrayerMultiplier() {
		return 0.6;
	}

	@Override
	public void handleIngoingHit(Hit hit) {
		super.handleIngoingHit(hit);
		if (hit.getLook() != HitLook.MELEE_DAMAGE && hit.getLook() != HitLook.RANGE_DAMAGE && hit.getLook() != HitLook.MAGIC_DAMAGE) {
			return;
		}
		if (hit.getSource() != null) {
			int recoil = hit.getDamage();
			if (recoil > 0) {
				hit.getSource().applyHit(new Hit(this, recoil, HitLook.REFLECTED_DAMAGE));
				setNextGraphics(new Graphics(2180));
			}
		}
	}

}
