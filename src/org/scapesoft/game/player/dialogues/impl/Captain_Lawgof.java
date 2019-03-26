package org.scapesoft.game.player.dialogues.impl;

import org.scapesoft.game.player.dialogues.ChatAnimation;
import org.scapesoft.game.player.dialogues.Dialogue;
import org.scapesoft.game.player.quests.impl.Helpless_Lawgof;
import org.scapesoft.utilities.misc.Utils;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Apr 4, 2014
 */
public class Captain_Lawgof extends Dialogue {

	private int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendNPCDialogue(npcId, ChatAnimation.NORMAL, "Hello, adventurer. I need some help gathering", "some materials. Could you assist me?");
	}

	@Override
	public void run(int interfaceId, int option) {
		switch(stage) {
		case -1:
			sendOptionsDialogue("Start Helpless Lawgof?", "Yes.", "No.");
			stage = 0;
			break;
		case 0:
			end();
			if (option == FIRST) {
				player.getQuestManager().startQuest(Helpless_Lawgof.class);
				player.getQuestManager().handleNPC(player, Utils.findLocalNPC(player, 208));
			}
			break;
		}
	}

	@Override
	public void finish() {
	}
}
