package org.scapesoft.game.player.dialogues.impl;

import org.scapesoft.api.event.listeners.interfaces.HelpInterface;
import org.scapesoft.game.player.content.PlayerLook;
import org.scapesoft.game.player.content.TicketSystem;
import org.scapesoft.game.player.controlers.impl.StartTutorial;
import org.scapesoft.game.player.dialogues.ChatAnimation;
import org.scapesoft.game.player.dialogues.Dialogue;
import org.scapesoft.utilities.misc.ChatColors;
import org.scapesoft.utilities.misc.Utils.CombatRates;

public final class HelpDialogue extends Dialogue {

	private static final int npcId = 2244;

	@Override
	public void start() {
		sendNPCDialogue(npcId, ChatAnimation.NORMAL, "Hello " + player.getDisplayName() + ". How may I help you?");
	}

	@Override
	public void run(int interfaceId, int option) {
		switch (stage) {
		case -1:
			sendOptionsDialogue("Select an Option", "View F.A.Q", "Modify Appearance", "Request Staff Assistance");
			stage = 0;
			break;
		case 0:
			if (player.getControllerManager().getController() instanceof StartTutorial) {
				StartTutorial controler = (StartTutorial) player.getControllerManager().getController();
				if ((controler.getStage() == 6 && option != SECOND)) {
					sendDialogue("Please select the correct option.");
					stage = -1;
					return;
				}
			}
			switch (option) {
			case FIRST:
				HelpInterface.display(player);
				end();
				break;
			case SECOND:
				PlayerLook.openCharacterCustomizing(player);
				end();
				break;
			case THIRD:
				TicketSystem.requestTicket(player);
				end();
				break;
			}
			break;
		case 1:
			CombatRates rates = null;
			switch (option) {
			case FIRST:
				rates = CombatRates.EASY;
				break;
			case SECOND:
				rates = CombatRates.NORMAL;
				break;
			case THIRD:
				rates = CombatRates.HARD;
				break;
			case FOURTH:
				rates = CombatRates.LEGEND;
				break;
			}
			if (rates != null) {
				player.getFacade().setModifiers(rates);
				if (player.getControllerManager().getController() instanceof StartTutorial) {
					end();
					StartTutorial controler = (StartTutorial) player.getControllerManager().getController();
					controler.setStage(controler.getStage() + 1);
					controler.sendTutorialDialogue(false);
				} else {
					sendDialogue("You are now playing on <col=" + ChatColors.BLUE + ">" + rates.name().toLowerCase() + "</col> rates.", "Visit your <col=" + ChatColors.BLUE + ">information tab</col> if you forget your rates.");
					stage = -2;
				}
			} else {
				end();
			}
			break;
		case 2:
			end();
			break;
		}
	}

	@Override
	public void finish() {

	}

}
