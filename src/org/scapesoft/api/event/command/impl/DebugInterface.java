package org.scapesoft.api.event.command.impl;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.game.player.Rights;
import org.scapesoft.utilities.misc.Utils;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Mar 19, 2014
 */
public class DebugInterface extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.OWNER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "dbi" };
	}

	@Override
	public void execute(Player player) {
		int interfaceId = Integer.parseInt(cmd[1]);
		int componentLength = Utils.getInterfaceDefinitionsComponentsSize(interfaceId);
		boolean sendInterface = true;
		if (cmd.length == 3) {
			sendInterface = Boolean.parseBoolean(cmd[2]);
		}
		for (int i = 0; i <= componentLength; i++) {
			player.getPackets().sendIComponentText(interfaceId, i, "" + i);
		}
		for (int i = 0; i <= 354; i++) {
			player.getPackets().sendGlobalString(i, "g" + i);
		}
		if (sendInterface)
			player.getInterfaceManager().sendInterface(interfaceId);
		
		System.out.println("Component length: " + componentLength);
	}

}
