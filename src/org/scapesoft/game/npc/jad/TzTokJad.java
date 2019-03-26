/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scapesoft.game.npc.jad;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.scapesoft.engine.CoresManager;
import org.scapesoft.game.Animation;
import org.scapesoft.game.Entity;
import org.scapesoft.game.World;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.npc.combat.NPCCombatDefinitions;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.content.Magic;
import org.scapesoft.game.tasks.WorldTask;
import org.scapesoft.game.tasks.WorldTasksManager;
import org.scapesoft.utilities.misc.Utils;

/**
 *
 * @author Owner
 */
public class TzTokJad extends NPC {

	/**
	 *
	 */
	private static final long serialVersionUID = 5908420182962388837L;

	public TzTokJad(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
	}

	@Override
	public ArrayList<Entity> getPossibleTargets() {
		ArrayList<Entity> possibleTarget = new ArrayList<Entity>();
		for (int regionId : getMapRegionsIds()) {
			List<Integer> playerIndexes = World.getRegion(regionId).getPlayerIndexes();
			if (playerIndexes != null) {
				for (int npcIndex : playerIndexes) {
					Player player = World.getPlayers().get(npcIndex);
					if (player == null || player.isDead() || player.hasFinished() || !player.isRunning() || !player.withinDistance(this, 64) || ((!isAtMultiArea() || !player.isAtMultiArea()) && player.getAttackedBy() != this && player.getAttackedByDelay() > System.currentTimeMillis()) || !clipedProjectile(player, false)) {
						continue;
					}
					possibleTarget.add(player);
				}
			}
		}
		return possibleTarget;
	}

	/*
	 * gotta override else setRespawnTask override doesnt work
	 */
	@Override
	public void sendDeath(Entity source) {
		Player killer = (Player) source;
		final NPCCombatDefinitions defs = getCombatDefinitions();
		resetWalkSteps();
		getCombat().removeTarget();
		setNextAnimation(null);
		killer.getInventory().addItem(6570, 1);
		if (Utils.random(5) == 1) {
			killer.getInventory().addItemDrop(21512, 1);
			killer.sendMessage("Congratulations, you were lucky enough to receive a pet!");
		}
		Magic.sendNormalTeleportSpell(killer, 0, 0, new WorldTile(2438, 5173, 0));
		WorldTasksManager.schedule(new WorldTask() {

			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					setNextAnimation(new Animation(defs.getDeathEmote()));
				} else if (loop >= defs.getDeathDelay()) {
					drop();
					reset();
					setLocation(getRespawnTile());
					finish();
					setRespawnTask();
					stop();
				}
				loop++;
			}
		}, 0, 1);
	}

	@Override
	public void setRespawnTask() {
		if (!hasFinished()) {
			reset();
			setLocation(getRespawnTile());
			finish();
		}
		final NPC npc = this;
		CoresManager.slowExecutor.schedule(new Runnable() {

			@Override
			public void run() {
				setFinished(false);
				World.addNPC(npc);
				npc.setLastRegionId(0);
				World.updateEntityRegion(npc);
				loadMapRegions();
				checkMultiArea();
			}
		}, getCombatDefinitions().getRespawnDelay() * 1200, TimeUnit.MILLISECONDS);
	}
}
