package org.scapesoft.game.player.content.shop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.CopyOnWriteArrayList;

import org.scapesoft.cache.loaders.ItemDefinitions;
import org.scapesoft.game.item.Item;
import org.scapesoft.game.item.ItemConstants;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.console.logging.FileLogger;
import org.scapesoft.utilities.game.item.ItemExamines;
import org.scapesoft.utilities.misc.Utils;

public class Shop {

	private static final int MAX_SHOP_ITEMS = 40;
	public static final int COINS = 995;
	public int id = 0;

	private String name;
	private Item[] mainStock;
	private Item[] generalStock;
	private int money;
	private CopyOnWriteArrayList<Player> viewingPlayers;

	private int[] defaultQuantity;

	public Shop(String name, int money, Item[] mainStock, boolean isGeneralStore) {
		viewingPlayers = new CopyOnWriteArrayList<Player>();
		this.setName(name);
		this.money = money;
		this.mainStock = mainStock;
		defaultQuantity = new int[mainStock.length];
		for (int i = 0; i < defaultQuantity.length; i++) {
			defaultQuantity[i] = mainStock[i].getAmount();
		}
		if (isGeneralStore && mainStock.length < MAX_SHOP_ITEMS) {
			generalStock = new Item[MAX_SHOP_ITEMS - mainStock.length];
		}
	}

	public void initialize() {
		viewingPlayers = new CopyOnWriteArrayList<Player>();
		defaultQuantity = new int[mainStock.length];
		if (isGeneralStore()) {
			generalStock = new Item[MAX_SHOP_ITEMS];
		}
		for (int i = 0; i < mainStock.length; i++) {
			if (mainStock[i] == null) {
				continue;
			}
			defaultQuantity[i] = mainStock[i].getAmount();
		}
		fixMainStock();
	}

	private void fixMainStock() {
		Item[] stock = new Item[mainStock.length];
		for (int i = 0; i < mainStock.length; i++) {
			if (mainStock[i] == null || mainStock[i].getId() == -1 || mainStock[i].getAmount() == -1) {
				continue;
			}
			stock[i] = mainStock[i];
		}
		mainStock = stock;
	}

	public boolean isGeneralStore() {
		return name.equalsIgnoreCase("General Store");
	}

	public void addPlayer(final Player player) {
		viewingPlayers.add(player);
		player.getTemporaryAttributtes().put("Shop", this);
		player.setCloseInterfacesEvent(new Runnable() {
			@Override
			public void run() {
				viewingPlayers.remove(player);
				player.getTemporaryAttributtes().remove("Shop");
			}
		});
		
		player.getPackets().sendConfig(118, 4);
		player.getPackets().sendConfig(1496, -1); // sets samples items set
		player.getPackets().sendConfig(532, getName().contains("Tokkul") ? 6529 : money);
		sendStore(player);
		player.getPackets().sendGlobalConfig(199, -1);// unknown
		player.getInterfaceManager().sendInterface(620); // opens shop
		for (int i = 0; i < MAX_SHOP_ITEMS; i++) {
			player.getPackets().sendGlobalConfig(946 + i, i < defaultQuantity.length ? defaultQuantity[i] : generalStock != null ? 0 : -1);// prices
		}
		player.getPackets().sendGlobalConfig(1241, 16750848);// unknown
		player.getPackets().sendGlobalConfig(1242, 15439903);// unknown
		player.getPackets().sendGlobalConfig(741, -1);// unknown
		player.getPackets().sendGlobalConfig(743, -1);// unknown
		player.getPackets().sendGlobalConfig(744, 0);// unknown
		if (generalStock != null) {
			player.getPackets().sendHideIComponent(620, 19, false);
		}
		player.getPackets().sendIComponentSettings(620, 25, 0, getStoreSize() * 6, 1150); // unlocks stock slots
		sendInventory(player);
		player.getPackets().sendIComponentText(620, 20, getName());
		CustomShops shops = CustomShops.getCustomShop(name);
		if (shops != null) {
			shops.updateShopCurrency(player);
		}
	}

	public void sendInventory(Player player) {
		player.getInterfaceManager().sendInventoryInterface(621);
		player.getPackets().sendItems(93, player.getInventory().getItems());
		player.getPackets().sendUnlockIComponentOptionSlots(621, 0, 0, 27, 0, 1, 2, 3, 4, 5);
		player.getPackets().sendInterSetItemsOptionsScript(621, 0, 93, 4, 7, "Value", "Sell 1", "Sell 5", "Sell 10", "Sell 50", "Examine");
	}

	public void addItem(Item item) {
		List<Item> list = new ArrayList<Item>(Arrays.asList(mainStock));
		ListIterator<Item> it = list.listIterator();
		while (it.hasNext()) {
			Item i = it.next();
			if (i == null) {
				it.remove();
			}
		}
		list.add(item);
		mainStock = list.toArray(mainStock);
	}

	public Item[] getMainStock() {
		return this.mainStock;
	}

