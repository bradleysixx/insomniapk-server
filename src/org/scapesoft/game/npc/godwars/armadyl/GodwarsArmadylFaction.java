package org.scapesoft.game.npc.godwars.armadyl;

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

public class GodwarsArmadylFaction extends NPC {

	/**
	 *
	 */
	private static final long serialVersionUID = -5991323933382549637L;

	public GodwarsArmadylFaction(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
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
				if (t instanceof GodwarsArmadylFaction || (t instanceof Player && hasGodItem((Player) t))) {
					continue;
				}
				targetsCleaned.add(t);
			}
			return targetsCleaned;
		}
	}

	private boolean hasGodItem(Player player) {
		for (Item item : player.getEquipment().getItems().getItems()) {
			if (item == null) {
				continue; // shouldn't happen
			}
			String name = item.getDefinitions().getName().toLowerCase();
			// using else as only one item should count
			if (name.contains("armadyl Helmet") || name.contains("armadyl mitre") || name.contains("armadyl full helm") || name.contains("armadyl coif") || name.contains("torva full helm") || name.contains("pernix cowl") || name.contains("virtus mask")) {
				return true;
			} else if (name.contains("armadyl cloak")) {
				return true;
			} else if (name.contains("armadyl pendant") || name.contains("armadyl stole")) {
				return true;
			} else if (name.contains("armadyl godsword") || name.contains("armadyl crozier") || name.contains("zaryte Bow")) {
				return true;
			} else if (name.contains("armadyl body") || name.contains("armadyl robe top") || name.contains("armadyl chestplate") || name.contains("armadyl platebody") || name.contains("torva platebody") || name.contains("pernix body") || name.contains("virtus robe top")) {
				return true;
			} else if (name.contains("illuminated book of law") || name.contains("book of law") || name.contains("armadyl kiteshield")) {
				return true;
			} else if (name.contains("armadyl robe legs") || name.contains("armadyl plateskirt") || name.contains("armadyl chaps") || name.contains("armadyl platelegs") || name.contains("armadyl Chainskirt") || name.contains("torva platelegs") || name.contains("pernix chaps") || name.contains("virtus robe legs")) {
				return true;
			} else if (name.contains("armadyl vambraces")) {
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
							godControler.incrementKillCount(1);
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
