package org.scapesoft.utilities.console.gson.impl;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.scapesoft.cache.Cache;
import org.scapesoft.cache.loaders.ItemDefinitions;
import org.scapesoft.utilities.console.gson.GsonHandler;
import org.scapesoft.utilities.console.gson.GsonLoader;
import org.scapesoft.utilities.game.npc.drops.Drop;
import org.scapesoft.utilities.game.npc.drops.NPCDrop;
import org.scapesoft.utilities.game.npc.drops.Drop.Chance;

import com.google.gson.reflect.TypeToken;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Mar 31, 2014
 */
public class NPCDropManager extends GsonLoader<NPCDrop> {

	public static void main(String... args) throws IOException {
		Cache.init();
		GsonHandler.initialize();
		NPCDropManager loader = GsonHandler.getJsonLoader(NPCDropManager.class);

		List<NPCDrop> drops = loader.generateList();

		ListIterator<NPCDrop> it = drops.listIterator();
		while (it.hasNext()) {
			NPCDrop drop = it.next();
			List<Drop> npcDrops = drop.getDrops();

			ListIterator<Drop> it2 = npcDrops.listIterator();
			while (it2.hasNext()) {
				Drop d = it2.next();
				ItemDefinitions defs = ItemDefinitions.getItemDefinitions(d.getItemId());
				if (d.getRate() == Chance.CUSTOM_RARE) {
					System.out.println(defs.getName() + " is custom. Dropped by " + drop.getName());
				}
			}
		}
	}

	@Override
	public void initialize() {
		List<NPCDrop> list = load();
		for (NPCDrop drop : list) {
			getDropMap().put(drop.getName(), drop.getDrops());
		}
	}

	@Override
	public String getFileLocation() {
		return "data/json/drops.json";
	}

	@Override
	protected List<NPCDrop> load() {
		List<NPCDrop> autospawns = null;
		String json = null;
		try {
			File file = new File(getFileLocation());
			if (!file.exists()) {
				return null;
			}
			FileReader reader = new FileReader(file);
			char[] chars = new char[(int) file.length()];
			reader.read(chars);
			json = new String(chars);
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		autospawns = gson.fromJson(json, new TypeToken<List<NPCDrop>>() {
		}.getType());
		return autospawns;
	}

	/**
	 * Gets the drops by the npc name
	 *
	 * @param name
	 *            The npc name
	 * @return
	 */
	public static List<Drop> getDrops(String name) {
		return getDropMap().get(name.replaceAll(" ", "_").trim());
	}

	/**
	 * @return the dropmap
	 */
	public static Map<String, List<Drop>> getDropMap() {
		return dropMap;
	}

	/**
	 * The array of possible charms to drop
	 */
	public static final int[] CHARMS = { 12158, 12159, 12160, 12163 };

	/**
	 * The map of drops
	 */
	private static final Map<String, List<Drop>> dropMap = new HashMap<String, List<Drop>>();

}
