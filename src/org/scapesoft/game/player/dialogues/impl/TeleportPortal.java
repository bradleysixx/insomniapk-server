package org.scapesoft.game.player.dialogues.impl;

import org.scapesoft.game.player.content.Magic;
import org.scapesoft.game.player.dialogues.ChatAnimation;
import org.scapesoft.game.player.dialogues.Dialogue;
import org.scapesoft.utilities.game.player.TeleportLocations;
import org.scapesoft.utilities.misc.ChatColors;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jun 15, 2014
 */
public class TeleportPortal extends Dialogue {

	@Override
	public void start() {
		sendOptionsDialogue("Select a Teleport", "Questing Dome", "Duel Arena", "Gamers Grotto");
	}

	@Override
	public void run(int interfaceId, int option) {
		switch (option) {
		case FIRST:
			Magic.sendPurpleTeleportSpell(player, TeleportLocations.QUESTING_DOME);
			break;
		case SECOND:
			Magic.sendPurpleTeleportSpell(player, TeleportLocations.DUEL_ARENA);
			break;
		case THIRD:
			Magic.sendPurpleTeleportSpell(player, TeleportLocations.GAMERS_GROTTO);
			end();
			player.setCloseInterfacesEvent(new Runnable() {

				@Override
				public void run() {
					player.lock(8);
					player.getDialogueManager().startDialogue(new Dialogue() {

						@Override
						public void start() {
							sendNPCDialogueNoContinue(2244, ChatAnimation.NORMAL, 8, "North from here is the <col=" + ChatColors.BLUE + ">Dicing Area</col>, in which you", "can dice duel players and guarantee rewards. East is ", "<col=" + ChatColors.BLUE + ">Clan Wars</col>, in which you can fight other people.");
						}

						@Override
						public void run(int interfaceId, int option) {

						}

						@Override
						public void finish() {

						}
					});
				}
			});
			break;
		}
	}

	@Override
	public void finish() {

	}

}
