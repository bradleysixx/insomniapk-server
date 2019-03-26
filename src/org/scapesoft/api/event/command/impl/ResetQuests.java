package org.scapesoft.api.event.command.impl;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.game.player.Rights;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jun 29, 2014
 */
public class ResetQuests extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.OWNER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "resetq" };
	}

	@Override
	public void execute(Player player) {
		player.getQuestManager().getProgressed().clear();
		player.getQuestManager().getStages().clear();
		player.getFacade().setLastRFDWave(0);
	}

}
