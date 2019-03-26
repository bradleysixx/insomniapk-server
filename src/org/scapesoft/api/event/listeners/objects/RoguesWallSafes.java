package org.scapesoft.api.event.listeners.objects;

import java.util.ArrayList;
import java.util.List;

import org.scapesoft.api.event.EventListener;
import org.scapesoft.game.Animation;
import org.scapesoft.game.Hit;
import org.scapesoft.game.WorldObject;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.item.Item;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.Skills;
import org.scapesoft.game.player.actions.Action;
import org.scapesoft.utilities.misc.Utils;

/**
 * @author Tyluur<itstyluur@gmail.com
 * @since Oct 25, 2014
 */
public class RoguesWallSafes extends EventListener {

	@Override
	public int[] getEventIds() {
		return new int[] { 7236 };
	}

	@Override
	public boolean handleButtonClick(Player player, int interfaceId, int buttonId, int packetId, int slotId, int itemId) {
		return false;
	}

	@Override
	public boolean handleObjectClick(Player player, int objectId, WorldObject worldObject, WorldTile tile, ClickOption option) {
		player.getActionManager().setAction(new Action() {

			@Override
			public boolean start(Player player) {
				if (player.getInventory().getFreeSlots() == 0) {
					player.sendMessage("You have no room in your inventory.");
					return false;
				}
				if (player.getSkills().getLevelForXp(Skills.THIEVING) < 50) {
					player.sendMessage("You need a thieving level of 50 to attempt to pick this lock.");
					return false;
				}
				return true;
			}

			@Override
			public boolean process(Player player) {
				if (player.getInventory().getFreeSlots() == 0) {
					player.sendMessage("You have no room in your inventory.");
					return false;
				}
				if (player.getSkills().getLevelForXp(Skills.THIEVING) < 50) {
					player.sendMessage("You need a thieving level of 50 to attempt to pick this lock.");
					return false;
				}
				return true;
			}

			@Override
			public int processWithDelay(Player player) {
				setActionDelay(player, 3);
				steal(player);
				return -1;
			}

			@Override
			public void stop(Player player) {
				
			}
			
			private void steal(Player player) {
				if (Utils.percentageChance(30)) {
					player.setNextAnimation(new Animation(3170));
					player.applyHit(new Hit(player, Utils.random(10, 40)));
					return;
				}
				double random = Utils.getRandomDouble(100);
				String reward = "";
				if (random <= VERY_RARE_CHANCE) {
					reward = "very_rare";
				} else if (random <= RARE_CHANCE) {
					reward = "rare";
				} else {
					reward = "basic";
				}
				int itemId = -1;
				switch (reward) {
				case "very_rare":
					itemId = Gems.ONYX.itemId;
					break;
				case "rare":
					itemId = Gems.DRAGONSTONE.itemId;
					break;
				case "basic":
					List<Gems> gems = new ArrayList<Gems>();
					for (Gems gem : Gems.values()) {
						if (gem.chance == BASE_CHANCE) {
							gems.add(gem);
						}
					}
					itemId = gems.get(Utils.random(gems.size())).itemId;
					break;
				}
				player.setNextAnimation(new Animation(2247));
				player.getSkills().addXp(Skills.THIEVING, 50);
				player.getInventory().addItem(itemId, 1);
				player.getInventory().addItem(995, 200);
				player.sendMessage("You find a gem within the wallsafe.");
			}
		});
		return true;
	}

	@Override
	public boolean handleNPCClick(Player player, NPC npc, ClickOption option) {
		return false;
	}

	@Override
	public boolean handleItemClick(Player player, Item item, ClickOption option) {
		return false;
	}

	private enum Gems {

		DIAMOND(1617), RUBY(1619), EMERALD(1621), SAPPHIRE(1623), OPAL(1625), JADE(1627), RED_TOPAZ(1629), DRAGONSTONE(1631, RARE_CHANCE), ONYX(6571, VERY_RARE_CHANCE);

		Gems(int itemId) {
			this.itemId = itemId;
			this.chance = BASE_CHANCE;
		}

		Gems(int itemId, double chance) {
			this.itemId = itemId;
			this.chance = chance;
		}

		private final int itemId;
		private final double chance;
	}

	private static final double BASE_CHANCE = 9.38;
	private static final double RARE_CHANCE = 4.98;
	private static final double VERY_RARE_CHANCE = 0.09;

}
