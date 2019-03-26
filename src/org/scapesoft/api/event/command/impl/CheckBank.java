package org.scapesoft.api.event.command.impl;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.World;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.game.player.Rights;
import org.scapesoft.utilities.player.Saving;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Apr 3, 2014
 */
public class CheckBank extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.MODERATOR;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "checkbank" };
	}

	@Override
	public void execute(Player player) {
		String[] cmd = command.split(" ");
		String name = getCompleted(cmd, 1);
		Player target = World.getPlayerByDisplayName(name);
		if (target == null) {
			target = Saving.loadPlayer(name);
		}
		if (target == null) {
			player.sendMessage("No such player!");
			return;
		}
		player.getInterfaceManager().sendInterface(762);
		player.getInterfaceManager().sendInventoryInterface(763);
		player.getPackets().sendItems(95, target.getBank().getContainerCopy());
		player.getPackets().sendIComponentSettings(762, 95, 0, 516, 2622718);
		player.getPackets().sendIComponentSettings(763, 0, 0, 27, 2425982);
	}

}
