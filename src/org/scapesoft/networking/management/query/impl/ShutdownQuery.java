package org.scapesoft.networking.management.query.impl;

import org.scapesoft.game.World;
import org.scapesoft.game.player.Player;
import org.scapesoft.networking.management.query.ServerQuery;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Nov 21, 2014
 */
public class ShutdownQuery extends ServerQuery {

	@Override
	public String[] getQueryListeners() {
		return new String[] { "shutdown" };
	}

	@Override
	public String getDescription() {
		return "Shuts down the game server";
	}

	@Override
	public String onRequest() {
		for (Player player : World.getPlayers()) {
			if (player == null)
				continue;
			player.realFinish();
		}
		System.exit(-1);
		return "Kicked all players: " + World.getPlayers();
	}

}
