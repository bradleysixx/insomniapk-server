package org.scapesoft.api.event.command.impl;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.World;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.game.player.Rights;
import org.scapesoft.utilities.player.Saving;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Mar 29, 2014
 */
public class FinishQuestsOther extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.OWNER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "fqo" };
	}

	@Override
	public void execute(Player player) {
		String[] cmd = command.split(" ");
		String name = getCompleted(cmd, 1);
		Player target = World.getPlayerByDisplayName(name);
		if (target == null) {
			target = Saving.loadPlayer(name);
		}
		if (target == null) {
			player.sendMessage("No such player!");
			return;
		}
		player.sendMessage(target.getFacade().getLastRFDWave());
		target.getFacade().setLastRFDWave(4);
	}

}
