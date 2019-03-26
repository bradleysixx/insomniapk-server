package org.scapesoft.api.event.command.impl;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.World;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.game.player.Rights;
import org.scapesoft.utilities.misc.Utils.CombatRates;

/**
 * @author Tyluur<itstyluur@gmail.com
 * @since Oct 26, 2014
 */
public class SetOtherRates extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.OWNER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "setotherrates" };
	}

	@Override
	public void execute(Player player) {
		final String name = cmd[1];
		Player o = World.getPlayer(name);
		if (o == null) {
			return;
		}
		CombatRates newRate = CombatRates.valueOf(cmd[2]);
		o.getFacade().setModifiers(newRate);
		o.sendMessage("Your experience rates are now " +newRate);
		player.sendMessage("set " + o.getDisplayName() + " to " + newRate);
	}
}