	public void removeItem(Item item) {
		List<Item> list = new ArrayList<Item>(Arrays.asList(mainStock));
		ListIterator<Item> it = list.listIterator();
		while (it.hasNext()) {
			Item i = it.next();
			if (i == null) {
				it.remove();
			}
		}
		list.remove(item);
		mainStock = list.toArray(mainStock);
	}

	public int getSlotId(int clickSlotId) {
		return clickSlotId / 6;
	}

	public void buy(Player player, int clickSlot, int quantity) {
		int slotId = getSlotId(clickSlot);
		if (slotId >= getStoreSize()) {
			return;
		}
		Item item = slotId >= mainStock.length ? generalStock[slotId - mainStock.length] : mainStock[slotId];
		if (item == null) {
			return;
		}
		if (item.getAmount() == 0) {
			player.getPackets().sendGameMessage("There is no stock of that item at the moment.");
			return;
		}
		int dq = slotId >= mainStock.length ? 0 : defaultQuantity[slotId];
		int price = getBuyPrice(item, dq);
		int amountCoins = player.getInventory().getItems().getNumberOf(money);
		int maxQuantity = amountCoins / price;
		int buyQ = item.getAmount() > quantity ? quantity : item.getAmount();
		CustomShops shops = CustomShops.getCustomShop(name);
		if (shops != null) {
			if (shops.buyCustomCurrency(player, item, buyQ)) {
				if (player.getTemporaryAttributtes().get("custom_shop_price") != null) {
					int cost = (int) player.getTemporaryAttributtes().get("custom_shop_price");
					shops.onBuy(player, item, buyQ, (int) player.getTemporaryAttributtes().remove("custom_shop_price"));
					FileLogger.getFileLogger().writeLog("shops/", player.getDisplayName() + " bought:[" + item.getAmount() + "x " + item.getName() + ", id=" + item.getId() + "] from " + getName() + " cost: " + cost, true);
					shops.updateShopCurrency(player);
				} else {
					System.out.println("Attempted to buy an item buy the shop price was null in attributes.");
				}
			}
			return;
		}
		boolean enoughCoins = maxQuantity >= buyQ;
		if (!enoughCoins) {
			player.getPackets().sendGameMessage("You don't have enough coins.");
			buyQ = maxQuantity;
		} else if (quantity > buyQ) {
			player.getPackets().sendGameMessage("The shop has run out of stock.");
		}
		if (item.getDefinitions().isStackable()) {
			if (player.getInventory().getFreeSlots() < 1) {
				player.getPackets().sendGameMessage("Not enough space in your inventory.");
				return;
			}
		} else {
			int freeSlots = player.getInventory().getFreeSlots();
			if (buyQ > freeSlots) {
				buyQ = freeSlots;
				player.getPackets().sendGameMessage("Not enough space in your inventory.");
			}
		}
		if (buyQ != 0) {
			int totalPrice = price * buyQ;
			player.getInventory().deleteItem(money, totalPrice);
			player.getInventory().addItem(item.getId(), buyQ);
			item.setAmount(item.getAmount() - buyQ);
			if (item.getAmount() <= 0 && slotId >= mainStock.length) {
				generalStock[slotId - mainStock.length] = null;
			}
			refreshShop();
			sendInventory(player);
		}
	}

	public void restoreItems() {
		boolean needRefresh = false;
		for (int i = 0; i < mainStock.length; i++) {
			if (mainStock[i] == null) {
				continue;
			}
			if (mainStock[i].getAmount() < defaultQuantity[i]) {
				mainStock[i].setAmount(mainStock[i].getAmount() + 1);
				needRefresh = true;
			} else if (mainStock[i].getAmount() > defaultQuantity[i]) {
				mainStock[i].setAmount(mainStock[i].getAmount() + -1);
				needRefresh = true;
			}
		}
		if (generalStock != null) {
			for (int i = 0; i < generalStock.length; i++) {
				Item item = generalStock[i];
				if (item == null) {
					continue;
				}
				item.setAmount(item.getAmount() - 1);
				if (item.getAmount() <= 0) {
					generalStock[i] = null;
				}
				needRefresh = true;
			}
		}
		if (needRefresh) {
			refreshShop();
		}
	}

	private boolean addItem(int itemId, int quantity) {
		for (Item item : mainStock) {
			if (item.getId() == itemId) {
				item.setAmount(item.getAmount() + quantity);
				refreshShop();
				return true;
			}
		}
		if (generalStock != null) {
			for (Item item : generalStock) {
				if (item == null) {
					continue;
				}
				if (item.getId() == itemId) {
					item.setAmount(item.getAmount() + quantity);
					refreshShop();
					return true;
				}
			}
			for (int i = 0; i < generalStock.length; i++) {
				if (generalStock[i] == null) {
					generalStock[i] = new Item(itemId, quantity);
					refreshShop();
					return true;
				}
			}
		}
		return false;
	}

