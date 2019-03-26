package org.scapesoft.game.npc.others;

import org.scapesoft.game.Animation;
import org.scapesoft.game.Entity;
import org.scapesoft.game.World;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.item.Item;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.npc.combat.NPCCombatDefinitions;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.controlers.impl.guilds.warriors.WarriorsGuild;
import org.scapesoft.game.player.controlers.impl.guilds.warriors.WarriorsGuildData.WarriorSet;
import org.scapesoft.game.tasks.WorldTask;
import org.scapesoft.game.tasks.WorldTasksManager;
import org.scapesoft.utilities.misc.Utils;

public class AnimatedArmor extends NPC {

	/**
	 *
	 */
	private static final long serialVersionUID = -3490465937456559584L;
	private transient Player player;

	public AnimatedArmor(Player player, int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
		this.player = player;
	}

	@Override
	public void processNPC() {
		super.processNPC();
		if (!getCombat().underCombat()) {
			finish();
		}
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
						for (Integer items : getDroppedItems()) {
							if (items == -1) {
								continue;
							}
							World.addGroundItem(new Item(items), new WorldTile(getCoordFaceX(getSize()), getCoordFaceY(getSize()), getPlane()), player, true, 60);
						}
						player.setWarriorPoints(3, WarriorsGuild.ARMOR_POINTS[getId() - 4278]);
					}
					finish();
					stop();
				}
				loop++;
			}
		}, 0, 1);
	}

	public int[] getDroppedItems() {
		int index = getId() - 4278;
		int[] droppedItems = WarriorSet.values()[index].getArmourIds();
		if (Utils.getRandom(15) == 0) {
			droppedItems[Utils.random(0, 2)] = -1;
		}
		return droppedItems;
	}

	@Override
	public void finish() {
		if (hasFinished()) {
			return;
		}
		super.finish();
		if (player != null) {
			player.getTemporaryAttributtes().remove("animator_spawned");
			if (!isDead()) {
				for (int item : getDroppedItems()) {
					if (item == -1) {
						continue;
					}
					player.getInventory().addItemDrop(item, 1);
				}
			}
		}
	}
}
