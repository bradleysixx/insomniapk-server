package org.scapesoft.game.player.dialogues.impl;

import org.scapesoft.game.WorldObject;
import org.scapesoft.game.player.dialogues.Dialogue;

public class RemoveBuildD extends Dialogue {

	WorldObject object;

	@Override
	public void start() {
		this.object = (WorldObject) parameters[0];
		sendOptionsDialogue("Really remove it?", "Yes.", "No.");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (componentId == FIRST) {
			player.getHouse().removeBuild(object);
		}
		end();
	}

	@Override
	public void finish() {

	}

}
