package com.alex.tools.clientCacheUpdater;

import com.alex.store.Store;

public class Ziotic667Packer {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		Store ziotic = new Store("C:\\Users\\Velocity\\My Dropbox\\Dropbox\\Mally Mally\\Ziotic\\Server\\data\\rs2cache\\");
		Store sixsixseven = new Store("C:\\Users\\Velocity\\My Dropbox\\Dropbox\\RSPS\\667 Framework\\data\\cache\\");
		
		//System.out.println("Models: " + ziotic.getIndexes()[7].packIndex(sixsixseven));
		//System.out.println("Items: " + ziotic.getIndexes()[19].packIndex(sixsixseven));
		//System.out.println("Objects: " + ziotic.getIndexes()[18].packIndex(sixsixseven));
		//System.out.println("NPCs: " + ziotic.getIndexes()[16].packIndex(sixsixseven));
		System.out.println("Maps: " + ziotic.getIndexes()[5].packIndex(sixsixseven));
	}

}
