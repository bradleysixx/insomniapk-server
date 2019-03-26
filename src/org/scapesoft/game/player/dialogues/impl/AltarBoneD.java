package org.scapesoft.game.player.dialogues.impl;

import org.scapesoft.game.WorldObject;
import org.scapesoft.game.player.actions.prayer.ConstructionAltarAction;
import org.scapesoft.game.player.content.SkillsDialogue;
import org.scapesoft.game.player.dialogues.Dialogue;

/**
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since Jan 15, 2014
 */
public class AltarBoneD extends Dialogue {
	
	int bone;
	WorldObject object;

	@Override
	public void start() {
		bone = (int) parameters[0];
		object = (WorldObject) parameters[1];
		SkillsDialogue.sendSkillsDialogue(player, SkillsDialogue.SELECT, "How many bones would you like to use?", player.getInventory().getItems().getNumberOf(bone), new int[] { bone }, null);
	}

	@Override
	public void run(int interfaceId, int option) {
		end();
		player.getActionManager().setAction(new ConstructionAltarAction(object, bone, SkillsDialogue.getQuantity(player)));
	}

	@Override
	public void finish() {

	}

}
