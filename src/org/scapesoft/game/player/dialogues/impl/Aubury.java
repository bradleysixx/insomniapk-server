package org.scapesoft.game.player.dialogues.impl;

import org.scapesoft.Constants;
import org.scapesoft.game.player.content.Magic;
import org.scapesoft.game.player.dialogues.ChatAnimation;
import org.scapesoft.game.player.dialogues.Dialogue;
import org.scapesoft.utilities.game.player.TeleportLocations;

/**
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since Feb 16, 2013
 */
public class Aubury extends Dialogue {

	int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendNPCDialogue(npcId, ChatAnimation.NORMAL, "Greetings, adventurer of " + Constants.SERVER_NAME + ", I am Aubury. ", "Would you like to teleport to mine pure essence?");
	}

	@Override
	public void run(int interfaceId, int option) {
		switch (getStage()) {
			case -1:
				sendOptionsDialogue("Select an Option", "Yes, please.", "No-thank you.");
				setStage(0);
				break;
			case 0:
				switch (option) {
					case FIRST:
						Magic.sendNormalTeleportSpell(player, 1, 0, TeleportLocations.ESSENCE_MINE);
						end();
						break;
					default:
						sendPlayerDialogue(ChatAnimation.NORMAL, "No thanks.");
						setStage(-2);
						break;
				}
				break;
			default:
				end();
				break;
		}
	}

	@Override
	public void finish() {

	}

}
