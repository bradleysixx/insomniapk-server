package org.scapesoft.api.event.command.impl;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.content.TicketSystem;
import org.scapesoft.utilities.game.player.Rights;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Nov 29, 2014
 */
public class Ticket extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.PLAYER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "ticket" };
	}

	@Override
	public void execute(Player player) {
		TicketSystem.requestTicket(player);		
	}

}
