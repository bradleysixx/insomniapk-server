package org.scapesoft.api.event.command.impl;

import java.util.concurrent.TimeUnit;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.api.input.IntegerInputAction;
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
public class MuteCommand extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.SUPPORT;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "mute" };
	}

	@Override
	public void execute(final Player player) {
		if (player.isSupporter() || player.getRights() > 0) {
			final PunishmentLoader loader = GsonHandler.getJsonLoader(PunishmentLoader.class);
			final String name = getCompleted(cmd, 1).replaceAll(" ", "_");
			player.getPackets().sendInputIntegerScript("Enter Duration (minutes)", new IntegerInputAction() {

				@Override
				public void handle(int input) {
					Player target = World.getPlayerByDisplayName(name);
					if (target != null) {
						loader.addPunishment(target.getUsername(), PunishmentType.MUTE, TimeUnit.MINUTES.toMillis(input));
						player.sendMessage("You have punished: " + target.getUsername());
					} else {
						player.sendMessage("No player found by name: " + name);
					}
				}
			});
		}
	}

}
