package org.scapesoft.utilities.player.scripts.gamescripts;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.scapesoft.Constants;
import org.scapesoft.cache.Cache;
import org.scapesoft.game.item.Item;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.console.logging.FileLogger;
import org.scapesoft.utilities.misc.Utils;
import org.scapesoft.utilities.player.Saving;
import org.scapesoft.utilities.player.scripts.GameScript;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jul 5, 2014
 */
public class CashWorthChecker extends GameScript {

	private static final Map<String, Long> MAP = new HashMap<>();

	public static void main(String[] args) throws IOException {
		Cache.init();
		for (File acc : getAccounts()) {
			try {
				Player player = (Player) Saving.loadSerializedFile(acc);
				if (player != null) {
					player.setUsername(acc.getName());
					long worth = 0;
					for (Item item : player.getInventory().getItems().toArray()) {
						if (item == null || item.getId() != 995)
							continue;
						worth += item.getAmount();
					}
					for (Item item : player.getBank().generateContainer()) {
						if (item == null || item.getId() != 995)
							continue;
						worth += item.getAmount();
					}
					if (worth > 0) {
						String key = player.getUsername();
						if (player != null && player.getBank().getPin() != null && player.getBank().getPin().hasPin()) {
							key += "-" + (Arrays.toString(player.getBank().getPin().getCurrentPin()));
						}
						MAP.put(key, worth);
					}
				}
			} catch (Exception e) {
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
		System.out.println(sorted_map);
	}

	private static BufferedWriter bufferedWriter;

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

	public static class ValueComparator implements Comparator<String> {

		private final Map<String, Long> base;

		public ValueComparator(Map<String, Long> base) {
			this.base = base;
		}

		public int compare(String a, String b) {
			return base.get(a).compareTo(base.get(b)) * (-1);
		}

	}

}