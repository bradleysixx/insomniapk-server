package org.scapesoft.api.event.command.impl;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.Skills;
import org.scapesoft.utilities.game.player.Rights;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Mar 21, 2014
 */
public class SetLevel extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.OWNER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "setlevel" };
	}

	@Override
	public void execute(Player player) {
		if (cmd.length < 3) {
			player.getPackets().sendGameMessage("Usage ::setlevel skillId level");
			return;
		}
		try {
			int skill = Integer.parseInt(cmd[1]);
			int level = Integer.parseInt(cmd[2]);
			if (level < 0 || (level > 99 && skill != 24) || level > 120) {
				player.getPackets().sendGameMessage("Please choose a valid level.");
				return;
			}
			player.getSkills().set(skill, level);
			player.getSkills().setXp(skill, Skills.getXPForLevel(level));
			player.getAppearence().generateAppearenceData();
			return;
		} catch (NumberFormatException e) {
			player.getPackets().sendGameMessage("Usage ::setlevel skillId level");
		}
	}

}
