package org.scapesoft.utilities.player.scripts.gamescripts;

import java.io.IOException;
import java.util.List;
import java.util.ListIterator;

import org.scapesoft.cache.Cache;
import org.scapesoft.utilities.console.gson.GsonHandler;
import org.scapesoft.utilities.console.gson.extra.Punishment;
import org.scapesoft.utilities.console.gson.impl.PunishmentLoader;

/**
 * @author Tyluur<itstyluur@gmail.com
 * @since Oct 23, 2014
 */
public class PunishmentRemover {
	
	public static void main(String[] args) throws IOException {
		Cache.init();
		GsonHandler.initialize();
		PunishmentLoader clazz = GsonHandler.getJsonLoader(PunishmentLoader.class);
		List<Punishment> punishments = clazz.getList();
		ListIterator<Punishment> it$ = punishments.listIterator();
		while(it$.hasNext()) {
			Punishment punishment = it$.next();
			if (punishment.getKey().equalsIgnoreCase("115.188.43.12")) {
				it$.remove();
				System.out.println("Removed punishment " + punishment);
				break;
			}
		}
		clazz.save(punishments);
	}

}
