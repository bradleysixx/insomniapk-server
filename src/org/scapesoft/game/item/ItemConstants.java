package org.scapesoft.game.item;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.scapesoft.Constants;
import org.scapesoft.cache.Cache;
import org.scapesoft.cache.loaders.ItemDefinitions;
import org.scapesoft.game.player.Equipment;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.content.shop.ShopPrices;
import org.scapesoft.game.player.dialogues.impl.Bob;

public class ItemConstants {

	public static int getDegradeItemWhenWear(int id) {
		// Pvp armors
		if (id == 13958 || id == 13961 || id == 13964 || id == 13967 || id == 13970 || id == 13973 || id == 13908 || id == 13911 || id == 13914 || id == 13917 || id == 13920 || id == 13923 || id == 13941 || id == 13944 || id == 13947 || id == 13950 || id == 13958 || id == 13938 || id == 13926 || id == 13929 || id == 13932 || id == 13935)
			return id + 2; // When equiping it becomes Corrupted
		return -1;
	}

	public static int getItemDefaultCharges(int id) {
		// Pvp Armors
		if (id == 13910 || id == 13913 || id == 13916 || id == 13919 || id == 13922 || id == 13925 || id == 13928 || id == 13931 || id == 13934 || id == 13937 || id == 13940 || id == 13943 || id == 13946 || id == 13949 || id == 13952)
			return 1500 * Constants.DEGRADE_GEAR_RATE; // 15minutes
		if (id == 13960 || id == 13963 || id == 13966 || id == 13969 || id == 13972 || id == 13975)
			return 2000 * Constants.DEGRADE_GEAR_RATE;// 20 min.
		if (id == 13860 || id == 13863 || id == 13866 || id == 13869 || id == 13872 || id == 13875 || id == 13878 || id == 13886 || id == 13889 || id == 13892 || id == 13895 || id == 13898 || id == 13901 || id == 13904 || id == 13907 || id == 13960)
			return 6000 * Constants.DEGRADE_GEAR_RATE; // 1hour
		if (Bob.isBarrows(id)) {
			int charges = Bob.getCharges(id);
			if (charges == -1)
				return 1;
			else
				return 3000; // 10 minutes
		}
		return -1;
	}

	public static int getItemDegrade(int id) {
		// nex armors
		if (id == 20137 || id == 20141 || id == 20145 || id == 20149 || id == 20153 || id == 20157 || id == 20161 || id == 20165 || id == 20169 || id == 20173) {
			return id + 1;
		}
		if (Bob.isBarrows(id)) {
			return Bob.getDegraded(false, id);
		}
		return -1;
	}

	public static int getDegradeItemWhenCombating(int id) {
		// Nex and Pvp
		if (id == 20135 || id == 20139 || id == 20143 || id == 20147 || id == 20151 || id == 20155 || id == 20159 || id == 20163 || id == 20167 || id == 20171 || id == 13858 || id == 13861 || id == 13864 || id == 13867 || id == 13870 || id == 13873 || id == 13876 || id == 13884 || id == 13887 || id == 13890 || id == 13893 || id == 13896 || id == 13905 || id == 13902 || id == 13899)
			return id + 2;
		return -1;
	}

	public static boolean itemDegradesWhileHit(int id) {
		if (id == 2550) {
			return true;
		}
		return false;
	}

	public static boolean itemDegradesWhileWearing(int id) {
		String name = ItemDefinitions.getItemDefinitions(id).getName().toLowerCase();
		if (name.contains("c. dragon") || name.contains("corrupt dragon") || name.contains("vesta's") || name.contains("statius'") || name.contains("morrigan's") || name.contains("zuriel's"))
			return true;
		return false;
	}

	public static boolean itemDegradesWhileCombating(int id) {
		if (Bob.isBarrows(id))
			return true;
		return false;
	}

	public static boolean isDestroy(Item item) {
		return item.getDefinitions().isDestroyItem() || item.getDefinitions().isLended();
	}

