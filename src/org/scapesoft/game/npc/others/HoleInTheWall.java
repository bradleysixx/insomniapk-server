package org.scapesoft.game.npc.others;

import org.scapesoft.game.Animation;
import org.scapesoft.game.Entity;
import org.scapesoft.game.Hit;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.Hit.HitLook;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.npc.combat.NPCCombatDefinitions;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.content.slayer.Slayer;
import org.scapesoft.game.tasks.WorldTask;
import org.scapesoft.game.tasks.WorldTasksManager;
import org.scapesoft.utilities.misc.Utils;

public class HoleInTheWall extends NPC {

	/**
	 *
	 */
	private static final long serialVersionUID = -4249922004734704518L;
	private transient boolean hasGrabbed;

	public HoleInTheWall(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
		setCantFollowUnderCombat(true);
		setCantInteract(true);
		setForceAgressive(true);
	}

	@Override
	public void processNPC() {
		super.processNPC();
		if (getId() == 2058) {
			if (!hasGrabbed) {
				for (Entity entity : getPossibleTargets()) {
					if (entity == null || entity.isDead() || !withinDistance(entity, 1)) {
						continue;
					}
					if (entity instanceof Player) {
						final Player player = (Player) entity;
						player.resetWalkSteps();
						hasGrabbed = true;
						if (Slayer.hasSpinyHelmet(player)) {
							transformInto(7823);
							setNextAnimation(new Animation(1805));
							setCantInteract(false);
							player.getPackets().sendGameMessage("The spines on your helmet repell the beast's hand.");
							return;
						}
						setNextAnimation(new Animation(1802));
						player.lock(4);
						player.setNextAnimation(new Animation(425));
						player.getPackets().sendGameMessage("A giant hand appears and grabs your head.");
						WorldTasksManager.schedule(new WorldTask() {

							@Override
							public void run() {
								player.applyHit(new Hit(player, Utils.getRandom(44), HitLook.REGULAR_DAMAGE));
								setNextAnimation(new Animation(-1));
								WorldTasksManager.schedule(new WorldTask() {

									@Override
									public void run() {
										hasGrabbed = false;
									}
								}, 20);
							}
						}, 5);
					}
				}
			}
		} else {
			if (!getCombat().process()) {
				setCantInteract(true);
				transformInto(2058);
			}
		}
	}

	@Override
	public void sendDeath(Entity source) {
		final NPCCombatDefinitions defs = getCombatDefinitions();
		resetWalkSteps();
		getCombat().removeTarget();
		setNextAnimation(null);
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					setNextAnimation(new Animation(defs.getDeathEmote()));
				} else if (loop >= defs.getDeathDelay()) {
					setNPC(2058);
					drop();
					reset();
					setLocation(getRespawnTile());
					finish();
					WorldTasksManager.schedule(new WorldTask() {

						@Override
						public void run() {
							hasGrabbed = false;
						}
					}, 8);
					spawn();
					stop();
				}
				loop++;
			}
		}, 0, 1);
	}
}
