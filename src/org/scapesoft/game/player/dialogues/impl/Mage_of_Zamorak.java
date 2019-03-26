package org.scapesoft.game.player.dialogues.impl;

import org.scapesoft.game.player.content.Magic;
import org.scapesoft.game.player.dialogues.Dialogue;
import org.scapesoft.utilities.game.player.TeleportLocations;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jul 8, 2014
 */
public class Mage_of_Zamorak extends Dialogue {

	int npcId;

	@Override
	public void start() {
		npcId = (int) parameters[0];
		sendOptionsDialogue("Select an Option", "Teleport to the Abyss", "Cancel");
	}

	@Override
	public void run(int interfaceId, int option) {
		end();
		if (option == FIRST) {
			player.getPrayer().drainPrayer();
			Magic.sendPurpleTeleportSpell(player, TeleportLocations.ABYSS);
		}
	}

	@Override
	public void finish() {
	}

}
