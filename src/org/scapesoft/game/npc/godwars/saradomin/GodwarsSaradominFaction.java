package org.scapesoft.game.npc.godwars.saradomin;

import java.util.ArrayList;

import org.scapesoft.game.Animation;
import org.scapesoft.game.Entity;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.item.Item;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.npc.combat.NPCCombatDefinitions;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.controlers.Controller;
import org.scapesoft.game.player.controlers.impl.GodWars;
import org.scapesoft.game.tasks.WorldTask;
import org.scapesoft.game.tasks.WorldTasksManager;

public class GodwarsSaradominFaction extends NPC {

	/**
	 *
	 */
	private static final long serialVersionUID = -3497570542106729604L;

	public GodwarsSaradominFaction(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
	}

	@Override
	public ArrayList<Entity> getPossibleTargets() {
		if (!withinDistance(new WorldTile(2881, 5306, 0), 200)) {
			return super.getPossibleTargets();
		} else {
			ArrayList<Entity> targets = getPossibleTargets(true, true);
			ArrayList<Entity> targetsCleaned = new ArrayList<Entity>();
			for (Entity t : targets) {
				if (t instanceof GodwarsSaradominFaction || (t instanceof Player && hasGodItem((Player) t))) {
					continue;
				}
				targetsCleaned.add(t);
			}
			return targetsCleaned;
		}
	}

	public static boolean hasGodItem(Player player) {
		for (Item item : player.getEquipment().getItems().getItems()) {
			if (item == null) {
				continue; // shouldn't happen
			}
			String name = item.getDefinitions().getName().toLowerCase();
			// using else as only one item should count
			if (name.contains("saradomin coif") || name.contains("citharede hood") || name.contains("saradomin mitre") || name.contains("saradomin full helm") || name.contains("saradomin halo") || name.contains("torva full helm") || name.contains("pernix cowl") || name.contains("virtus mask")) {
				return true;
			} else if (name.contains("saradomin cape") || name.contains("saradomin cloak")) {
				return true;
			} else if (name.contains("holy symbol") || name.contains("citharede symbol") || name.contains("saradomin stole")) {
				return true;
			} else if (name.contains("saradomin arrow")) {
				return true;
			} else if (name.contains("saradomin godsword") || name.contains("saradomin sword") || name.contains("saradomin staff") || name.contains("saradomin crozier") || name.contains("zaryte Bow")) {
				return true;
			} else if (name.contains("saradomin robe top") || name.contains("saradomin d'hide") || name.contains("citharede robe top") || name.contains("monk's robe top") || name.contains("saradomin platebody") || name.contains("torva platebody") || name.contains("pernix body") || name.contains("virtus robe top")) {
				return true;
			} else if (name.contains("illuminated holy book") || name.contains("holy book") || name.contains("saradomin kiteshield")) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void sendDeath(final Entity source) {
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
					if (source instanceof Player) {
						Player player = (Player) source;
						Controller controler = player.getControllerManager().getController();
						if (controler != null && controler instanceof GodWars) {
							GodWars godControler = (GodWars) controler;
							godControler.incrementKillCount(2);
						}
					}
					drop();
					reset();
					setLocation(getRespawnTile());
					finish();
					if (!isSpawned()) {
						setRespawnTask();
					}
					stop();
				}
				loop++;
			}
		}, 0, 1);
	}
}
