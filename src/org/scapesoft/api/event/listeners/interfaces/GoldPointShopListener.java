package org.scapesoft.api.event.listeners.interfaces;

import java.util.ArrayList;
import java.util.List;

import org.scapesoft.api.event.EventListener;
import org.scapesoft.api.input.IntegerInputAction;
import org.scapesoft.cache.loaders.ItemDefinitions;
import org.scapesoft.game.item.Item;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.dialogues.Dialogue;
import org.scapesoft.game.player.dialogues.impl.GoldPointShopD;
import org.scapesoft.utilities.misc.ChatColors;
import org.scapesoft.utilities.misc.Utils;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Jan 17, 2015
 */
public class GoldPointShopListener extends EventListener {

	@Override
	public int[] getEventIds() {
		return new int[] { INTERFACE_ID };
	}

	@Override
	public boolean handleButtonClick(Player player, int interfaceId, int buttonId, int packetId, int slotId, int itemId) {
		if (player.getAttributes().get("goldshop_current") == null) {
			return false;
		}
		if (buttonId == 27) {
			switch (packetId) {
			case 61:
				sendInformation(player, itemId, 1);
				break;
			case 64:
				purchaseItem(player, itemId, 1);
				break;
			case 4:
				purchaseItem(player, itemId, 5);
				break;
			case 52:
				purchaseItem(player, itemId, 10);
				break;
			case 81:
				purchaseItem(player, itemId, Integer.MAX_VALUE);
				break;
			case 91:
				player.getPackets().sendInputIntegerScript("Enter Amount:", new IntegerInputAction() {

					@Override
					public void handle(int input) {
						purchaseItem(player, itemId, input);
					}
					
				});
				break;
			case 18:
				break;
			default:
				System.out.println(packetId);
				break;
			}
		}
		return true;
	}	
	
	public static void openShop(Player player, GoldShop shop) {
		player.closeAllButDialogue();

		player.getInterfaceManager().sendInterface(INTERFACE_ID);
		player.getInterfaceManager().sendInventoryInterface(INVENTORY_INTERFACE_ID);
		player.getPackets().sendIComponentText(INTERFACE_ID, 14, Utils.formatPlayerNameForDisplay(shop.name()));
		player.getPackets().sendItems(ITEMS_KEY, shop.getItems());
		player.getPackets().sendHideIComponent(INTERFACE_ID, 29, true);
		sendOptions(player);
		sendInventoryInformation(player);
		insertFlag(player, shop);
	}
	
	private static void insertFlag(Player player, GoldShop shop) {
		player.getAttributes().put("goldshop_current", shop);
		player.setCloseInterfacesEvent(new Runnable() {

			@Override
			public void run() {
				player.getAttributes().remove("goldshop_current");
				player.getPackets().sendHideIComponent(INTERFACE_ID, 29, false);
			}

		});
	}

	public static void refreshShop(Player player, GoldShop shop) {
		player.getPackets().sendItems(ITEMS_KEY, shop.getItems());
		player.getPackets().sendIComponentText(INTERFACE_ID, 14, Utils.formatPlayerNameForDisplay(shop.name()));
		sendInventoryInformation(player);
		insertFlag(player, shop);
	}

	private static void sendOptions(Player player) {
		player.getPackets().sendUnlockIComponentOptionSlots(INTERFACE_ID, 27, 0, ITEMS_KEY, 0, 1, 2, 3, 4, 5, 6);
		player.getPackets().sendInterSetItemsOptionsScript(INTERFACE_ID, 27, ITEMS_KEY, 6, 5, "Value", "Buy-1", "Buy-5", "Buy-10", "Buy-All", "Buy-X", "Examine");
	}

