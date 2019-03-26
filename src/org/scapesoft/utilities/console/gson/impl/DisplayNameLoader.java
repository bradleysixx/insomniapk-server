package org.scapesoft.utilities.console.gson.impl;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import org.scapesoft.Constants;
import org.scapesoft.cache.Cache;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.console.gson.GsonHandler;
import org.scapesoft.utilities.console.gson.GsonLoader;
import org.scapesoft.utilities.console.gson.extra.DisplayName;
import org.scapesoft.utilities.misc.Utils;
import org.scapesoft.utilities.player.Saving;
import org.scapesoft.utilities.player.scripts.GameScript;

import com.google.gson.reflect.TypeToken;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Dec 3, 2014
 */
@SuppressWarnings("unused")
public class DisplayNameLoader extends GsonLoader<DisplayName> {

	public static void main(String[] args) throws ClassNotFoundException {
		try {
			System.out.println("Started script.");
			Cache.init();
			GsonHandler.initialize();
			DisplayNameLoader loader = GsonHandler.getJsonLoader(DisplayNameLoader.class);
			removeInvalidDisplays(loader);
			//loadAllDisplays(loader);
			//deleteAccount("josh", loader);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void removeInvalidDisplays(DisplayNameLoader loader) {
		List<String> namesToFix = new ArrayList<>();
		List<DisplayName> displayNames = loader.generateList();
		ListIterator<DisplayName> it$ = displayNames.listIterator();
		while(it$.hasNext()) {
			DisplayName next = it$.next();
			List<String> names = next.getDisplayNames();
			ListIterator<String> it$2 = names.listIterator();
			while(it$2.hasNext()) {
				String name = it$2.next();
				if (Utils.invalidAccountName(name)) {
					it$2.remove();
					System.out.println(next.getLoginName() + " had invalid name: " + name);
					if (!namesToFix.contains(next.getLoginName())) {
						namesToFix.add(next.getLoginName());
					}
				}
			}
		}
		ListIterator<String> it2$ = namesToFix.listIterator();
		while(it2$.hasNext()) {
			String name = it2$.next();
			resetDisplayName(name);
		}
		loader.save(displayNames);
		System.out.println("Saved display names.");
	}
	
	private static void resetDisplayName(String name) {
		for (File acc : GameScript.getAccounts()) {
			if (acc.getName().replaceAll(".p", "").equalsIgnoreCase(name)) {
				try {
					Player player = (Player) Saving.loadSerializedFile(acc);
					player.setChangedDisplay(false);
					GameScript.savePlayer(player, acc, true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void loadAllDisplays(DisplayNameLoader loader) {
		for (File acc : GameScript.getAccounts()) {
			try {
				Player player = (Player) Saving.loadSerializedFile(acc);
				player.setUsername(acc.getName().replaceAll(".p", ""));
				loader.setDisplayName(player.getUsername(), player.getUsername());
				System.out.println("Saved " + player.getUsername());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void deleteAccount(String name, DisplayNameLoader loader) {
		for (File acc : GameScript.getAccounts()) {
			if (acc.getName().equalsIgnoreCase(name + ".p")) {
				boolean deleted = acc.delete();
				System.out.println("Deleted " + acc + "? " + deleted);
			}
		}
		loader.removeDisplay(name);
	}

	@Override
	public void initialize() {
		displayNames.clear();
		for (DisplayName name : generateList()) {
			displayNames.add(name);
		}
	}

	@Override
	public String getFileLocation() {
		return Constants.FILES_PATH + "/display_names.json";
	}

	@Override
	protected List<DisplayName> load() {
		List<DisplayName> autospawns = null;
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
		autospawns = gson.fromJson(json, new TypeToken<List<DisplayName>>() {
		}.getType());
		return autospawns;
	}

	/**
	 * Getting the display name of the player
	 * 
	 * @param loginName
	 *            The login name
	 * @return
	 */
	public String getDisplayName(String loginName) {
		synchronized (lock) {
			ListIterator<DisplayName> it$ = displayNames.listIterator();
			while (it$.hasNext()) {
				DisplayName next = it$.next();
				if (next.getLoginName().equalsIgnoreCase(loginName)) {
					List<String> names = next.getDisplayNames();
					return names.get(names.size() - 1);
				}
			}
			return null;
		}
	}

	/**
	 * Storing the display name and login name
	 * 
	 * @param loginName
	 *            The login name
	 * @param displayName
	 *            The display name
	 */
	public void setDisplayName(String loginName, String displayName) {
		synchronized (lock) {
			List<DisplayName> names = generateList();
			ListIterator<DisplayName> it$ = names.listIterator();
			boolean contained = false;
			while (it$.hasNext()) {
				DisplayName next = it$.next();
				if (next.getLoginName().equalsIgnoreCase(loginName)) {
					next.getDisplayNames().add(displayName);
					contained = true;
					break;
				}
			}
			if (!contained) {
				names.add(new DisplayName(loginName, displayName));
			}
			save(names);
			initialize();
		}
	}

	/**
	 * If this display name exists
	 * 
	 * @param displayName
	 *            The display name we're checking for
	 * @return
	 */
	public String displayNameExists(String displayName) {
		ListIterator<DisplayName> it$ = displayNames.listIterator();
		while (it$.hasNext()) {
			DisplayName next = it$.next();
			for (String name : next.getDisplayNames()) {
				name = name.replaceAll(" ", "_").trim();
				displayName = displayName.replaceAll(" ", "_").trim();
				if (name.equalsIgnoreCase(displayName)) {
					return next.getLoginName();
				}
			}
		}
		return null;
	}

	/**
	 * Removing the display name from the system
	 * 
	 * @param displayName
	 */
	public void removeDisplay(String displayName) {
		List<DisplayName> nameList = generateList();
		ListIterator<DisplayName> it$ = nameList.listIterator();
		while(it$.hasNext()) {
			DisplayName next = it$.next();
			List<String> names = next.getDisplayNames();
			ListIterator<String> it2 = names.listIterator();
			while(it2.hasNext()) {
				String name = it2.next();
				if (name.equalsIgnoreCase(displayName)) {
					it2.remove();
					System.out.println("Found " + displayName + " - original = " + next.getLoginName());
					break;
				}
			}
			if (names.size() == 0) {
				it$.remove();
				break;
			}
		}
		save(nameList);
	}

	/**
	 * The object names will be synchronized through
	 */
	private final Object lock = new Object();

	/**
	 * The list of display names that players are using
	 */
	private final List<DisplayName> displayNames = Collections.synchronizedList(new ArrayList<>());

}
