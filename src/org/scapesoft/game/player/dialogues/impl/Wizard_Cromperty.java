package org.scapesoft.game.player.dialogues.impl;

import org.scapesoft.api.event.listeners.interfaces.TeleportationSelectListener;
import org.scapesoft.game.player.dialogues.Dialogue;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Dec 30, 2014
 */
public class Wizard_Cromperty extends Dialogue {

	@Override
	public void start() {
		TeleportationSelectListener.display(player);
	}

	@Override
	public void run(int interfaceId, int option) {
		end();
	}

	@Override
	public void finish() {
		
	}

}
