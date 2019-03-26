package org.scapesoft.api.event.command.impl;

import java.io.File;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.engine.process.impl.LogUploaderProcessor;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.game.player.Rights;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Nov 1, 2014
 */
public class RequestLogsUpdate extends CommandSkeleton{

	@Override
	public Rights getRightsRequired() {
		return Rights.ADMINISTRATOR;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "requestlogs" };
	}

	@Override
	public void execute(Player player) {
		File file = LogUploaderProcessor.DURATION_FILE;
		boolean deleted = file.delete();
		if (!deleted) {
			player.sendMessage("Error requesting logs update - please wait and try again...");
		} else {
			player.sendMessage("Successfully requested logs update - they will be updated in 1-5 minutes!");
		}
	}

}
