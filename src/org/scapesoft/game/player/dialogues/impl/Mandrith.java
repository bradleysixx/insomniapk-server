package org.scapesoft.game.player.dialogues.impl;

import org.scapesoft.game.player.dialogues.ChatAnimation;
import org.scapesoft.game.player.dialogues.Dialogue;
import org.scapesoft.utilities.console.gson.GsonHandler;
import org.scapesoft.utilities.console.gson.impl.ShopsLoader;
import org.scapesoft.utilities.misc.ChatColors;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jul 6, 2014
 */
public class Mandrith extends Dialogue {

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendOptionsDialogue("Select an Option", "How do I receive wilderness points?", "View Wilderness Rewards Shop", "Request Skull Marking", "Cancel");
	}

	@Override
	public void run(int interfaceId, int option) {
		switch (stage) {
		case -1:
			switch (option) {
			case FIRST:
				sendNPCDialogue(npcId, ChatAnimation.NORMAL, "Wilderness points are received for activity in the wilderness.", "Wilderness activities happen every 30 minutes or so", "watch for them and you can receive points. Killing", "players also gives a lot of good points.");
				stage = 0;
				break;
			case SECOND:
				GsonHandler.<ShopsLoader> getJsonLoader(ShopsLoader.class).openShop(player, "Wilderness Point Rewards");
				end();
				break;
			case THIRD:
				end();
				player.setWildernessSkull();
				break;
			case FOURTH:
				end();
				break;
			}
			break;
		case 0:
			sendNPCDialogue(npcId, ChatAnimation.NORMAL, "Such activites are pking, bosses, or skilling activities, be ready!", "<col=" + ChatColors.BLUE + ">Check my shop to view possible rewards!");
			stage = -2;
			break;
		}
	}

	@Override
	public void finish() {
	}

	int npcId;

}
