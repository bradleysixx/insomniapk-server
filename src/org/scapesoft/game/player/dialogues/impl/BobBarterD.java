package org.scapesoft.game.player.dialogues.impl;

import org.scapesoft.game.item.Decanting;
import org.scapesoft.game.player.dialogues.ChatAnimation;
import org.scapesoft.game.player.dialogues.Dialogue;
import org.scapesoft.utilities.misc.Utils;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Nov 5, 2014
 */
public class BobBarterD extends Dialogue {

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendNPCDialogue(npcId, ChatAnimation.NORMAL, "Hello " + player.getDisplayName() + ", how can I help you?");
	}

	@Override
	public void run(int interfaceId, int option) {
		switch(stage) {
		case -1:
			sendOptionsDialogue("What would you like to say?", "Decant my potions...", "Nevermind.");
			stage = 0;
			break;
		case 0:
			switch(option) {
			case FIRST:		
				sendNPCDialogue(npcId, ChatAnimation.NORMAL, "Great, it will cost " + Utils.format(Decanting.COST_PER_POTION) + " gp per potion!");
				stage = 1;
				break;
			case SECOND:
				end();
				break;
			}
			break;
		case 1:
			sendOptionsDialogue("Pay " + Utils.format(Decanting.getPotionCost(player)) + " coins?", "Yes", "No");
			stage = 2;
			break;
		case 2:
			switch(option) {
			case FIRST:
				if (player.takeMoney(Decanting.getPotionCost(player))) {
					Decanting.decantInventory(player, 4);
				} else {
					sendPlayerDialogue(ChatAnimation.SAD, "I don't have that much money actually...");
					stage = -2;
				}
				break;
			case SECOND:
				end();
				break;
			}
			break;
		}
	}

	@Override
	public void finish() {
		
	}
	
	int npcId;

}
