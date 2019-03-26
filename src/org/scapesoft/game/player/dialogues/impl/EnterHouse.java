package org.scapesoft.game.player.dialogues.impl;

import org.scapesoft.api.input.StringInputAction;
import org.scapesoft.game.player.content.construction.House;
import org.scapesoft.game.player.dialogues.Dialogue;

/**
 * 
 * @author Jonathan
 * @since January 22th, 2014
 */
public class EnterHouse extends Dialogue {

	@Override
	public void start() {
		sendOptionsDialogue(DEFAULT_OPTIONS_TI, "Go to your house.", "Go to your house (building mode).", "Go to a friend's house.", "Never mind.");
	}

	@Override
	public void run(int interfaceId, int option) {
		switch (stage) {
		case -1:
			switch (option) {
			case FIRST:
				player.getHouse().setBuildMode(false);
				player.getHouse().enterMyHouse();
				end();
				break;
			case SECOND:
				player.getHouse().setBuildMode(true);
				player.getHouse().enterMyHouse();
				end();
				break;
			case THIRD:
				player.getPackets().sendInputNameScript("Enter name of the person who's house you'd like to join:", new StringInputAction() {

					@Override
					public void handle(String input) {
						House.enterHouse(player, input);
					}
				});
				end();
				break;
			case FOURTH:
				end();
				break;
			}
			break;
		}
	}

	@Override
	public void finish() {
	}

}