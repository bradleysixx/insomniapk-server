package org.scapesoft.game.player.dialogues.impl;

import org.scapesoft.Constants;
import org.scapesoft.game.World;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.player.controlers.impl.StartTutorial;
import org.scapesoft.game.player.dialogues.ChatAnimation;
import org.scapesoft.game.player.dialogues.Dialogue;
import org.scapesoft.utilities.misc.Utils;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Mar 22, 2014
 */
public class Tutorial_Dialogue extends Dialogue {

	@Override
	public void start() {
		stage = (Integer) parameters[0];
		send(stage++);
	}

	/**
	 * @param currentStage
	 */
	private void send(int currentStage) {
		if (player.getControllerManager().getController() instanceof StartTutorial) {
			StartTutorial tut = (StartTutorial) player.getControllerManager().getController();
			tut.setStage(currentStage);
		}
		switch (currentStage) {
		case 0:
			sendNPCDialogueNoContinue(npcId, ChatAnimation.NORMAL, 5, "Hello adventurer " + player.getDisplayName() + ". Welcome to " + Constants.SERVER_NAME + ".", "It's my job to inform you about us in less than 30 seconds.", "So here we go...");
			player.setNextWorldTile(Constants.HOME_TILE);
			break;
		case 1:
			sendNPCDialogueNoContinue(npcId, ChatAnimation.NORMAL, 5, "To transport around the world, talk to the", "Wizard. He has a variety of teleports available", "just for you.");
			NPC cromperty = Utils.findLocalNPC(player, 2328);
			if (cromperty != null) {
				WorldTile teleTile = null;
				int[][] checkNearDirs = Utils.getCoordOffsetsNear(1);
				for (int dir = 0; dir < checkNearDirs[0].length; dir++) {
					final WorldTile tile = new WorldTile(new WorldTile(cromperty.getX() + checkNearDirs[0][dir], cromperty.getY() + checkNearDirs[1][dir], cromperty.getPlane()));
					if (World.isTileFree(tile.getPlane(), tile.getX(), tile.getY(), 1)) { // if
						teleTile = tile;
						break;
					}
				}
				if (teleTile != null) {
					player.setNextWorldTile(teleTile);
				}
				player.setNextFaceEntity(cromperty);
			}
			break;
		case 2:
			sendNPCDialogueNoContinue(npcId, ChatAnimation.NORMAL, 5, "Here we have the \"Event Portal\", which will teleport", "you into event locations, such as questing places", "and minigame locations.");
			player.setNextFaceEntity(null);
			player.setNextWorldTile(new WorldTile(3287, 3499, 0));
			player.setNextFaceWorldTile(new WorldTile(3288, 3499, 0));
			break;
		case 3:
			sendNPCDialogueNoContinue(npcId, ChatAnimation.NORMAL, 5, "Infront of you is the Exchange Clerk. This", "is where you can buy and sell all items to " + Constants.SERVER_NAME + " players.", "Try buying supplies after the tutorial.");
			player.setNextWorldTile(new WorldTile(3282, 3488, 0));
			player.setNextFaceWorldTile(new WorldTile(3283, 3488, 0));
			break;
		case 4:
			sendNPCDialogueNoContinue(npcId, ChatAnimation.NORMAL, 5, "We're at the book lectern. Here is where you can wish", "for a change in your prayer or magic book. Simply", "click the lectern and select an option.");
			player.setNextWorldTile(new WorldTile(3276, 3493, 0));
			player.setNextFaceWorldTile(new WorldTile(3275, 3493, 0));
			break;
		case 5:
			sendNPCDialogueNoContinue(npcId, ChatAnimation.NORMAL, 5, "Wilderness activity is very important in " + Constants.SERVER_NAME + ".", "Speak to Mandrith to find out how to get wilderness points", "and what they can be exchanged for.");
			player.setNextWorldTile(new WorldTile(3276, 3502, 0));
			player.setNextFaceWorldTile(new WorldTile(3275, 3502, 0));
			break;
		case 6:
			sendNPCDialogueNoContinue(npcId, ChatAnimation.NORMAL, 5, "Lastly is the Slayer Master, Kuradal.", "If you wish to start a slayer task", "it is best you talk to her first.");
			player.setNextWorldTile(new WorldTile(3284, 3505, 0));
			player.setNextFaceWorldTile(new WorldTile(3284, 3506, 0));
			break;
		case 7:
			sendNPCDialogueNoContinue(npcId, ChatAnimation.NORMAL, -1, "Please personalize your character's looks!", "Modify your appearance with the \"?\" symbol at the top right");
			break;
		default:
			sendDialogue("stage:" + currentStage);
			break;
		}
	}

	@Override
	public void run(int interfaceId, int option) {
		send(stage++);
	}

	@Override
	public void finish() {

	}

	int stage;
	private final int npcId = 2244;

}
