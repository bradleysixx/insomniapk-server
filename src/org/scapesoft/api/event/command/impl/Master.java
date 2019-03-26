package org.scapesoft.api.event.command.impl;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.game.player.Rights;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Mar 20, 2014
 */
public class Master extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.OWNER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "master" };
	}

	@Override
	public void execute(Player player) {
		for (int skill = 0; skill < 25; skill++)
			player.getSkills().addXp(skill, 14000000, true);
		player.getSkills().restoreSkills();
	}

}