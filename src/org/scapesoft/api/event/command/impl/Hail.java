package org.scapesoft.api.event.command.impl;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.Animation;
import org.scapesoft.game.ForceTalk;
import org.scapesoft.game.World;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.game.player.Rights;

public final class Hail extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.OWNER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "hail" };
	}

	@Override
	public void execute(final Player player) {
		try {
			player.getRegion()
					.getPlayerIndexes()
					.forEach(
							i -> {
								Player p = World.getPlayerByIndex(i);
								if (player.getIndex() != p.getIndex()) {
									p.turnTo(player);
									p.setNextAnimation(new Animation(1651));
									p.setNextForceTalk(new ForceTalk(
											"All hail "
													+ player.getDisplayName()
													+ "!"));
								}
							});
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

}