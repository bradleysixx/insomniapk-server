package org.scapesoft.game.npc.familiar;

import org.scapesoft.game.Animation;
import org.scapesoft.game.Entity;
import org.scapesoft.game.Graphics;
import org.scapesoft.game.Hit;
import org.scapesoft.game.World;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.Hit.HitLook;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.actions.summoning.Pouches;
import org.scapesoft.game.tasks.WorldTask;
import org.scapesoft.game.tasks.WorldTasksManager;
import org.scapesoft.utilities.misc.Utils;

public class Spiritdagannoth extends Familiar {

	private static final long serialVersionUID = -494712406261011797L;

	public Spiritdagannoth(Player owner, Pouches pouch, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Spike Shot";
	}

	@Override
	public String getSpecialDescription() {
		return "Inflicts damage to your target from up to 180 hitpoints.";
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
		final Familiar npc = this;
		getOwner().setNextGraphics(new Graphics(1316));
		getOwner().setNextAnimation(new Animation(7660));
		setNextAnimation(new Animation(7787));
		setNextGraphics(new Graphics(1467));
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				WorldTasksManager.schedule(new WorldTask() {

					@Override
					public void run() {
						int hitDamage = Utils.random(180);
						if (hitDamage > 0) {
							if (target instanceof Player) {
								((Player) target).lock(6);
							} else {
								target.addFreezeDelay(6000);
							}
						}
						target.applyHit(new Hit(getOwner(), hitDamage, HitLook.MAGIC_DAMAGE));
						target.setNextGraphics(new Graphics(1428));
					}
				}, 2);
				World.sendProjectile(npc, target, 1426, 34, 16, 30, 35, 16, 0);
			}
		});
		return true;
	}
}