	private static void sendInventoryInformation(Player player) {
		player.getPackets().sendIComponentText(INVENTORY_INTERFACE_ID, 14, "<col=" + ChatColors.YELLOW + ">You currently have " + Utils.format(player.getFacade().getGoldPoints()) + " gold points.");
		player.getPackets().sendIComponentText(INVENTORY_INTERFACE_ID, 2, "Gold Points");
		
		player.getPackets().sendGlobalConfig(741, 4278);
		player.getPackets().sendGlobalString(25, "<col=" + ChatColors.YELLOW + ">You currently have " + Utils.format(player.getFacade().getGoldPoints()) + " gold points.");
		player.getPackets().sendGlobalString(34, ""); // quest id for some items
		player.getPackets().sendGlobalConfig(746, -1);
		player.getPackets().sendGlobalConfig(745, 0);
		player.getPackets().sendGlobalConfig(168, 98);
	}

	private static void purchaseItem(Player player, int itemId, int amount) {
		int basePrice = getPrice(itemId);
		if (basePrice == -1) {
			return;
		}
		Item buyingItem = GoldShop.getItem(itemId);
		if (buyingItem == null) {
			return;
		}
		if (amount > buyingItem.getAmount()) {
			amount = buyingItem.getAmount();
		}
		long totalPrice = (amount * basePrice);
		long points = player.getFacade().getGoldPoints();
		if (totalPrice > points) {
			player.getDialogueManager().startDialogue(new Dialogue() {

				@Override
				public void start() {
					sendDialogue("You need " + Utils.format(totalPrice) + " gold points!");
				}

				@Override
				public void run(int interfaceId, int option) {
					end();
					player.getDialogueManager().startDialogue(GoldPointShopD.class);
				}

				@Override
				public void finish() {
				}

			});
			return;
		}
		player.getFacade().setGoldPoints(points - totalPrice);
		Item reward = new Item(itemId, amount);
		player.getInventory().addItem(reward);
		sendInventoryInformation(player);
		player.getDialogueManager().startDialogue(new Dialogue() {

			@Override
			public void start() {
				sendDialogue("<col=" + ChatColors.MAROON + ">You have bought " + reward.getAmount() + "x " + reward.getName() + "! Thanks for the purchase.");
			}

			@Override
			public void run(int interfaceId, int option) {
				end();
				player.getDialogueManager().startDialogue(GoldPointShopD.class);
			}

			@Override
			public void finish() {
			}

		});
	}

	public enum GoldShop {

		ARMOUR(new Item(10551, 5000), new Item(11724, 5000), new Item(11726, 5000), new Item(6585, 5000), new Item(18363, 5000), new Item(18361, 5000), new Item(11732, 5000)), 
		UNTRADEABLES(new Item(6570, 5000), new Item(20072, 5000)), 
		RARES(new Item(4278, 5000), new Item(20706, 5000), new Item(6199, 28), new Item(6183, 28), new Item(10025, 28)), 
		WEAPONS(new Item(18353, 5000), new Item(18351, 5000), new Item(18349, 5000), new Item(18355, 5000), new Item(18357, 5000), new Item(15486, 5000), new Item(4151, 5000), new Item(19784, 5000)), 
		MEMBER_ONLY_STOCK(new Item(21467, 5000), new Item(21468, 5000), new Item(21469, 5000), new Item(21470, 5000), new Item(21471, 5000), new Item(21472, 5000), new Item(21473, 5000), new Item(21474, 5000), new Item(21475, 5000), new Item(21476, 5000), new Item(21462, 5000), new Item(21463, 5000), new Item(21464, 5000), new Item(21465, 5000), new Item(21466, 5000));

		GoldShop(Item... items) {
			this.items = items;
		}

		/**
		 * @return the items
		 */
		public Item[] getItems() {
			return items;
		}

		/**
		 * Finds an existing {@code Item} {@code Object} from all gold shops
		 * based on the item id
		 * 
		 * @param itemId
		 *            The item id
		 * @return
		 */
		public static Item getItem(int itemId) {
			for (GoldShop shop : GoldShop.values()) {
				for (Item item : shop.items) {
					if (item.getId() == itemId) {
						return item;
					}
				}
			}
			return null;
		}

		/**
		 * The items in this gold shop
		 */
		private final Item[] items;
	}

