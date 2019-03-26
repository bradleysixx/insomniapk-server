// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   NPCCombat.java

package org.scapesoft.game.npc.combat;

import org.scapesoft.game.Animation;
import org.scapesoft.game.Entity;
import org.scapesoft.game.ForceMovement;
import org.scapesoft.game.World;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.npc.familiar.Familiar;
import org.scapesoft.game.npc.godwars.zaros.Nex;
import org.scapesoft.game.player.content.Combat;
import org.scapesoft.utilities.game.cache.MapAreas;
import org.scapesoft.utilities.misc.Utils;

// Referenced classes of package com.rs.game.npc.combat:
//            NPCCombatDefinitions, CombatScriptsHandler

public final class NPCCombat {

	public NPCCombat(NPC npc) {
		this.npc = npc;
	}

	public int getCombatDelay() {
		return combatDelay;
	}

	public boolean process() {
		if (combatDelay > 0)
			combatDelay--;
		if (target != null) {
			if (!checkAll()) {
				removeTarget();
				return false;
			}
			if (combatDelay <= 0)
				combatDelay = combatAttack();
			return true;
		} else {
			return false;
		}
	}

	private int combatAttack() {
		Entity target = this.target;
		if (target == null)
			return 0;
		if (npc.getFreezeDelay() >= System.currentTimeMillis())
			return 0;
		NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int attackStyle = defs.getAttackStyle();
		int maxDistance = attackStyle != 0 && attackStyle != 4 ? 7 : 0;
		if (!(npc instanceof Nex) && !npc.clipedProjectile(target, maxDistance == 0))
			return 0;
		int distanceX = target.getX() - npc.getX();
		int distanceY = target.getY() - npc.getY();
		int size = npc.getSize();
		if (distanceX > size + maxDistance || distanceX < -1 - maxDistance || distanceY > size + maxDistance || distanceY < -1 - maxDistance) {
			return 0;
		} else {
			addAttackedByDelay(target);
			return CombatScriptsHandler.specialAttack(npc, target);
		}
	}

	protected void doDefenceEmote(Entity target) {
		target.setNextAnimationNoPriority(new Animation(Combat.getDefenceEmote(target)));
	}

	public Entity getTarget() {
		return target;
	}

	protected void addAttackedByDelay(Entity target) {
		target.setAttackedBy(npc);
		target.setAttackedByDelay(System.currentTimeMillis() + (long) (npc.getCombatDefinitions().getAttackDelay() * 600) + 600L);
	}

	public void setTarget(Entity target) {
		this.target = target;
		npc.setNextFaceEntity(target);
		if (!checkAll()) {
			removeTarget();
			return;
		} else {
			return;
		}
	}

