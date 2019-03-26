package org.scapesoft.game.player.dialogues.impl;

import org.scapesoft.Constants;
import org.scapesoft.game.minigames.GoblinWars;
import org.scapesoft.game.player.content.Magic;
import org.scapesoft.game.player.dialogues.ChatAnimation;
import org.scapesoft.game.player.dialogues.Dialogue;

public final class Ozan extends Dialogue {
	
	private int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendNPCDialogue(npcId, ChatAnimation.NORMAL, "Goblin Wars are among " + Constants.SERVER_NAME + "!", "Would you like to visit the lobby?");
		stage = 0;
	}

	@Override
	public void run(int interfaceId, int option) {
		switch (stage) {
		case -1:
			end();
			break;
		case 0:
			sendOptionsDialogue("Visit the lobby?", "Yes", "No");
			stage = 1;
			break;
		case 1:
			switch (option) {
			case FIRST:
				sendPlayerDialogue(ChatAnimation.NORMAL, "Bring it.");
				stage = 2;
				break;
			case SECOND:
				sendPlayerDialogue(ChatAnimation.SNOBBY, "No! I don't need the trouble.");
				stage = -1;
				break;
			}
			break;
		case 2:
			Magic.sendGreenTeleportSpell(player, GoblinWars.LOBBY);
			end();
			break;
		}
	}
	
	
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		
	}

}