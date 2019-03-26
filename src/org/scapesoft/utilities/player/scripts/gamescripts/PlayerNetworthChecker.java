package org.scapesoft.utilities.player.scripts.gamescripts;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.scapesoft.Constants;
import org.scapesoft.cache.Cache;
import org.scapesoft.game.item.Item;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.content.exchange.ExchangeOffer;
import org.scapesoft.utilities.console.gson.GsonHandler;
import org.scapesoft.utilities.console.gson.impl.ExchangeItemLoader;
import org.scapesoft.utilities.console.gson.impl.ExchangePriceLoader;
import org.scapesoft.utilities.console.logging.FileLogger;
import org.scapesoft.utilities.misc.Utils;
import org.scapesoft.utilities.player.Saving;
import org.scapesoft.utilities.player.scripts.GameScript;
import org.scapesoft.utilities.player.scripts.gamescripts.CashWorthChecker.ValueComparator;

public class PlayerNetworthChecker extends GameScript {

	public static void main(String[] args) throws IOException {
		Cache.init();
		GsonHandler.initialize();
		for (File acc : getAccounts()) {
			try {
				Player player = (Player) Saving.loadSerializedFile(acc);
				if (player != null) {
					player.setUsername(acc.getName());
					long worth = 0;
					for (Item item : player.getInventory().getItems().toArray()) {
						if (item == null)
							continue;
						worth += (item.getId() == 995 ? item.getAmount() : item.getAmount() * getValue(item));
					}
					for (Item item : player.getEquipment().getItems().toArray()) {
						if (item == null)
							continue;
						worth += (item.getId() == 995 ? item.getAmount() : item.getAmount() * getValue(item));
					}
					for (Item item : player.getBank().generateContainer()) {
						if (item == null)
							continue;
						worth += (item.getId() == 995 ? item.getAmount() : item.getAmount() * getValue(item));
					}
					if (player.getFamiliar() != null) {
						if (player.getFamiliar().getBob() != null) {
							for (Item item : player.getFamiliar().getBob().getBeastItems().toArray()) {
								if (item == null) {
									continue;
								}
								worth += (item.getId() == 995 ? item.getAmount() : item.getAmount() * getValue(item));
							}
						}
					}
					List<ExchangeOffer> offers = GsonHandler.<ExchangeItemLoader>getJsonLoader(ExchangeItemLoader.class).getOffersList(player.getUsername().replaceAll(".p", ""));
					for (ExchangeOffer offer : offers) {
						worth += (offer.getItemId() == 995 ? offer.getAmountProcessed() : offer.getAmountProcessed() * offer.getPrice());
					}
					String bankPin = getPin(player);
					if (worth > 0) {
						MAP.put(player.getUsername() + "" + (bankPin == null ? "" : "\t\tPIN: " + bankPin), worth);
					}
					player.getLoyaltyManager().forcePoints(0);
					player.getAppearence().forceTitle(-1);
					savePlayer(player, acc);
				}
			} catch (Exception e) {
				System.err.println("Error on: " + acc.getAbsolutePath());
				e.printStackTrace();
			}
		}
		ValueComparator bvc = new ValueComparator(MAP);
		TreeMap<String, Long> sorted_map = new TreeMap<String, Long>(bvc);
		sorted_map.putAll(MAP);

		Iterator<Entry<String, Long>> it = sorted_map.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Long> entry = it.next();
			write(entry.getKey() + "\t has a networth of:\t" + Utils.format(entry.getValue()));
		}
		System.out.println("DONE!");
	}
	
	private static int getValue(Item item) {
		ExchangePriceLoader loader = GsonHandler.getJsonLoader(ExchangePriceLoader.class);
		return loader.getAveragePrice(item.getId());
	}

	private static String getPin(Player player) {
		if (player.getBank().getPin() != null && player.getBank().getPin().hasPin()) {
			return (Arrays.toString(player.getBank().getPin().getCurrentPin()));
		}
		return null;
	}

	private static BufferedWriter bufferedWriter;
	private static final Map<String, Long> MAP = new HashMap<>();

	private static void write(String data) {
		if (bufferedWriter == null) {
			try {
				bufferedWriter = new BufferedWriter(new FileWriter(Constants.isVPS ? FileLogger.getFileLogger().getLocation() + "networths.txt" : "info/script/networth.txt"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			bufferedWriter.write(data);
			bufferedWriter.newLine();
			bufferedWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}