	public boolean checkAll() {
		Entity target = this.target;
		if (target == null)
			return false;
		if (npc.isDead() || npc.hasFinished() || npc.isForceWalking() || target.isDead() || target.hasFinished())
			return false;
		if (npc.getFreezeDelay() >= System.currentTimeMillis())
			return true;
		int distanceX = npc.getX() - npc.getRespawnTile().getX();
		int distanceY = npc.getY() - npc.getRespawnTile().getY();
		int size = npc.getSize();
		int maxDistance = 32;
		if (!npc.isCantFollowUnderCombat() && !(npc instanceof Familiar))
			if (npc.getMapAreaNameHash() != -1) {
				if (!MapAreas.isAtArea(npc.getMapAreaNameHash(), npc) || !npc.canBeAttackFromOutOfArea() && !MapAreas.isAtArea(npc.getMapAreaNameHash(), target)) {
					npc.forceWalkRespawnTile();
					return false;
				}
			} else if (distanceX > size + maxDistance || distanceX < -1 - maxDistance || distanceY > size + maxDistance || distanceY < -1 - maxDistance) {
				npc.forceWalkRespawnTile();
				return false;
			}
		maxDistance = 16;
		distanceX = target.getX() - npc.getX();
		distanceY = target.getY() - npc.getY();
		if (npc.getPlane() != target.getPlane() || distanceX > size + maxDistance || distanceX < -1 - maxDistance || distanceY > size + maxDistance || distanceY < -1 - maxDistance)
			return false;
		if (npc instanceof Familiar) {
			Familiar familiar = (Familiar) npc;
			if (!familiar.canAttack(target))
				return false;
		} else if (!npc.isForceMultiAttacked() && (!target.isAtMultiArea() || !npc.isAtMultiArea())) {
			if (npc.getAttackedBy() != target && npc.getAttackedByDelay() > System.currentTimeMillis())
				return false;
			if (target.getAttackedBy() != npc && target.getAttackedByDelay() > System.currentTimeMillis())
				return false;
		}
		if (!npc.isCantFollowUnderCombat()) {
			// if is under
			int targetSize = target.getSize();
			if (distanceX < size && distanceX > -targetSize && distanceY < size && distanceY > -targetSize && !target.hasWalkSteps()) {

				/*
				 * System.out.println(size + maxDistance); System.out.println(-1
				 * - maxDistance);
				 */
				npc.resetWalkSteps();
				if (!npc.addWalkSteps(target.getLocation().getX() + 1, npc.getLocation().getY())) {
					npc.resetWalkSteps();
					if (!npc.addWalkSteps(target.getLocation().getX() - size, npc.getLocation().getY())) {
						npc.resetWalkSteps();
						if (!npc.addWalkSteps(npc.getLocation().getX(), target.getLocation().getY() + 1)) {
							npc.resetWalkSteps();
							if (!npc.addWalkSteps(npc.getLocation().getX(), target.getLocation().getY() - size)) {
								return true;
							}
						}
					}
				}
				return true;
			}
			if (npc.getCombatDefinitions().getAttackStyle() == NPCCombatDefinitions.MELEE && targetSize == 1 && size == 1 && Math.abs(npc.getLocation().getX() - target.getLocation().getX()) == 1
					&& Math.abs(npc.getLocation().getY() - target.getLocation().getY()) == 1 && !target.hasWalkSteps()) {
				if (!npc.addWalkSteps(target.getLocation().getX(), npc.getLocation().getY(), 1))
					npc.addWalkSteps(npc.getLocation().getX(), target.getLocation().getY(), 1);
				return true;
			}

			int attackStyle = npc.getCombatDefinitions().getAttackStyle();
			if (npc instanceof Nex) {
				Nex nex = (Nex) npc;
				maxDistance = nex.isForceFollowClose() ? 0 : 7;
				if (!nex.isFlying() && (!npc.clipedProjectile(target, maxDistance == 0)) || !Utils.isInRange(npc.getX(), npc.getY(), size, target.getX(), target.getY(), target.getSize(), maxDistance)) {
					npc.resetWalkSteps();
					if (!Utils.isInRange(npc.getX(), npc.getY(), size, target.getX(), target.getY(), target.getSize(), 10)) {
						int[][] dirs = Utils.getCoordOffsetsNear(size);
						for (int dir = 0; dir < dirs[0].length; dir++) {
							final WorldTile tile = new WorldTile(new WorldTile(target.getX() + dirs[0][dir], target.getY() + dirs[1][dir], target.getPlane()));
							if (World.isTileFree(tile.getPlane(), tile.getX(), tile.getY(), size)) { // if
								// found
								// done
								npc.setNextForceMovement(new ForceMovement(new WorldTile(npc), 0, tile, 1, Utils.getMoveDirection(tile.getX() - npc.getX(), tile.getY() - npc.getY())));
								npc.setNextAnimation(new Animation(17408));
								npc.setNextWorldTile(tile);
								nex.setFlying(false);
								return true;
							}
						}
					} else
						npc.calcFollow(target, 2, true, npc.isIntelligentRouteFinder());
					return true;
				} else
					// if doesnt need to move more stop moving
					npc.resetWalkSteps();
			} else {
				maxDistance = npc.isForceFollowClose() ? 0 : (attackStyle == NPCCombatDefinitions.MELEE || attackStyle == NPCCombatDefinitions.SPECIAL2) ? 0 : 7;
				npc.resetWalkSteps();
				// is far from target, moves to it till can attack
				if ((!npc.clipedProjectile(target, maxDistance == 0)) || distanceX > size + maxDistance || distanceX < -1 - maxDistance || distanceY > size + maxDistance
						|| distanceY < -1 - maxDistance) {
					if (!npc.addWalkStepsInteract(target.getLocation().getX(), target.getLocation().getY(), 2, size, true) && combatDelay < 3)
						combatDelay = 3;
					return true;
				}
				// if under target, moves

			}
		}
/*		if (!npc.isCantFollowUnderCombat()) {
			if (distanceX < size && distanceX > -1 && distanceY < size && distanceY > -1) {
				npc.resetWalkSteps();
				if (!npc.addWalkSteps(target.getX() + 1, npc.getY())) {
					npc.resetWalkSteps();
					if (!npc.addWalkSteps(target.getX() - size - 1, npc.getY())) {
						npc.resetWalkSteps();
						if (!npc.addWalkSteps(target.getX(), npc.getY() + 1)) {
							npc.resetWalkSteps();
							if (!npc.addWalkSteps(target.getX(), npc.getY() - size - 1))
								return true;
						}
					}
				}
				return true;
			}
			int attackStyle = npc.getCombatDefinitions().getAttackStyle();
			if (npc instanceof Nex) {
				Nex nex = (Nex) npc;
				maxDistance = nex.isForceFollowClose() ? 0 : 7;
				if (!nex.isFlying() && (!npc.clipedProjectile(target, maxDistance == 0)) || !Utils.isInRange(npc.getX(), npc.getY(), size, target.getX(), target.getY(), target.getSize(), maxDistance)) {
					npc.resetWalkSteps();
					if (!Utils.isInRange(npc.getX(), npc.getY(), size, target.getX(), target.getY(), target.getSize(), 10)) {
						int[][] dirs = Utils.getCoordOffsetsNear(size);
						for (int dir = 0; dir < dirs[0].length; dir++) {
							final WorldTile tile = new WorldTile(new WorldTile(target.getX() + dirs[0][dir], target.getY() + dirs[1][dir], target.getPlane()));
							if (World.isTileFree(tile.getPlane(), tile.getX(), tile.getY(), size)) { // if
								// found
								// done
								npc.setNextForceMovement(new ForceMovement(new WorldTile(npc), 0, tile, 1, Utils.getMoveDirection(tile.getX() - npc.getX(), tile.getY() - npc.getY())));
								npc.setNextAnimation(new Animation(17408));
								npc.setNextWorldTile(tile);
								nex.setFlying(false);
								return true;
							}
						}
					} else
						npc.calcFollow(target, 2, true, npc.isIntelligentRouteFinder());
					return true;
				} else
					// if doesnt need to move more stop moving
					npc.resetWalkSteps();
			} else {
				maxDistance = npc.isForceFollowClose() ? 0 : ((int) (attackStyle != 0 && attackStyle != 4 ? 7 : 0));
				if (!npc.clipedProjectile(target, maxDistance == 0) || (distanceX > size + maxDistance || distanceX < -1 - maxDistance || distanceY > size + maxDistance || distanceY < -1 - maxDistance)) {
					npc.resetWalkSteps();
					npc.addWalkStepsInteract(target.getX(), target.getY(), 2, size, true);
					return true;
				}
				npc.resetWalkSteps();
			}
		}*/
		return true;
	}

	public void addCombatDelay(int delay) {
		combatDelay += delay;
	}

	public void setCombatDelay(int delay) {
		combatDelay = delay;
	}

	public boolean underCombat() {
		return target != null;
	}

	public void removeTarget() {
		target = null;
		npc.setNextFaceEntity(null);
	}

	public void reset() {
		combatDelay = 0;
		target = null;
	}

	private NPC npc;
	private int combatDelay;
	private Entity target;
}
