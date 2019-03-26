package org.scapesoft.api.event.command.impl;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.item.Item;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.game.player.Rights;

/**
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since Aug 6, 2013
 */
public class Test extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.OWNER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[]{"test"};
	}

	@Override
	public void execute(final Player player) {
		for (int i = 0; i < 600; i++) {
			player.getBank().addItem(new Item(i, 0), false);
		}
	}

}
