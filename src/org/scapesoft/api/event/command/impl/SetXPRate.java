package org.scapesoft.api.event.command.impl;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.game.player.Rights;
import org.scapesoft.utilities.misc.Utils.CombatRates;

public final class SetXPRate extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.OWNER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "setxprate" };
	}

	@Override
	public void execute(Player player) {
		CombatRates rate = CombatRates.valueOf(cmd[1]);
		player.getAttributes().put("experience_rate_switch",
				rate);
		player.getFacade().setModifiers(
				(CombatRates) player.getAttributes().get(
						"experience_rate_switch"));
		player.sendMessage("Successfully set XP rate to " + rate.name());
	}

}