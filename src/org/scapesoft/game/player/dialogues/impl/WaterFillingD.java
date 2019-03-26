package org.scapesoft.game.player.dialogues.impl;

import org.scapesoft.game.player.actions.WaterFilling;
import org.scapesoft.game.player.actions.WaterFilling.Fill;
import org.scapesoft.game.player.content.SkillsDialogue;
import org.scapesoft.game.player.dialogues.Dialogue;

public class WaterFillingD extends Dialogue {

	private Fill fill;

	@Override
	public void start() {
		this.fill = (Fill) parameters[0];
		SkillsDialogue.sendSkillsDialogue(player, SkillsDialogue.MAKE, "Choose how many you wish to fill,<br>then click on the item to begin.", player.getInventory().getItems().getNumberOf(fill.getEmpty()), new int[] { fill.getEmpty() }, null);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		player.getActionManager().setAction(new WaterFilling(fill, SkillsDialogue.getQuantity(player)));
		end();
	}

	@Override
	public void finish() {

	}

}