	public static void sendInformation(Player player, int itemId, int amount) {
		player.getDialogueManager().startDialogue(new Dialogue() {

			@Override
			public void start() {
				List<String> messages = new ArrayList<>();
				messages.add("<col=" + ChatColors.MAROON + ">" + amount + "x " + ItemDefinitions.getItemDefinitions(itemId).getName() + " costs: " + Utils.format(getPrice(itemId)) + " gold points");
				String information = getInformation(itemId);
				if (information != null) {
					messages.add("<col=" + ChatColors.BLUE + ">" + information);
				}
				sendDialogue(messages.toArray(new String[messages.size()]));
			}

			@Override
			public void run(int interfaceId, int option) {
				end();
				player.getDialogueManager().startDialogue(GoldPointShopD.class);
			}

			@Override
			public void finish() {
			}

		});
	}

	/**
	 * Gets the price of an item id from the #PRICES array
	 *
	 * @param itemId
	 *            The item id
	 * @return
	 */
	protected static int getPrice(int itemId) {
		for (int i = 0; i < PRICES.length; i++) {
			if (itemId == PRICES[i][0]) {
				return PRICES[i][1];
			}
		}
		return -1;
	}

	/**
	 * Gets extra information about the item if it exists in the
	 * {@link #INFORMATION} array
	 * 
	 * @param itemId
	 *            The item id
	 * @return
	 */
	private static String getInformation(int itemId) {
		for (int i = 0; i < INFORMATION.length; i++) {
			if (itemId == (int) INFORMATION[i][0]) {
				return (String) INFORMATION[i][1];
			}
		}
		return null;
	}

	private static final Object[][] INFORMATION = new Object[][] {
	/***/
	{ 20706, "This item imbues all rings into their (i) type" },
	/***/
	{ 4278, "These can be exchanged for 1 gold point and they can be traded." },
	/***/
	{ 10025, "You have a chance at party hats when you open this box!" },
	/***/
	{ 6183, "You have a high chance at godwars items when you open this box!" },
	/***/
	{ 6199, "You have a high chance at extremely rare items when you open this box!" } };

	/**
	 * The array of prices
	 */
	private static final int[][] PRICES = new int[][] { { 18353, 1750 }, { 18351, 1500 }, { 18349, 1500 }, { 4151, 500 }, { 18355, 1350 }, { 18357, 1350 }, { 15486, 700 }, { 10551, 950 }, { 20072, 300 }, { 18363, 2000 }, { 18361, 2000 }, { 6570, 500 }, { 11732, 900 }, { 607, 200 }, { 608, 200 }, { 6769, 500 }, { 786, 500 }, { 11724, 1500 }, { 11726, 1500 }, { 6585, 1000 }, { 11694, 2000 }, { 14484, 1250 }, { 19784, 750 },
	/** Armour */
	{ 13738, 1650 }, { 13740, 2000 }, { 13742, 1600 }, { 13744, 1500 }, { 11283, 1100 }, { 21467, 900 }, { 21468, 900 }, { 21469, 900 }, { 21470, 900 }, { 21471, 900 }, { 21472, 900 }, { 21473, 900 }, { 21474, 900 }, { 21475, 900 }, { 21476, 900 }, { 21462, 900 }, { 21463, 900 }, { 21464, 900 }, { 21465, 900 }, { 21466, 900 }, { 20135, 1500 }, { 20139, 1500 }, { 20143, 1500 }, { 20147, 1400 }, { 20151, 1400 }, { 20155, 1400 }, { 20159, 1450 }, { 20163, 1450 }, { 20167, 1450 },
	/** Rares */
	{ 20706, 1200 }, { 4278, 1}, { 6199, 1000 }, { 10025, 500 }, { 6183, 500 }, { 1053, 1500 }, { 1055, 2000 }, { 1057, 1750 }, { 1040, 2000 }, { 1038, 2000 }, { 1042, 2000 }, { 1044, 2000 }, { 1046, 2000 }, { 1048, 2000 } };

	/**
	 * The interface id of the shop
	 */
	private static final int INTERFACE_ID = 671;

	/**
	 * The interface id of the inventory interface
	 */
	private static final int INVENTORY_INTERFACE_ID = 449;

	/**
	 * The interface id of the shop
	 */
	private static final int ITEMS_KEY = 530;
}
