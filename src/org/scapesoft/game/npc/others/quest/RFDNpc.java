package org.scapesoft.game.npc.others.quest;

import org.scapesoft.game.Animation;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.controlers.impl.quest.RFDQuest;
import org.scapesoft.game.player.quests.QuestManager;
import org.scapesoft.game.player.quests.impl.Recipe_For_Disaster;

public class RFDNpc extends NPC {

	public RFDNpc(int id, WorldTile tile) {
		super(id, tile, -1, true, true);
		setSpawned(true);
		setNextAnimation(new Animation(-1));
	}

	@Override
	public void drop() {
		Player killer = getMostDamageReceivedSourcePlayer();
		if (killer == null) {
			if (getCombat() != null && getCombat().getTarget() != null) {
				killer = getCombat().getTarget().isPlayer() ? getCombat().getTarget().player() : null;
			}
		}
		if (killer == null) {
			System.out.println("RFD NPc had no killer!");
			return;
		}
		killer.getFacade().setLastRFDWave(killer.getFacade().getLastRFDWave() + 1);
		int index = getArrayIndex();
		if (index != -1) {
			((Recipe_For_Disaster) killer.getQuestManager().getProgressedQuest(QuestManager.getQuest(Recipe_For_Disaster.class).getName())).getKilledBosses()[index] = true;
		}
		if (killer.getControllerManager().getController() instanceof RFDQuest) {
			((RFDQuest) killer.getControllerManager().getController()).nextWave();
		}
	}

	/**
	 * Get the index in the array for the npc id
	 * 
	 * @return
	 */
	private int getArrayIndex() {
		switch (getId()) {
		case 3493:
			return Recipe_For_Disaster.AGRITH_NA_NA_INDEX;
		case 3494:
			return Recipe_For_Disaster.FLAMBEED_INDEX;
		case 3495:
			return Recipe_For_Disaster.KARAMEL_INDEX;
		case 3496:
			return Recipe_For_Disaster.DESSOURT_INDEX;
		}
		return -1;
	}

	private static final long serialVersionUID = -7664850315199508535L;

}
