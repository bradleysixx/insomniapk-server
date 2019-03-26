package org.scapesoft.game.player.dialogues.impl;

import org.scapesoft.Constants;
import org.scapesoft.game.player.content.Magic;
import org.scapesoft.game.player.content.construction.HouseConstants.POHLocation;
import org.scapesoft.game.player.dialogues.Dialogue;

public class HomePortal extends Dialogue {

	@Override
	public void start() {
		sendOptionsDialogue("Select an Option", "Go to a city of " + Constants.SERVER_NAME + ".", "Go back home.", "Nevermind.");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (componentId == FIRST) {
			end();
			Magic.sendNormalTeleportSpell(player, 0, 0, POHLocation.HOME_PORTAL.getTile());
		} else if (componentId == SECOND) {
			end();
			Magic.sendNormalTeleportSpell(player, 0, 0, Constants.HOME_TILE);
		} else {
			end();
		}

	}

	@Override
	public void finish() {
	}

}
