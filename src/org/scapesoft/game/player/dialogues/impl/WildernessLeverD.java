package org.scapesoft.game.player.dialogues.impl;

import org.scapesoft.game.player.dialogues.Dialogue;
import org.scapesoft.utilities.game.player.TeleportLocations;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jun 30, 2014
 */
public class WildernessLeverD extends Dialogue {

	@Override
	public void start() {
		sendOptionsDialogue("Select an Option", "Revenants", "East Dragons", "West Dragons", "Deserted Keep");
	}

	@Override
	public void run(int interfaceId, int option) {
		switch (option) {
		case FIRST:
			player.getDialogueManager().startDialogue(TeleportConfirmation.class, TeleportLocations.REVENANTS_CAVE);
			break;
		case SECOND:
			player.getDialogueManager().startDialogue(TeleportConfirmation.class, TeleportLocations.EASTS);
			break;
		case THIRD:
			player.getDialogueManager().startDialogue(TeleportConfirmation.class, TeleportLocations.WESTS);
			break;
		case FOURTH:
			player.getDialogueManager().startDialogue(TeleportConfirmation.class, TeleportLocations.DESERTED_KEEP);
			break;
		}
	}

	@Override
	public void finish() {

	}

}