	public static boolean isLendable(Item item) {
		return !item.getDefinitions().isLended() && item.getDefinitions().getLendId() > -1;
	}

	public static void main(String[] args) throws IOException {
		Cache.init();
		Item item = new Item(16933);
		System.out.println(item.getName() + ", " + item.getDefinitions().getEquipSlot());
		System.out.println(canExchange(item.getId()));
		System.out.println(canExchange(item, null));
		/*
		 * boolean write = true;
		 * 
		 * if (write) { BufferedWriter writer = new BufferedWriter(new
		 * FileWriter(file)); for (int i = 0; i <
		 * Utils.getItemDefinitionsSize(); i++) { item = new Item(i); if
		 * (CacheFixer.canExchange(item)) { writer.write(item.getId() + ", " +
		 * item.getDefinitions().getName()); writer.newLine(); writer.flush(); }
		 * } writer.close(); }
		 */
	}

	public static boolean canExchange(Item item, Player player) {
		if (item.getDefinitions().isExchangeable()) {
			return true;
		}
		if (!item.getDefinitions().isExchangeable()) {
			return false;
		}
		if (!item.getDefinitions().isWearItem()) {
			return false;
		}
		if (!item.getDefinitions().isTradeable()) {
			return false;
		}
		for (int[] items : ShopPrices.VOTE_SHOP) {
			if (item.getId() == items[0]) {
				return false;
			}
		}
		for (int[] items : ShopPrices.WILDY_POINT_SHOP) {
			if (item.getId() == items[0]) {
				return false;
			}
		}
		if (!Equipment.canWear(item, player)) {
			return false;
		}
		return true;
	}

	public static boolean isTradeable(Object object) {
		int itemId = -1;
		if (object instanceof Item) {
			itemId = ((Item) object).getId();
		} else if (object instanceof Integer) {
			itemId = (Integer) object;
		} else {
			throw new IllegalStateException("Invalid parameters! Only Item or Integer can be specified");
		}
		ItemDefinitions definitions = ItemDefinitions.getItemDefinitions(itemId);
		for (String n : forceTradeable) {
			if (definitions.getName().toLowerCase().contains(n.toLowerCase())) {
				return true;
			}
		}
		return definitions.isTradeable();
	}

	public static List<Integer> getUnexchangeables() {
		if (unexchangeables == null) {
			unexchangeables = new ArrayList<>();
			try {
				List<String> text = (ArrayList<String>) Files.readAllLines(new File("./data/exchange/full_exchange_list.txt").toPath(), Charset.defaultCharset());
				for (String line : text) {
					if (line.startsWith("//"))
						continue;
					String[] split = line.split(": ");
					unexchangeables.add(Integer.parseInt(split[1]));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return unexchangeables;
	}

	private static List<Integer> unexchangeables;

	public static boolean shouldExchange(int itemId) {
		return getUnexchangeables().contains(itemId);
	}

	public static boolean canExchange(int itemId) {
		if (!isTradeable(itemId)) {
			return false;
		}
		org.scapesoft.cache.loaders.ItemDefinitions definitions = org.scapesoft.cache.loaders.ItemDefinitions.getItemDefinitions(itemId);
		if (definitions.isNoted()) {
			return false;
		}
		if (!definitions.isExchangeable())
			return false;
		return true;
	}

	private static final String[] forceTradeable = new String[] {};

	/**
	 * Finding out if an item is a rare item that is dropped from monsters
	 * 
	 * @param item
	 *            The item to check for
	 * @return
	 */
	public static boolean isRare(Item item) {
		String name = item.getName().toLowerCase();
		for (String rare : RARE_NAMES) {
			if (name.contains(rare.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * An array of names that will make an item rare so it will appear if a
	 * player gets a drop of it
	 */
	private static final String[] RARE_NAMES = { "hilt", "shard", "whip", "visage", "vesta", "statius", "virtus", "morrigan", "pernix", "torva", "virtus", "sigil", "steadfast", "ragefire", "glaiven", "elixir", "third age" };

}
