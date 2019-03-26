package org.scapesoft.game.player.dialogues.impl;

import org.scapesoft.game.player.dialogues.Dialogue;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jun 19, 2014
 */
public class WorldMapConfirmation extends Dialogue {

	@Override
	public void start() {
		if (player.getTrade().isTrading()) {
			player.sendMessage("You cannot do this while trading.");
			return;
		}
		sendOptionsDialogue("Open world map?", "Yes", "No");
	}

	@Override
	public void run(int interfaceId, int option) {
		switch (option) {
		case FIRST:
			if (player.getTrade().isTrading()) {
				player.sendMessage("You cannot do this while trading.");
				return;
			}
			player.getPackets().sendWindowsPane(755, 0);
			int posHash = player.getX() << 14 | player.getY();
			player.getPackets().sendGlobalConfig(622, posHash); // map
			// open
			player.getPackets().sendGlobalConfig(674, posHash); // player-position
			break;
		case SECOND:
			break;
		}
		end();
	}

	@Override
	public void finish() {

	}

}