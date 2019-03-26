package org.scapesoft.api.event.command.impl;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.content.PlayerLook;
import org.scapesoft.utilities.game.player.Rights;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Mar 20, 2014
 */
public class CustomiseCharacter extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.OWNER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "custc" };
	}

	@Override
	public void execute(Player player) {
		PlayerLook.openCharacterCustomizing(player);
	}

}
