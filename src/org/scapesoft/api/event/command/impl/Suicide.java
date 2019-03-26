package org.scapesoft.api.event.command.impl;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.Hit;
import org.scapesoft.game.Hit.HitLook;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.game.player.Rights;

public class Suicide extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.OWNER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "die", "suicide" };
	}

	@Override
	public void execute(Player player) {
		player.removeHitpoints(new Hit(player, Short.MAX_VALUE, HitLook.MELEE_DAMAGE));
	}

}
