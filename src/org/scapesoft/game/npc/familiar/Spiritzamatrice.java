package org.scapesoft.game.npc.familiar;

import org.scapesoft.game.Animation;
import org.scapesoft.game.Entity;
import org.scapesoft.game.Graphics;
import org.scapesoft.game.Hit;
import org.scapesoft.game.World;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.Hit.HitLook;
import org.scapesoft.game.item.Item;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.Skills;
import org.scapesoft.game.player.actions.summoning.Pouches;
import org.scapesoft.game.tasks.WorldTask;
import org.scapesoft.game.tasks.WorldTasksManager;
import org.scapesoft.utilities.misc.Utils;

public class Spiritzamatrice extends Familiar {

	private static final long serialVersionUID = 9026151557574358985L;
	private int chocoTriceEgg;

	public Spiritzamatrice(Player owner, Pouches pouch, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public void processNPC() {
		super.processNPC();
		chocoTriceEgg++;
		if (chocoTriceEgg == 500) {
			addChocolateEgg();
		}
	}

	private void addChocolateEgg() {
		getBob().getBeastItems().add(new Item(12109, 1));
		chocoTriceEgg = 0;
	}

	@Override
	public String getSpecialName() {
		return "Petrifying Gaze";
	}

	@Override
	public String getSpecialDescription() {
		return "Inflicts damage and drains a combat stat, which varies according to the type of cockatrice.";
	}

	@Override
	public int getBOBSize() {
		return 30;
	}

	@Override
	public int getSpecialAmount() {
		return 5;
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
		setNextAnimation(new Animation(7766));
		setNextGraphics(new Graphics(1467));
		World.sendProjectile(this, target, 1468, 34, 16, 30, 35, 16, 0);
		if (target instanceof Player) {
			Player playerTarget = (Player) target;
			int level = playerTarget.getSkills().getLevelForXp(Skills.STRENGTH);
			int drained = 3;
			if (level - drained > 0) {
				drained = level;
			}
			playerTarget.getSkills().drainLevel(Skills.STRENGTH, drained);
		}
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				target.applyHit(new Hit(getOwner(), Utils.random(100), HitLook.MELEE_DAMAGE));
			}
		}, 2);
		return true;
	}
}
