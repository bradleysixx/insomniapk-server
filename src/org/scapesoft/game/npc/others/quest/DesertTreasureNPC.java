package org.scapesoft.game.npc.others.quest;

import org.scapesoft.game.Entity;
import org.scapesoft.game.ForceTalk;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.controlers.impl.quest.DesertTreasure;
import org.scapesoft.utilities.game.player.TeleportLocations;

public class DesertTreasureNPC extends QuestNPC {

	public DesertTreasureNPC(String target, int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(target, id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
		this.setSpawned(true);
		this.setForceAgressive(true);
	}

	private static final long serialVersionUID = -2490461318196371202L;

	@Override
	public void sendDeath(Entity source) {
		if (getId() != 1974) {
			super.sendDeath(source);
			if (getMostDamageReceivedSourcePlayer() == null) {
				return;
			}
			Player player = this.getMostDamageReceivedSourcePlayer();
			if (player.getControllerManager().getController() instanceof DesertTreasure) {
				if (!player.getFacade().getDesertTreasureKills().contains(getId())) {
					player.getFacade().getDesertTreasureKills().add(getId());
				}
				player.getControllerManager().forceStop();
				player.setNextWorldTile(TeleportLocations.QUESTING_DOME);
				int amtToKill = (4) - player.getFacade().getDesertTreasureKills().size();
				player.getDialogueManager().startDialogue("SimpleNPCMessage", 1971, "You have defeated " + getName().toLowerCase() + ".", "You have " + (amtToKill) + " more monster" + (amtToKill == 1 ? "" : "s") + " to kill.");
				this.finish();
				if (amtToKill == 0) {
					player.getQuestManager().finishQuest("Desert Treasure");
				}
			}
		} else {
			transformInto(1975);
			setHitpoints(getMaxHitpoints());
			setNextForceTalk(new ForceTalk("I am Damis, invincible Lord of the Shadows!"));
		}
	}

	@Override
	public void finish() {
		if (hasFinished()) {
			return;
		}
		super.finish();
	}

}
