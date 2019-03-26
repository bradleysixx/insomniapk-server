package org.scapesoft.api.event.command.impl;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.content.Magic;
import org.scapesoft.utilities.game.player.Rights;

public final class Island extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.MODERATOR;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "island" };
	}

	@Override
	public void execute(Player player) {
		Magic.sendNormalTeleportSpell(player, 1, 0, new WorldTile(2859, 2578, 0));
		player.sendMessage("An island for the cool kids; here you land.");
	}

}