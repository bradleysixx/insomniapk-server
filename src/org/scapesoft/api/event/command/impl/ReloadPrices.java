package org.scapesoft.api.event.command.impl;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.console.gson.GsonHandler;
import org.scapesoft.utilities.console.gson.impl.ExchangePriceLoader;
import org.scapesoft.utilities.game.player.Rights;

public final class ReloadPrices extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.OWNER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "reloadprices" };
	}

	@Override
	public void execute(Player player) {
		((ExchangePriceLoader) GsonHandler.getJsonLoader(ExchangePriceLoader.class)).initialize();
		player.sendMessage("Reloaded prices.");
	}

}
