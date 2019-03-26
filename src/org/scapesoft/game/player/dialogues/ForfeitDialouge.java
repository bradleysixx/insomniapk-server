package org.scapesoft.game.player.dialogues;

import org.scapesoft.game.player.controlers.impl.DuelArena;

public class ForfeitDialouge extends Dialogue {

	@Override
	public void start() {
		sendOptionsDialogue("Forfeit Duel?", "Yes.", "No.");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		switch (componentId) {
			case FIRST:
				if (!player.getLastDuelRules().getRule(7)) {
					((DuelArena) player.getControllerManager().getController()).endDuel(player.getLastDuelRules().getTarget(), player, false);
				} else {
					sendDialogue("You can't forfeit during this duel.");
				}
				break;
		}
		end();
	}

	@Override
	public void finish() {

	}

}
