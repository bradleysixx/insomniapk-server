package org.scapesoft.game.player.dialogues.impl;

import org.scapesoft.cache.loaders.ItemDefinitions;
import org.scapesoft.game.item.Item;
import org.scapesoft.game.player.dialogues.ChatAnimation;
import org.scapesoft.game.player.dialogues.Dialogue;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Nov 29, 2014
 */
public class SnowQueenUpgradeD extends Dialogue {

	@Override
	public void start() {
		this.npcId = (Integer) parameters[0];
		this.baseId = ((Item) parameters[1]).getId();
		this.upgradedId = (Integer) parameters[2];
		sendNPCDialogue(npcId, ChatAnimation.NORMAL, "Would you like to upgrade your " + ItemDefinitions.getItemDefinitions(baseId).getName().toLowerCase() + "?", "It costs 5,000,000 coins per upgrade.");
	}

	@Override
	public void run(int interfaceId, int option) {
		switch (stage) {
		case -1:
			sendOptionsDialogue("Select an Option", "Yes, I would.", "No");
			stage = 0;
			break;
		case 0:
			switch (option) {
			case FIRST:
				if (!player.getInventory().contains(baseId)) {
					sendNPCDialogue(npcId, ChatAnimation.SAD, "You no longer have your item to upgrade!");
					return;
				}
				if (player.takeMoney(5000000)) {
					player.getInventory().deleteItem(baseId, 1);
					player.getInventory().addItem(upgradedId, 1);
					sendPlayerDialogue(ChatAnimation.LAUGHING, "OMG thanks!", "I can't wait to use my " + ItemDefinitions.getItemDefinitions(upgradedId).name.toLowerCase() + "!");
				} else {
					sendNPCDialogue(npcId, ChatAnimation.SAD, "You don't have enough money...");
				}
				stage = -2;
				break;
			case SECOND:
				end();
				break;
			}
			break;
		}
	}

	@Override
	public void finish() {

	}

	int npcId;
	int baseId;
	int upgradedId;

}
