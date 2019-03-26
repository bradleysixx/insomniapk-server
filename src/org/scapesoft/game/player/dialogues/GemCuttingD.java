package org.scapesoft.game.player.dialogues;

import org.scapesoft.game.player.actions.GemCutting;
import org.scapesoft.game.player.actions.GemCutting.Gem;
import org.scapesoft.game.player.content.SkillsDialogue;

public class GemCuttingD extends Dialogue {

	private Gem gem;

	@Override
	public void start() {
		this.gem = (Gem) parameters[0];
		SkillsDialogue.sendSkillsDialogue(player, SkillsDialogue.CUT, "Choose how many you wish to cut,<br>then click on the item to begin.", player.getInventory().getItems().getNumberOf(gem.getUncut()), new int[] { gem.getUncut() }, null);

	}

	@Override
	public void run(int interfaceId, int componentId) {
		player.getActionManager().setAction(new GemCutting(gem, SkillsDialogue.getQuantity(player)));
		end();
	}

	@Override
	public void finish() {

	}

}
