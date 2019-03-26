package org.scapesoft.game.npc.familiar;

import org.scapesoft.game.Animation;
import org.scapesoft.game.Entity;
import org.scapesoft.game.Graphics;
import org.scapesoft.game.Hit;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.Hit.HitLook;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.actions.summoning.Pouches;
import org.scapesoft.utilities.misc.Utils;

public class Smokedevil extends Familiar {

	private static final long serialVersionUID = -2734031002616044128L;

	public Smokedevil(Player owner, Pouches pouch, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Dust Cloud";
	}

	@Override
	public String getSpecialDescription() {
		return "Hit up to 80 damage to all people within 1 square of you.";
	}

	@Override
	public int getBOBSize() {
		return 0;
	}

	@Override
	public int getSpecialAmount() {
		return 6;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.ENTITY;
	}

	@Override
	public boolean submitSpecial(Object object) {
		getOwner().setNextGraphics(new Graphics(1316));
		getOwner().setNextAnimation(new Animation(7660));
		setNextAnimation(new Animation(7820));
		setNextGraphics(new Graphics(1470));
		for (Entity entity : this.getPossibleTargets()) {
			if (entity == null || entity == getOwner() || !entity.withinDistance(this, 1)) {
				continue;
			}
			entity.applyHit(new Hit(this, Utils.random(80), HitLook.MAGIC_DAMAGE));
		}
		return true;
	}
}
