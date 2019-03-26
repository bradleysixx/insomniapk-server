package org.scapesoft.api.event.command.impl;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.console.gson.GsonHandler;
import org.scapesoft.utilities.console.gson.extra.Punishment.PunishmentType;
import org.scapesoft.utilities.console.gson.impl.PunishmentLoader;
import org.scapesoft.utilities.game.player.Rights;
import org.scapesoft.utilities.player.Saving;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Mar 24, 2014
 */
public class UnipbanCommand extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.ADMINISTRATOR;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "unipban" };
	}

	@Override
	public void execute(Player player) {
		final PunishmentLoader loader = GsonHandler.getJsonLoader(PunishmentLoader.class);
		final String name = getCompleted(cmd, 1).replaceAll(" ", "_");
		Player target = Saving.loadPlayer(name);
		if (target == null) {
			player.sendMessage("No such player: " + name);
			return;
		}
		if (!loader.forceRemovePunishment(target.getLastIP(), PunishmentType.IPBAN)) {
			player.sendMessage("No such ipban was found!");
		} else {
			player.sendMessage("You have unpunished: " + target.getUsername());
		}
	}

}
