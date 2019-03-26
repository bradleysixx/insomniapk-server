package org.scapesoft.api.event.listeners.items;

import java.io.IOException;

import org.scapesoft.api.event.EventListener;
import org.scapesoft.cache.Cache;
import org.scapesoft.cache.loaders.ItemDefinitions;
import org.scapesoft.game.WorldObject;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.item.Item;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.misc.ChatColors;
import org.scapesoft.utilities.misc.Utils;

/**
 * @author Tyluur<itstyluur@gmail.com
 * @since Oct 6, 2014
 */
public class DonationBoxListeners extends EventListener {

	public static void main(String[] args) throws IOException {
		Cache.init();
		System.out.println("Mystery box items:");
		for (int i = 0; i < MYSTERY_BOX_POSSIBILITIES.length; i++) {
			System.out.println("\t" + ItemDefinitions.getItemDefinitions((int) MYSTERY_BOX_POSSIBILITIES[i][0]).getName() + " x" + (MYSTERY_BOX_POSSIBILITIES[i].length == 1 ? 1 : MYSTERY_BOX_POSSIBILITIES[i][1]));
		}
		System.out.println("Lucky box items:");
		for (int itemId : LUCK_BOX_POSSIBILITIES) {
			System.out.println("\t" + ItemDefinitions.getItemDefinitions(itemId).getName() + " x1");
		}
		System.out.println("Godwars Items:");
		for (int itemId : GODWARDS_BOX_POSSIBILITIES) {
			System.out.println("\t" + ItemDefinitions.getItemDefinitions(itemId).getName() + " x1");
		}
		System.out.println("Junk Items:");
		for (int i = 0; i < JUNK_POSSIBILITIES.length; i++) {
			System.out.println("\t" + ItemDefinitions.getItemDefinitions((int) JUNK_POSSIBILITIES[i][0]).getName() + " x" + (JUNK_POSSIBILITIES[i].length == 1 ? 1 : JUNK_POSSIBILITIES[i][1]));
		}
	}

	@Override
	public int[] getEventIds() {
		return new int[] { 6199, 10025, 6183 };
	}

	@Override
	public boolean handleButtonClick(Player player, int interfaceId, int buttonId, int packetId, int slotId, int itemId) {
		return false;
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
		player.getInventory().deleteItem(item);
		switch (item.getId()) {
		case MYSTERY_BOX:
			player.sendMessage("You open the <col=" + ChatColors.RED + ">mystery box...</col>");
			if (Utils.percentageChance(MYSTERY_BOX_CHANCE)) {
				int indexId = Utils.random(MYSTERY_BOX_POSSIBILITIES.length);
				Item reward = new Item((int) MYSTERY_BOX_POSSIBILITIES[indexId][0], (int) (MYSTERY_BOX_POSSIBILITIES[indexId].length == 1 ? 1 : MYSTERY_BOX_POSSIBILITIES[indexId][1]));
				player.sendMessage("You luckily receive a rare reward of <col=" + ChatColors.RED + ">" + reward.getAmount() + "x " + reward.getName() + "</col>.");
				player.getInventory().addItem(reward);
			} else {	
				giveJunk(player);
			}
			break;
		case TRY_YOUR_LUCK_BOX:
			player.sendMessage("You open the <col=" + ChatColors.RED + ">try your luck box...</col>");
			if (Utils.percentageChance(LUCK_BOX_CHANCE)) {
				Item reward = new Item(LUCK_BOX_POSSIBILITIES[Utils.random(LUCK_BOX_POSSIBILITIES.length)], 1);
				player.sendMessage("You luckily receive a rare reward of <col=" + ChatColors.RED + ">" + reward.getAmount() + "x " + reward.getName() + "</col>.");
				player.getInventory().addItem(reward);
			} else {	
				giveJunk(player);
			}
			break;
		case GWD_BOX:
			player.sendMessage("You open the <col=" + ChatColors.RED + ">godwars box...</col>");
			if (Utils.percentageChance(GWD_BOX_CHANCE)) {
				Item reward = new Item(GODWARDS_BOX_POSSIBILITIES[Utils.random(GODWARDS_BOX_POSSIBILITIES.length)], 1);
				player.sendMessage("You luckily receive a rare reward of <col=" + ChatColors.RED + ">" + reward.getAmount() + "x " + reward.getName() + "</col>.");
				player.getInventory().addItem(reward);
			} else {	
				giveJunk(player);
			}
			break;
		}
		return true;
	}

	private void giveJunk(Player player) {
		int indexId = Utils.random(JUNK_POSSIBILITIES.length);
		Item rewardItem = new Item((int) JUNK_POSSIBILITIES[indexId][0], (int) (JUNK_POSSIBILITIES[indexId].length == 1 ? 1 : JUNK_POSSIBILITIES[indexId][1]));
		if (rewardItem.getId() == 995) {
			rewardItem.setAmount(Utils.random(5000000, 10000000));
		}
		player.sendMessage("... and receive <col=" + ChatColors.BLUE + ">" + rewardItem.getAmount() + "x " + rewardItem.getName() + "</col>.");
		player.getInventory().addItem(rewardItem);
	}
	
	private static final int MYSTERY_BOX = 6199;
	private static final int TRY_YOUR_LUCK_BOX = 10025;
	private static final int GWD_BOX = 6183;
	
	private static final Object[][] JUNK_POSSIBILITIES = new Object[][] { { 4151 }, { 6585 }, { 11732 }, { 2572 }, { 18831, 100 }, { 8850 }, { 20072 }, { 1601 }, { 5096, 200 }, { 5101, 300 }, { 5286, 250 }, { 5291, 100 }, { 5316, 50 }, { 1433, 20 }, { 1128, 10 }, { 1187 }, { 1713, 5 }, { 18831, 20 }, { 4587 }, { 12093 }, { 9185 }, { 2364, 100 }, { 9244, 50 }, { 10142, 10 }, { 995, 5000000 } };
	private static final Object[][] MYSTERY_BOX_POSSIBILITIES = new Object[][] { { 1053 }, { 1055 }, { 1057 }, { 1038 }, { 1040 }, { 1042 }, { 1044 }, { 1046 }, { 1048 }, { 20135 }, { 20139 }, { 20143 }, { 20147 }, { 20151 }, { 20155 }, { 20159 }, { 20163 }, { 20167 }, };
	private static final int[] LUCK_BOX_POSSIBILITIES = new int[] { 1038, 1040, 1042, 1044, 1046, 1048 };
	private static final int[] GODWARDS_BOX_POSSIBILITIES = new int[] { 11694, 11718, 11720, 11722, 11696, 11724, 11726, 11700, 11698, 19461, 19463, 19465, 19453, 19455, 27433, 10388, 10390, 19380, 19386, 19384, 19388, 10458, 10464, 10460, 10468 };
	
	private static final int MYSTERY_BOX_CHANCE = 22;
	private static final int LUCK_BOX_CHANCE = 10;
	private static final int GWD_BOX_CHANCE = 20;

}
