package org.scapesoft.api.event.command.impl;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.game.player.Rights;
import org.scapesoft.utilities.misc.ChatColors;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Apr 5, 2014
 */
public class HidePlayer extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.SUPPORT;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "hide" };
	}

	@Override
	public void execute(Player player) {
		if (player.isSupporter() || player.getRights() > 0) {
			player.getAppearence().switchHidden();
			player.getDialogueManager().startDialogue("SimpleMessage", "<col=" + ChatColors.MAROON + ">You are now " + (player.getAppearence().isHidden() ? "hidden" : "visible") + ".");
		}
	}

}
