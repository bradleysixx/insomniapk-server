package org.scapesoft.api.event.command.impl;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.game.player.Rights;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Dec 27, 2014
 */
public class OpenPurchasePage extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.PLAYER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "member", "gold" };
	}

	@Override
	public void execute(Player player) {
		player.getPackets().sendOpenURL("http://scapesoft.org/community/index.php?/page/Membership/membership_information.html");
	}

}
