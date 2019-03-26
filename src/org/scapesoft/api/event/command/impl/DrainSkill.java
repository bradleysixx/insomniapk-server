package org.scapesoft.api.event.command.impl;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.game.player.Rights;

public class DrainSkill extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.OWNER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "ds" };
	}

	@Override
	public void execute(Player player) {
		int skill = Integer.parseInt(cmd[1]);
		player.getSkills().drainLevel(skill, player.getSkills().getLevel(skill));
	}

}
