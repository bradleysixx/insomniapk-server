package org.scapesoft.api.event.command.impl;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.World;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.console.gson.GsonHandler;
import org.scapesoft.utilities.console.gson.extra.Punishment.PunishmentType;
import org.scapesoft.utilities.console.gson.impl.PunishmentLoader;
import org.scapesoft.utilities.game.player.Rights;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Mar 24, 2014
 */
public class UnmuteCommand extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.MODERATOR;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "unmute" };
	}

	@Override
	public void execute(final Player player) {
		final PunishmentLoader loader = GsonHandler.getJsonLoader(PunishmentLoader.class);
		String name = getCompleted(cmd, 1).replaceAll("_", " ");
		Player target = World.getPlayerByDisplayName(name);
		if (target != null) {
			loader.forceRemovePunishment(target.getUsername(), PunishmentType.MUTE);
			player.sendMessage("You have unpunished: " + target.getUsername());
		} else {
			player.sendMessage("No such player.");
		}
	}

}
