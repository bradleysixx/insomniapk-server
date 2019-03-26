package org.scapesoft.cache.scripts;

import java.io.IOException;

import org.scapesoft.cache.Cache;
import org.scapesoft.utilities.misc.Utils;

import com.alex.loaders.items.ItemDefinitions;
import com.alex.store.Store;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Apr 18, 2014
 */
public class ItemNameChanger {

	public static void main(String... args) throws IOException {
		Cache.init();
		Store ours = new Store("./data/cache/");
		for (int i = 0; i < Utils.getItemDefinitionsSize(); i++) {
			/*for (Object[] element : OBJECTS) {
				if (i == (int) element[0]) {
					ItemDefinitions definitions = ItemDefinitions.getItemDefinition(ours, i);
					String prevName = definitions.getName();
					definitions.setName((String) element[1]);
					System.out.println("Set the name of " + i + "[ " + prevName + "] to " + definitions.getName());
					definitions.write(ours, true);
				}
			}*/
			if (i == 20706) {
				ItemDefinitions definitions = ItemDefinitions.getItemDefinition(ours, i);
				String prevName = definitions.getName();
				definitions.setName("Magic Imbuent");
				String[] options = definitions.getInventoryOptions();
				options[0] = null;
				options[1] = null;
				//definitions.setInventoryOptions(options);
				System.out.println("Set the name of " + i + "[ " + prevName + "] to " + definitions.getName());
				//definitions.write(ours, true);
			}
		}
	}

	private static final Object[][] OBJECTS = new Object[][] { { 10025, "Lucky Box" }, { 6183, "Godwars Box" } };

}