package org.scapesoft.api.event.command.impl;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.game.player.Rights;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jun 28, 2014
 */
public class SwitchBooks extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.OWNER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "switchbook" };
	}

	@Override
	public void execute(Player player) {
		String completed = getCompleted(cmd, 1);
		try {
			boolean prayer = completed.contains("-pray");
			if (prayer) {
				player.getPrayer().setPrayerBook(!player.getPrayer().isAncientCurses());
			} else {
				int book = Integer.parseInt(completed.split(" ")[1]);
				player.getCombatDefinitions().setSpellBook(book);
			}
		} catch (Exception e) {
			player.sendMessage("Use as: switchbook [-pray/-book] [0-2]");
		}
	}
}
