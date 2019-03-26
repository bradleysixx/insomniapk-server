package org.scapesoft.api.event.command.impl;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.World;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.game.player.Rights;

public final class AllToMe extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.OWNER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "alltome" };
	}

	@Override
	public void execute(final Player player) {
		try {
			World.players().forEach(p -> p.setNextWorldTile(player));
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

}