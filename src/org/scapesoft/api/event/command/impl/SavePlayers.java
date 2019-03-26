package org.scapesoft.api.event.command.impl;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.engine.CoresManager;
import org.scapesoft.game.World;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.game.player.Rights;
import org.scapesoft.utilities.player.Saving;

public final class SavePlayers extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.OWNER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "saveplayers", "saveall" };
	}

	@Override
	public void execute(Player player) {
		World.players().forEach(p -> CoresManager.slowExecutor.submit(() -> Saving.savePlayer(p)));
		player.sendMessage("Successfully saved all players.");
	}

}