package org.scapesoft.api.event.command.impl;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.game.player.Rights;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Apr 10, 2014
 */
public class InterfaceTest extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.OWNER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "itft" };
	}

	@Override
	public void execute(Player player) {
		player.getInterfaceManager().sendInterface(1055);
		player.getPackets().sendHideIComponent(1055, 0, false);
		player.getPackets().sendIComponentModel(1055, 13, 9533);//you can get the spriteId from the general requirement maps
		player.getPackets().sendRunScript(3970);
		player.getPackets().sendRunScript(3969);
		player.getPackets().sendRunScript(3968);
		for (int i = 0; i < 13; i++) {
			player.getPackets().sendIComponentText(1055, i, "Text, get this from General Requirement maps");
		}
	}

}
