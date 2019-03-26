package org.scapesoft.networking.management.query.impl;

import org.scapesoft.game.World;
import org.scapesoft.game.player.Player;
import org.scapesoft.networking.management.query.ServerQuery;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Nov 21, 2014
 */
public class PlayersQuery extends ServerQuery {

	@Override
	public String[] getQueryListeners() {
		return new String[] { "listplayers", "players" };
	}

	@Override
	public String getDescription() {
		return "Shows all players currently online.";
	}

	@Override
	public String onRequest() {
		String players = new String();
		for (Player pl : World.getPlayers()) {
			players += pl.getDisplayName() + ", ";
		}
		return players + "\n\t[size=" + World.getPlayers().size() + "]";
	}

}
