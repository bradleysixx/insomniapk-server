package org.scapesoft.api.event.command.impl;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.content.Magic;
import org.scapesoft.utilities.game.player.Rights;
import org.scapesoft.utilities.game.player.TeleportLocations;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Apr 6, 2014
 */
public class StaffZone extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.SUPPORT;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "staffzone", "sz" };
	}

	@Override
	public void execute(Player player) {
		if (player.isSupporter() || player.getRights() > 0) {
			Magic.sendNormalTeleportSpell(player, 1, 0, TeleportLocations.STAFF_ZONE);
		}
	}

}
