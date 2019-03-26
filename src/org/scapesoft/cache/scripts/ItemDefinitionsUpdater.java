package org.scapesoft.cache.scripts;

import java.io.IOException;

import org.scapesoft.cache.Cache;
import org.scapesoft.utilities.misc.Utils;

import com.alex.loaders.items.ItemDefinitions;
import com.alex.store.Store;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Nov 21, 2014
 */
public class ItemDefinitionsUpdater {
	
	public static void main(String[] args) throws IOException {
		Cache.init();
		Store ours = new Store("./data/cache/");
		for (int i = 0; i < Utils.getItemDefinitionsSize(); i++) {
			ItemDefinitions definitions = ItemDefinitions.getItemDefinition(ours, i);
			//definitions.write(ours, true);
			if (i % 500 == 0) {
				System.out.println(i + " is finished.");
			}
		}
	}

}
