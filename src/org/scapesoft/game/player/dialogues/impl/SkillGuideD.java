package org.scapesoft.game.player.dialogues.impl;

import org.scapesoft.game.player.content.SkillScrolls;
import org.scapesoft.game.player.dialogues.Dialogue;
import org.scapesoft.game.player.dialogues.LevelUp;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Nov 1, 2014
 */
public class SkillGuideD extends Dialogue {

	@Override
	public void start() {
		this.lvlupskill = (Integer) parameters[0];
		this.skillmenu = (Integer) parameters[1];
		this.skillId = (Integer) parameters[2];
		sendOptionsDialogue("Select an Option", "View Skill Scroll", "View RS-Guide");
	}

	@Override
	public void run(int interfaceId, int option) {
		switch(option) {
		case FIRST:
			SkillScrolls.get().displayScroll(player, skillId);
			break;
		case SECOND:
			player.getInterfaceManager().sendInterface(lvlupskill != -1 ? 741 : 499);
			if (skillId != -1) {
				LevelUp.switchFlash(player, skillId, false);
			}
			if (skillmenu != -1) {
				player.getTemporaryAttributtes().put("skillMenu", skillmenu);
			}
			break;
		}
		end();
	}

	@Override
	public void finish() {
		
	}
	
	int lvlupskill;
	int skillmenu;
	int skillId;

}
