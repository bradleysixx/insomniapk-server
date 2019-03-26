package org.scapesoft.api.event.listeners.interfaces;

import org.scapesoft.api.event.EventListener;
import org.scapesoft.api.input.IntegerInputAction;
import org.scapesoft.cache.loaders.ItemDefinitions;
import org.scapesoft.game.WorldObject;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.item.Item;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.dialogues.SimpleNPCMessage;
import org.scapesoft.utilities.misc.Utils;

/**
 * @author Tyluur<itstyluur@gmail.com
 * @since Oct 26, 2014
 */
public final class PlankCreationListener extends EventListener {

	@Override
	public int[] getEventIds() {
		return new int[] { 403 };
	}

	public static void display(Player player) {
		int interfaceId = 403;
		for (Planks plank : Planks.values()) {
			player.getPackets().sendIComponentText(interfaceId, 20 + plank.ordinal(), Utils.formatPlayerNameForDisplay(plank.name()) + "<br>Cost = " + Utils.format(plank.getCost()) + " per log");
		}
		player.getInterfaceManager().sendInterface(interfaceId);
	}

	@Override
	public boolean handleButtonClick(Player player, int interfaceId, int buttonId, int packetId, int slotId, int itemId) {
		Planks plank = (buttonId == 12 ? Planks.WOOD : buttonId == 13 ? Planks.OAK : buttonId == 14 ? Planks.TEAK : buttonId == 15 ? Planks.MAHOGANY : null);
		if (plank == null) {
			return true;
		}
		int amount = 0;
		switch (packetId) {
		case 61:
			amount = 1;
			break;
		case 64:
			amount = 5;
			break;
		case 4:
			amount = 10;
			break;
		case 52:
			player.getPackets().sendInputIntegerScript("Enter Amount", new IntegerInputAction() {

				@Override
				public void handle(int input) {
					makePlanks(player, plank, input);
				}
				
			});
			return true;
		case 81:
			amount = player.getInventory().getAmountOf(plank.getLogId());
			break;
		}
		if (amount <= 0) {
			return true;
		}
		makePlanks(player, plank, amount);
		return true;
	}
	
	private void makePlanks(Player player, Planks plank, int amount) {
		int logCount = player.getInventory().getAmountOf(plank.getLogId());
		if (amount > logCount) {
			amount = logCount;
		}
		if (!player.getInventory().containsItem(plank.getLogId(), amount)) {
			player.sendMessage("You don't have that many " + ItemDefinitions.getItemDefinitions(plank.getLogId()).name.toLowerCase() + "!");
			return;
		}
		int cost = plank.getCost() * amount;
		if (!player.takeMoney(cost)) {
			player.getDialogueManager().startDialogue(SimpleNPCMessage.class, 4250, "You need " + Utils.format(cost) + " coins to do this.");
			return;
		}
		player.getInventory().deleteItem(plank.getLogId(), amount);
		player.getInventory().addItem(plank.getPlankId(), amount);
	}

	@Override
	public boolean handleObjectClick(Player player, int objectId, WorldObject worldObject, WorldTile tile, ClickOption option) {
		return false;
	}

	@Override
	public boolean handleNPCClick(Player player, NPC npc, ClickOption option) {
		return false;
	}

	@Override
	public boolean handleItemClick(Player player, Item item, ClickOption option) {
		return false;
	}

	public enum Planks {

		WOOD(960, 1511, 1250),
		OAK(8778, 1521, 5750),
		TEAK(8780, 6333, 8850),
		MAHOGANY(8782, 6332, 12500);

		Planks(int plankId, int logId, int cost) {
			this.plankId = plankId;
			this.logId = logId;
			this.cost = cost;
		}

		private final int plankId;
		private final int logId;
		private final int cost;
		
		public static Planks forId(int id) {
			for (Planks plank : Planks.values()) {
				if (plank.getLogId() == id) {
					return plank;
				}
			}
			return null;
		}

		public int getPlankId() {
			return plankId;
		}

		public int getLogId() {
			return logId;
		}

		public int getCost() {
			return cost;
		}
	}

}
