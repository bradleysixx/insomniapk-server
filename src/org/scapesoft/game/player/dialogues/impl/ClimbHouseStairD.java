package org.scapesoft.game.player.dialogues.impl;

import org.scapesoft.game.WorldObject;
import org.scapesoft.game.player.dialogues.Dialogue;

public class ClimbHouseStairD extends Dialogue {

	private WorldObject object;

	@Override
	public void start() {
		this.object = (WorldObject) parameters[0];
		sendOptionsDialogue(DEFAULT_OPTIONS_TI, "Climb up.", "Climb down.", "Cancel");

	}

	@Override
	public void run(int interfaceId, int componentId) {
		end();
		if (componentId != OPTION_3)
			player.getHouse().climbStaircase(object, componentId == OPTION_1);

	}

	@Override
	public void finish() {

	}

}