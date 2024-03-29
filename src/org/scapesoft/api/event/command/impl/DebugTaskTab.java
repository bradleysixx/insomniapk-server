package org.scapesoft.api.event.command.impl;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.game.player.Rights;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Mar 20, 2014
 */
public class DebugTaskTab extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.OWNER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "dtt" };
	}

	@Override
	public void execute(Player player) {
		player.stopAll();

		int interfaceId = 156;
		/*int componentLength = Utils.getInterfaceDefinitionsComponentsSize(interfaceId);
		for (int i = 0; i <= componentLength; i++) {
			player.getPackets().sendIComponentText(interfaceId, i, "" + i);
		}*/
			player.getPackets().sendGlobalString(211, "AYY LMAO");
		player.getInterfaceManager().sendInterface(interfaceId);
	}

}
