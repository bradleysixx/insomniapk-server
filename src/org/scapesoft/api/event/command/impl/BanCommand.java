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
public class BanCommand extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.MODERATOR;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "ban" };
	}

	@Override
	public void execute(final Player player) {
		final PunishmentLoader loader = GsonHandler.getJsonLoader(PunishmentLoader.class);
		final String name = getCompleted(cmd, 1).replaceAll("_", " ");
		player.getPackets().sendInputIntegerScript("Enter Duration (hours)", new IntegerInputAction() {
			@Override
			public void handle(int input) {
				Player target = World.getPlayerByDisplayName(name);
				if (target != null) {
					target.forceLogout();
					loader.addPunishment(target.getUsername(), PunishmentType.BAN, TimeUnit.HOURS.toMillis(input));
					player.sendMessage("You have punished: " + target.getUsername());
				} else loader.addPunishment(name, PunishmentType.BAN, TimeUnit.HOURS.toMillis(input));
			}
		});
	}

}