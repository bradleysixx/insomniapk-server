package org.scapesoft.api.event.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.scapesoft.Constants;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.console.logging.FileLogger;
import org.scapesoft.utilities.misc.FileClassLoader;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Feb 28, 2014
 */
public class CommandHandler {

	/**
	 * Handles the command that has been typed
	 *
	 * @param player
	 *            The player typing the command
	 * @param cmd
	 *            The command typed
	 */
	public void handleCommand(Player player, String cmd) {
		try {
			CommandSkeleton command = commands.get(cmd.split(" ")[0].toLowerCase());
			if (command != null) {
				if (command.getRightsRequired().hasRight(player)) {
					command.setCommand(cmd).execute(player);
					FileLogger.getFileLogger().writeLog("cmd/", player.getDisplayName() + " used command:\t[" + cmd + "]", true);
				}
			}
		} catch (Exception e) {
			if (Constants.DEBUG)
				e.printStackTrace();
			player.sendMessage("Error parsing command! Retry with correct parameters if required.");
		}
	}

	/**
	 * Loads up all of the game commands to the map and prints out data about
	 * loading speed and size of map
	 */
	public void initialize() {
		commands.clear();
		for (Object packet : FileClassLoader.getClassesInDirectory(CommandSkeleton.class.getPackage().getName() + ".impl")) {
			try {
				CommandSkeleton skeleton = (CommandSkeleton) packet;
				if (skeleton.getCommandApplicable() != null) {
					for (String parameter : skeleton.getCommandApplicable())
						commands.put(parameter.toLowerCase(), skeleton);
				} else {
					throw new IllegalStateException("Could not register " + skeleton.getClass().getCanonicalName() + "; no parameters");
				}
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}

	public List<CommandSkeleton> getAvailableCommands(Player player) {
		List<CommandSkeleton> list = new ArrayList<CommandSkeleton>();
		Iterator<Entry<String, CommandSkeleton>> it = commands.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, CommandSkeleton> entry = it.next();
			if (entry.getValue().getRightsRequired().hasRight(player)) {
				list.add(entry.getValue());
			}
		}
		return list;
	}

	public static CommandHandler get() {
		return INSTANCE;
	}

	private final Map<String, CommandSkeleton> commands = new HashMap<String, CommandSkeleton>();
	private static final CommandHandler INSTANCE = new CommandHandler();

}
