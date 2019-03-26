package org.scapesoft.game.npc.familiar;

import org.scapesoft.game.Animation;
import org.scapesoft.game.Entity;
import org.scapesoft.game.Graphics;
import org.scapesoft.game.Hit;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.Hit.HitLook;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.actions.summoning.Pouches;
import org.scapesoft.game.tasks.WorldTask;
import org.scapesoft.game.tasks.WorldTasksManager;
import org.scapesoft.utilities.misc.Utils;

public class Prayingmantis extends Familiar {

	private static final long serialVersionUID = -2129621856723157961L;

	public Prayingmantis(Player owner, Pouches pouch, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Mantis Strike";
	}

	@Override
	public String getSpecialDescription() {
		return "Uses a magic based attack (max hit 170) which always drains the opponent's prayer and binds if it deals any damage.";
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
		final Entity target = (Entity) object;
		getOwner().setNextGraphics(new Graphics(1316));
		getOwner().setNextAnimation(new Animation(7660));
		setNextAnimation(new Animation(8071));
		setNextGraphics(new Graphics(1422));
		final int hitDamage = Utils.random(170);
		if (hitDamage > 0) {
			if (target instanceof Player) {
				((Player) target).getPrayer().drainPrayer(hitDamage);
			}
		}
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				target.setNextGraphics(new Graphics(1423));
				target.applyHit(new Hit(getOwner(), hitDamage, HitLook.MAGIC_DAMAGE));
			}
		}, 2);
		return true;
	}
}
