package org.scapesoft.api.event.command.impl;

import org.scapesoft.Constants;
import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.content.Magic;
import org.scapesoft.utilities.game.player.Rights;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Apr 18, 2014
 */
public class Home extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.PLAYER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "home" };
	}

	@Override
	public void execute(Player player) {
		Magic.sendNormalTeleportSpell(player, 1, 0, Constants.HOME_TILE);
	}

}
