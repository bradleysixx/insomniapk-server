package org.scapesoft.api.event.command.impl;

import java.util.Map.Entry;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.quests.Quest;
import org.scapesoft.game.player.quests.QuestManager;
import org.scapesoft.utilities.game.player.Rights;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Mar 29, 2014
 */
public class FinishQuests extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.OWNER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[]{"fq", "cq"};
	}

	@Override
	public void execute(Player player) {
		player.getQuestManager().getProgressed().clear();
		for (Entry<String, Quest<?>> quest : QuestManager.getQuests().entrySet()) {
			player.getQuestManager().startQuest(quest.getValue().getClass());
			quest.getValue().completeQuest(player);
		}
		player.getFacade().setLastRFDWave(4);
	}

}
