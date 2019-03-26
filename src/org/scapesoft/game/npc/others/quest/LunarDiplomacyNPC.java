package org.scapesoft.game.npc.others.quest;

import org.scapesoft.game.WorldTile;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.quests.QuestManager;
import org.scapesoft.game.player.quests.impl.Lunar_Diplomacy;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jun 29, 2014
 */
public class LunarDiplomacyNPC extends QuestNPC {

	public LunarDiplomacyNPC(String target, int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(target, id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
		setSpawned(true);
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
			System.out.println("RFD NPC had no killer!");
			return;
		}
		killer.getQuestManager().finishQuest(QuestManager.getQuest(Lunar_Diplomacy.class).getName());
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 8860295790986915718L;

}
