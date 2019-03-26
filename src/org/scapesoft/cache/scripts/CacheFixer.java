package org.scapesoft.cache.scripts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;

import org.scapesoft.cache.tools.CacheIndex;
import org.scapesoft.game.item.Item;

import com.alex.store.Store;

/**
 * 
 * @author Jonathan
 * 
 */
public class CacheFixer {

	private static ArrayList<String> nonTradeables = new ArrayList<String>();

	static {
		try {
			nonTradeables = (ArrayList<String>) Files.readAllLines(new File("./data/items/nontradeables.txt").toPath(), Charset.defaultCharset());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		Store theirs = new Store("E:/Users/Jonathan/Desktop/RSPS Junk/100% 718 Cache/data/", true);
		Store ours = new Store("./data/cache/");

		System.out.println(ours.getIndexes()[CacheIndex.MODELS].packIndex(theirs));
		System.out.println(ours.getIndexes()[CacheIndex.ITEMS].packIndex(theirs));
	}

	public static int[] getEquipInfo(int id) throws Exception {
		new Item(id);
		BufferedReader reader = new BufferedReader(new FileReader(new File("data/items/equip.txt")));
		String line = "";
		while ((line = reader.readLine()) != null) {
			int lineId = Integer.parseInt(line.substring(0, line.indexOf(":")));
			if (lineId == id) {
				String info = line.substring(line.indexOf(":") + 1, line.length()).trim();
				String[] splitInfo = info.split(",");
				reader.close();
				return new int[] { Integer.parseInt(splitInfo[0].trim()), Integer.parseInt(splitInfo[1].trim()) };
			}
		}
		reader.close();
		return null;
	}

	public static boolean isTradeable(int itemId) {
		org.scapesoft.cache.loaders.ItemDefinitions definitions = org.scapesoft.cache.loaders.ItemDefinitions.getItemDefinitions(itemId);
		switch (itemId) {
		case 10943:
			return true;
		}
		if (definitions.isDestroyItem() || definitions.isLended())
			return false;
		boolean listContained = false;
		for (String listName : nonTradeables) {
			int id = -1;
			try {
				id = Integer.parseInt(listName);
			} catch (Exception e) {

			}
			if (id != -1) {
				if (itemId == id) {
					listContained = true;
					System.out.println("Found by id!");
					break;
				}
			}
			if (definitions.getName().equalsIgnoreCase(listName)) {
				listContained = true;
				System.out.println("Found by name!");
				break;
			}
		}
		if (listContained) {
			return false;
		}
		if (itemId >= 7454 && itemId <= 7462)
			return false;
		switch (itemId) {
		case 18778:
		case 10548:
		case 10551:
		case 6570:
			return false;
		default:
			String name = definitions.getName().toLowerCase();
			if (name.contains("flameburst") || name.contains("ancient effigy") || name.contains("clue scroll") || name.contains("(i)") || name.contains("chaotic"))
				return false;
			return true;
		}
	}

}
