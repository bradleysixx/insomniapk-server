package org.scapesoft.api.event.command.impl;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.console.gson.GsonHandler;
import org.scapesoft.utilities.console.gson.impl.ShopsLoader;
import org.scapesoft.utilities.game.player.Rights;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jul 2, 2014
 */
public class OpenShop extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.OWNER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "openshop" };
	}

	@Override
	public void execute(Player player) {
		((ShopsLoader) GsonHandler.getJsonLoader(ShopsLoader.class)).openShop(player, getCompleted(cmd, 1));
	}

}
