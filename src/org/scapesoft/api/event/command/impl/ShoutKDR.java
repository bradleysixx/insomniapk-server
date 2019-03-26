package org.scapesoft.api.event.command.impl;

import java.text.MessageFormat;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.ForceTalk;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.game.player.Rights;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Dec 30, 2014
 */
public class ShoutKDR extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.PLAYER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "kdr" };
	}

	@Override
	public void execute(Player player) {
		player.setNextForceTalk(new ForceTalk(MessageFormat.format("KDR INFO: [kills={0}, deaths={1}, ratio:{2}]", player.getKillCount(), player.getDeathCount(), (double) ((double) player.getKillCount() / (double) player.getDeathCount()))));
	}

}
