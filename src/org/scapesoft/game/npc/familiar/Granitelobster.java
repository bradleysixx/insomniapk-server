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
import org.scapesoft.game.player.actions.Fishing;
import org.scapesoft.game.player.actions.summoning.Pouches;
import org.scapesoft.game.tasks.WorldTask;
import org.scapesoft.game.tasks.WorldTasksManager;
import org.scapesoft.utilities.misc.Utils;

public class Granitelobster extends Familiar {

	private int forageTicks;
	private static final long serialVersionUID = -8354346765099305088L;

	public Granitelobster(Player owner, Pouches pouch, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public void processNPC() {
		super.processNPC();
		boolean isFishing = getOwner().getActionManager().getAction() != null && getOwner().getActionManager().getAction() instanceof Fishing;
		if (isFishing) {
			forageTicks++;
			if (forageTicks == 300) {
				giveReward();
			}
		}
	}

	private void giveReward() {
		boolean isShark = Utils.random(3) == 0;
		int foragedItem = isShark ? 383 : 371;
		if (!isShark) {
			getOwner().getSkills().addXp(Skills.FISHING, 30);
		}
		getBob().getBeastItems().add(new Item(foragedItem, 1));
		forageTicks = 0;
	}

	@Override
	public String getSpecialName() {
		return "Crushing Claw";
	}

	@Override
	public String getSpecialDescription() {
		return "May inflict up to 140 life points of magic damage and temporarily decrease an opponent's Defence by five levels.";
	}

	@Override
	public int getBOBSize() {
		return 30;
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
		setNextAnimation(new Animation(8118));
		setNextGraphics(new Graphics(1351));
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {

				WorldTasksManager.schedule(new WorldTask() {

					@Override
					public void run() {
						if (Utils.getRandom(5) == 0) {
							if (target instanceof Player) {
								((Player) target).getSkills().set(Skills.DEFENCE, ((Player) target).getSkills().getLevel(Skills.DEFENCE));
							}
						}
						target.applyHit(new Hit(getOwner(), Utils.random(140), HitLook.MELEE_DAMAGE));
						target.setNextGraphics(new Graphics(1353));
					}
				}, 2);
				World.sendProjectile(npc, target, 1352, 34, 16, 30, 35, 16, 0);
			}
		});
		return true;
	}
}
