package org.scapesoft.cache.scripts;

import java.io.IOException;

import org.scapesoft.cache.Cache;
import org.scapesoft.cache.loaders.NPCDefinitions;
import org.scapesoft.utilities.misc.Utils;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Apr 18, 2014
 */
public class EquipmentSetOpener {

	public static void main(String... args) throws IOException {
		Cache.init();
		int biggestSize = 0;
		int id = 0;
		for (int i = 0; i < Utils.getNPCDefinitionsSize(); i++) {
			NPCDefinitions definitions = NPCDefinitions.getNPCDefinitions(2745);
			int size = definitions.size;
			if (size > biggestSize) {
				biggestSize = size;
				id = i;
			}

		}
		System.out.println(id+", "+biggestSize);
/*		for (int i = 0; i < Utils.getItemDefinitionsSize(); i++) {
			ItemDefinitions definitions = ItemDefinitions.getItemDefinition(ours, i);
			if (definitions.getName().contains(" set")) {
				System.out.println("Set the first option for " + definitions.getName() + "[" + i + "] to 'Open'");
				definitions.getInventoryOptions()[0] = "Open";
				definitions.write(ours, true);
			}
		}*/
	}

}