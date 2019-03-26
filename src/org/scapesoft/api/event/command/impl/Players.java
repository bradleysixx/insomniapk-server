package org.scapesoft.api.event.command.impl;

import java.util.ArrayList;
import java.util.List;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.api.event.listeners.interfaces.Scrollable;
import org.scapesoft.game.World;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.controlers.impl.Wilderness;
import org.scapesoft.utilities.game.player.Rights;
import org.scapesoft.utilities.misc.ChatColors;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Mar 31, 2014
 */
public class Players extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.PLAYER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "players" };
	}

	@Override
	public void execute(Player player) {
		List<String> messageList = new ArrayList<String>();
		for (Player p : World.getPlayers()) {
			messageList.add("(#" + p.getIndex() + ") <img=" + p.getChatIcon() + "><col=" + ChatColors.MAROON + ">" + p.getDisplayName() + "</col> - Lvl: " + p.getSkills().getCombatLevelWithSummoning() + " - In Wild: <col=" + ChatColors.RED + ">" + (p.getControllerManager().getController() instanceof Wilderness ? "Yes" : "No") + "</col>");
		}
		Scrollable.sendScroll(player, "Players Online: " + World.getPlayers().size(), messageList.toArray(new String[messageList.size()]));
	}

}