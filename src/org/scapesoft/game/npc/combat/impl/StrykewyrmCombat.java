package org.scapesoft.game.npc.combat.impl;

import org.scapesoft.game.Animation;
import org.scapesoft.game.Entity;
import org.scapesoft.game.Graphics;
import org.scapesoft.game.Hit;
import org.scapesoft.game.World;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.Hit.HitLook;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.npc.combat.CombatScript;
import org.scapesoft.game.npc.combat.NPCCombatDefinitions;
import org.scapesoft.game.npc.slayer.Strykewyrm;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.tasks.WorldTask;
import org.scapesoft.game.tasks.WorldTasksManager;
import org.scapesoft.utilities.misc.Utils;

public class StrykewyrmCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 9463, 9465, 9467 };
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int attackStyle = Utils.getRandom(10);
		if (attackStyle <= 7 && Utils.isInRange(npc.getX(), npc.getY(), npc.getSize(), target.getX(), target.getY(), target.getSize(), 0)) { // melee
			npc.setNextAnimation(new Animation(defs.getAttackEmote()));
			if (npc.getId() == 9467) {
				if (Utils.getRandom(10) == 0) {
					target.setNextGraphics(new Graphics(2309));
					target.getPoison().makePoisoned(44);
				}
			}
			delayHit(npc, 0, target, getMeleeHit(npc, getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.MAGE, target)));
			return defs.getAttackDelay();
		}
		if (attackStyle <= 9) { // mage
			npc.setNextAnimation(new Animation(12794));
			final Hit hit = getMagicHit(npc, getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.MAGE, target));
			delayHit(npc, 1, target, hit);
			World.sendProjectile(npc, target, defs.getAttackProjectile(), 41, 16, 41, 30, 16, 0);
			if (npc.getId() == 9463) {
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						if (Utils.getRandom(10) == 0 && target.getFreezeDelay() < Utils.currentTimeMillis()) {
							target.addFreezeDelay(3000);
							target.setNextGraphics(new Graphics(369));
							if (target instanceof Player) {
								Player targetPlayer = (Player) target;
								targetPlayer.stopAll();
							}
						} else if (hit.getDamage() != 0) {
							target.setNextGraphics(new Graphics(2315));
						}
					}
				}, 1);
			} else if (npc.getId() == 9467) {
				if (Utils.getRandom(10) == 0) {
					target.setNextGraphics(new Graphics(2313));
					if (Utils.random(2) == 0) {
						target.getPoison().makePoisoned(88);
					}
				}
			}
		} else if (attackStyle == 10) { // bury
			final WorldTile tile = new WorldTile(target);
			tile.moveLocation(-1, -1, 0);
			npc.setNextAnimation(new Animation(12796));
			npc.setCantInteract(true);
			npc.getCombat().removeTarget();
			WorldTasksManager.schedule(new WorldTask() {

				int count;

				@Override
				public void run() {
					if (count == 0) {

						npc.transformInto(((Strykewyrm) npc).getStompId());
						npc.setForceWalk(tile);
						count++;
					} else if (count == 1 && !npc.hasForceWalk()) {
						npc.transformInto(((Strykewyrm) npc).getStompId() + 1);
						npc.setNextAnimation(new Animation(12795));
						int distanceX = target.getX() - npc.getX();
						int distanceY = target.getY() - npc.getY();
						int size = npc.getSize();
						if (distanceX < size && distanceX > -1 && distanceY < size && distanceY > -1) {
							delayHit(npc, 0, target, new Hit(npc, 300, HitLook.REGULAR_DAMAGE));
							if (npc.getId() == 9467) {
								target.getPoison().makePoisoned(88);
							} else if (npc.getId() == 9465) {
								delayHit(npc, 0, target, new Hit(npc, 300, HitLook.REGULAR_DAMAGE));
								target.setNextGraphics(new Graphics(2311));
							}
						}
						count++;
					} else if (count == 2) {
						WorldTasksManager.schedule(new WorldTask() {
							@Override
							public void run() {
								npc.getCombat().setCombatDelay(defs.getAttackDelay());
								npc.setTarget(target);
								npc.setCantInteract(false);
							}
						});
						stop();
					}
				}
			}, 1, 1);
		}
		return defs.getAttackDelay();
	}
}
