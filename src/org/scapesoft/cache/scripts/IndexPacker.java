package org.scapesoft.cache.scripts;

import com.alex.loaders.items.ItemDefinitions;
import com.alex.store.Store;
import com.alex.utils.Constants;

public class IndexPacker {

	private static final Store matrix718 = new Store("C:/Users/HP/Desktop/Me/Programming/- SERVERS -/600+/[SALLESY] 718 Server/data/cache/", true);
	private static final Store ours = new Store("C:/Users/HP/Desktop/cache/", false);
	private static final int INDEX = Constants.SPRITES_INDEX;

	public static void main(String[] args) {
		System.out.println(ours.getIndexes()[INDEX].packIndex(matrix718));
	}

}