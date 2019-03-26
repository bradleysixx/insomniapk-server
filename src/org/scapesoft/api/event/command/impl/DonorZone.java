package org.scapesoft.api.event.command.impl;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.content.DonatorZone;
import org.scapesoft.utilities.game.player.Rights;

public class DonorZone extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.PLAYER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "dz" };
	}

	@Override
	public void execute(Player player) {
		if (player.isDonator() || player.getRights() == 3) {
			DonatorZone.enterDonatorzone(player);
		}
	}

}
