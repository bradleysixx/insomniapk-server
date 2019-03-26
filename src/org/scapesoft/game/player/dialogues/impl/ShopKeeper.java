package org.scapesoft.game.player.dialogues.impl;

import org.scapesoft.Constants;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.player.dialogues.ChatAnimation;
import org.scapesoft.game.player.dialogues.Dialogue;
import org.scapesoft.utilities.console.gson.GsonHandler;
import org.scapesoft.utilities.console.gson.impl.ShopsLoader;
import org.scapesoft.utilities.misc.ChatColors;
import org.scapesoft.utilities.misc.Utils;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jun 21, 2014
 */
public class ShopKeeper extends Dialogue {

	int npcId;
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendNPCDialogue(npcId, ChatAnimation.LISTENING, "Hello, how can I help you?");
	}

	@Override
	public void run(int interfaceId, int option) {
		switch(stage) {
		case -1:
			sendOptionsDialogue("Select a Option", "View General Store", "Where are the shops?");
			stage = 0;
			break;
		case 0:
			switch(option) {
			case FIRST:
				((ShopsLoader) GsonHandler.getJsonLoader(ShopsLoader.class)).openShop(player, "General Store");
				end();
				break;
			case SECOND:
				sendNPCDialogue(npcId, ChatAnimation.NORMAL, Constants.SERVER_NAME + " has no regular rsps-like shops, instead", "you buy items straight from the grand exchange.", "Try it now!");
				NPC clerk = Utils.findLocalNPC(player, 2241);
				if (clerk != null) {
					player.getHintIconsManager().addHintIcon(clerk, 0, -1, false);
					player.sendMessage("<col=" + ChatColors.RED + ">Follow the marker on your minimap to the grand exchange!</col>");
					player.setCloseInterfacesEvent(new Runnable() {

						@Override
						public void run() {
							player.getHintIconsManager().removeUnsavedHintIcon();
						}
					
					});
				}
				stage = -2;
				break;
			}
			break;
		}
	}

	@Override
	public void finish() {
		
	}

}
