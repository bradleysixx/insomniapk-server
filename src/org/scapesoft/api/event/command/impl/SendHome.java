package org.scapesoft.api.event.command.impl;

import java.io.File;

import org.scapesoft.Constants;
import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.World;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.game.player.Rights;
import org.scapesoft.utilities.player.Saving;

/**
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since Jul 12, 2013
 */
public class SendHome extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.MODERATOR;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "sendhome", "telehome", "unnull" };
	}

	@Override
	public void execute(Player player) {
		String[] cmd = command.split(" ");
		String name = getCompleted(cmd, 1).replaceAll("_", " ");
		Player target = World.getPlayerByDisplayName(name);
		if (target != null) {
			target.getControllerManager().forceStop();
			target.setNextWorldTile(Constants.HOME_TILE);
			target.sendMessage(player.getDisplayName() + " has teleported you home.");
		} else {
			name = name.replaceAll(" ", "_");
			name = name + ".p";
			try {
				File acc = new File(Saving.PATH + "" + name);
				target = (Player) Saving.loadSerializedFile(acc);
				if (target == null) {
					player.sendMessage("No such player: " + name);
					return;
				}
				if (acc.getName().equalsIgnoreCase(name)) {
					target.setUsername(name.replaceAll(".p", ""));
					target.getControllerManager().removeControllerWithoutCheck();
					target.setLocation(Constants.HOME_TILE);
					Saving.savePlayer(target);
					player.sendMessage("un nulled offline player: " + acc);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
