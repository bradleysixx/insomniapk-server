package org.scapesoft.networking.management.query.impl;

import org.scapesoft.game.World;
import org.scapesoft.game.player.Player;
import org.scapesoft.networking.management.query.ServerQuery;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Nov 21, 2014
 */
public class KickQuery extends ServerQuery {

	@Override
	public String[] getQueryListeners() {
		return new String[] { "kick" };
	}
	
	@Override
	public String getDescription() {
		return "Kicks a player from the server";
	}

	@Override
	public String onRequest() {
		String[] split = getQuery().split(" ");
		String name = "";
		for (int i = 0; i < split.length; i++) {
			if (i == 0)
				continue;
			for (char c : split[i].toCharArray()) {
				name += c;
			}
		}
		Player target = World.getPlayer(name);
		if (target == null) {
			return "No such player " + name + "..";
		} else {
			target.forceLogout();
			return "Successfully booted " + name + "";
		}
	}

}
