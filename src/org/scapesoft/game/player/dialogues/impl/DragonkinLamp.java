package org.scapesoft.game.player.dialogues.impl;

import org.scapesoft.api.event.listeners.interfaces.SkillSelectionInterface;
import org.scapesoft.game.player.dialogues.Dialogue;

public class DragonkinLamp extends Dialogue {

	@Override
	public void start() {
		sendDialogue("Select the skill you wish to receive experience in");
		SkillSelectionInterface.display(player);
		player.getTemporaryAttributtes().put("skill_selection_type", "EXPERIENCE_KIN");
	}

	@Override
	public void run(int interfaceId, int option) {
		end();
	}

	@Override
	public void finish() {
	}

}