	public void sell(Player player, int slotId, int quantity) {
		if (player.getInventory().getItemsContainerSize() < slotId) {
			return;
		}
		Item item = player.getInventory().getItem(slotId);
		if (item == null) {
			return;
		}

		int originalId = item.getId();
		if (item.getDefinitions().isNoted()) {
			item = new Item(item.getDefinitions().getCertId(), item.getAmount());
		}
		if (item.getDefinitions().isDestroyItem() || ItemConstants.getItemDefaultCharges(item.getId()) != -1 || !ItemConstants.isTradeable(item) || item.getId() == money) {
			player.getPackets().sendGameMessage("You can't sell this item.");
			return;
		}
		CustomShops shops = CustomShops.getCustomShop(name);
		if (shops != null) {
			player.sendMessage("You cannot sell any items to this shop.");
			return;
		}
		int dq = getDefaultQuantity(item.getId());
		if (dq == 0 && generalStock == null) {
			player.getPackets().sendGameMessage("You can't sell this item to this shop.");
			return;
		}
		int price = ItemDefinitions.getItemDefinitions(item.getId()).getValue() / 4;
		int numberOff = player.getInventory().getItems().getNumberOf(originalId);
		if (quantity > numberOff) {
			quantity = numberOff;
		}
		if (!addItem(item.getId(), quantity)) {
			player.getPackets().sendGameMessage("Shop is currently full.");
			return;
		}
		player.getInventory().deleteItem(originalId, quantity);
		player.getInventory().addItem(money, price * quantity);
	}

	public void sendValue(Player player, int slotId) {
		if (player.getInventory().getItemsContainerSize() < slotId) {
			return;
		}
		Item item = player.getInventory().getItem(slotId);
		if (item == null) {
			return;
		}
		if (item.getDefinitions().isNoted()) {
			item = new Item(item.getDefinitions().getCertId(), item.getAmount());
		}
		if (item.getDefinitions().isNoted() || !ItemConstants.isTradeable(item) || item.getId() == money) {
			player.getPackets().sendGameMessage("You can't sell this item.");
			return;
		}
		int dq = getDefaultQuantity(item.getId());
		if (dq == 0 && generalStock == null) {
			player.getPackets().sendGameMessage("You can't sell this item to this shop.");
			return;
		}
		int price = ItemDefinitions.getItemDefinitions(item.getId()).getValue() / 4;
		player.getPackets().sendGameMessage(item.getDefinitions().getName() + ": shop will buy for: " + price + " " + ItemDefinitions.getItemDefinitions(money).getName().toLowerCase() + ". Right-click the item to sell.");
	}

	public int getDefaultQuantity(int itemId) {
		for (int i = 0; i < mainStock.length; i++) {
			if (mainStock[i] == null) {
				continue;
			}
			if (mainStock[i].getId() == itemId) {
				return defaultQuantity[i];
			}
		}
		return 0;
	}

	public void sendInfo(Player player, int clickSlot) {
		int slotId = getSlotId(clickSlot);
		if (slotId >= getStoreSize()) {
			return;
		}
		Item item = slotId >= mainStock.length ? generalStock[slotId - mainStock.length] : mainStock[slotId];
		if (item == null) {
			return;
		}
		player.getTemporaryAttributtes().put("ShopSelectedSlot", clickSlot);
		int dq = slotId >= mainStock.length ? 0 : defaultQuantity[slotId];
		int price = getBuyPrice(item, dq);
		CustomShops shops = CustomShops.getCustomShop(name);
		if (shops != null) {
			shops.sendCost(player, item);
		} else {
			player.getPackets().sendGameMessage(item.getDefinitions().getName() + ": currently costs " + Utils.format(price) + " " + ItemDefinitions.getItemDefinitions(money).getName().toLowerCase() + ".");
		}
	}

	public int getBuyPrice(Item item, int dq) {
		int value = ItemDefinitions.getItemDefinitions(item.getId()).getValue();
		return value <= 0 ? 1 : value;
	}

	public int getSellPrice(Item item, int dq) {
		return (ItemDefinitions.getItemDefinitions(item.getId()).getValue() * (3 / 4));
	}

	public void sendExamine(Player player, int clickSlot) {
		int slotId = getSlotId(clickSlot);
		if (slotId >= getStoreSize()) {
			return;
		}
		Item item = slotId >= mainStock.length ? generalStock[slotId - mainStock.length] : mainStock[slotId];
		if (item == null) {
			return;
		}
		player.getPackets().sendGameMessage(ItemExamines.getExamine(item));
	}

	public void refreshShop() {
		for (Player player : viewingPlayers) {
			sendStore(player);
			player.getPackets().sendIComponentSettings(620, 25, 0, getStoreSize() * 6, 1150);
		}
	}

	public int getStoreSize() {
		return mainStock.length + (generalStock != null ? generalStock.length : 0);
	}

	public void sendStore(Player player) {
		Item[] stock = new Item[mainStock.length + (generalStock != null ? generalStock.length : 0)];
		System.arraycopy(mainStock, 0, stock, 0, mainStock.length);
		if (generalStock != null) {
			System.arraycopy(generalStock, 0, stock, mainStock.length, generalStock.length);
		}
		player.getPackets().sendItems(4, stock);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}
