package org.scapesoft.api.event.command.impl;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.game.player.Rights;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Nov 29, 2014
 */
public class ChangeYellTag extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.PLAYER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "changetag" };
	}

	@Override
	public void execute(Player player) {
		String tag = getCompleted(cmd, 1);
		if (tag.length() > 15) {
			player.sendMessage("Your tag is too long! It must be under 15 characters.");
			return;
		}
		if (tag.contains(">")) {
			player.sendMessage("Your tag contained an invalid character");
			return;
		}
		tag = tag.trim();
		player.getFacade().setYellTag(tag);
		player.sendMessage("You have just changed your yell tag to: " + tag);
	}

}
