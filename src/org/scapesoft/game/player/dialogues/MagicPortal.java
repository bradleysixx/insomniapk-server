package org.scapesoft.game.player.dialogues;

import org.scapesoft.game.WorldTile;

public class MagicPortal extends Dialogue {

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (getStage() == -1) {
			setStage(0);
		}
		if (getStage() == 0) {
			if (componentId == 2) {
				player.getPackets().sendGameMessage("You enter the portal...");
				player.useStairs(10584, new WorldTile(3233, 9315, 0), 2, 3, "..and are transported to an altar.");
				player.addWalkSteps(1599, 4515, -1, false);
				end();
			}
			if (componentId == 3) {
				player.getPackets().sendGameMessage("You enter the portal...");
				player.useStairs(10584, new WorldTile(2152, 3868, 0), 2, 3, "..and are transported to an altar.");
				player.addWalkSteps(1600, 4514, -1, false);
				end();
			} else {
				end();
			}
		}
	}

	@Override
	public void start() {
		sendDialogue(SEND_3_LARGE_OPTIONS, "Select an Option", "Ancient Magic Altar", "Lunar Magic Altar", "Never Mind");
	}

}
