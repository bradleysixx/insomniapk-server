package org.scapesoft.api.event.command.impl;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.World;
import org.scapesoft.game.item.FloorItem;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.game.player.Rights;

public class ClearGroundItems extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.OWNER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[]{"cleargrounditems"};
	}

	@Override
	public void execute(Player player) {
		for (FloorItem item : player.getRegion().forceGetFloorItems()) {
			World.removeGroundItem(null, item);
		}
	}

}
