package org.scapesoft.game.player.dialogues.impl;

import org.scapesoft.game.player.content.construction.House.RoomReference;
import org.scapesoft.game.player.dialogues.Dialogue;
import org.scapesoft.utilities.misc.Utils;

public class RemoveRoomD extends Dialogue {

	private RoomReference room;

	@Override
	public void start() {
		this.room = (RoomReference) parameters[0];
		sendOptionsDialogue("Remove the " + Utils.formatPlayerNameForDisplay(room.getRoom().toString()) + "?", "Yes.", "No.");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (componentId == FIRST)
			player.getHouse().removeRoom(room);
		end();
	}

	@Override
	public void finish() {
	}

}
