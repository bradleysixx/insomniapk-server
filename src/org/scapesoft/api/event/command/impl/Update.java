package org.scapesoft.api.event.command.impl;

import org.scapesoft.Constants;
import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.api.input.IntegerInputAction;
import org.scapesoft.api.input.StringInputAction;
import org.scapesoft.game.World;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.game.player.Rights;
import org.scapesoft.utilities.misc.ChatColors;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Mar 24, 2014
 */
public class Update extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.ADMINISTRATOR;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "update" };
	}

	@Override
	public void execute(Player player) {
		player.getPackets().sendInputIntegerScript("Enter Time (seconds)", new IntegerInputAction() {

			@Override
			public void handle(int input) {
				final int time = input;
				World.safeShutdown(true, time);
				player.getPackets().sendInputLongTextScript("Enter Update Reason", new StringInputAction() {

					@Override
					public void handle(String input) {
						World.sendWorldMessage("<col=" + ChatColors.MAROON + ">" + Constants.SERVER_NAME + " has to update because</col>: " + input, false, true);
					}
					
				});
			}

		});
	}

}