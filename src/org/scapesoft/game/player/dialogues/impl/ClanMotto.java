package org.scapesoft.game.player.dialogues.impl;

import org.scapesoft.game.player.dialogues.Dialogue;

public class ClanMotto extends Dialogue {

	@Override
	public void start() {
		player.getInterfaceManager().sendChatBoxInterface(1103);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		end();
	}

	@Override
	public void finish() {

	}

}
