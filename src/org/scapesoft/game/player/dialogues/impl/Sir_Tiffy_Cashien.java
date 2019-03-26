package org.scapesoft.game.player.dialogues.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import org.scapesoft.api.event.listeners.interfaces.Scrollable;
import org.scapesoft.api.event.listeners.interfaces.SkillSelectionInterface;
import org.scapesoft.api.input.IntegerInputAction;
import org.scapesoft.game.item.Decanting;
import org.scapesoft.game.item.Item;
import org.scapesoft.game.player.dialogues.ChatAnimation;
import org.scapesoft.game.player.dialogues.Dialogue;
import org.scapesoft.utilities.game.player.ForumGroup.ForumGroups;
import org.scapesoft.utilities.misc.ChatColors;
import org.scapesoft.utilities.misc.Utils;

import com.google.common.collect.ImmutableSortedMap;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Dec 27, 2014
 */
public class Sir_Tiffy_Cashien extends Dialogue {

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendOptionsDialogue("Select an Option", "Decant Potions", "Buy Skillcapes", "Reclaim lost items", "Track Monster Kills");
	}

	@Override
	public void run(int interfaceId, int option) {
		switch (stage) {
		case -1:
			switch (option) {
			case FIRST:
				sendNPCDialogue(npcId, ChatAnimation.NORMAL, "What dose should they be decanted to?", "It costs " + Utils.format(Decanting.COST_PER_POTION) + " gp per potion.");
				stage = 0;
				break;
			case SECOND:
				SkillSelectionInterface.display(player);
				sendDialogue("Select the skill in which you wish to buy a cape!");
				player.getTemporaryAttributtes().put("skill_selection_type", "CAPES");
				stage = -2;
				break;
			case THIRD:
				if (player.getUnclaimedUntradeables().size() == 0) {
					sendNPCDialogue(npcId, ChatAnimation.NORMAL, "You have no lost items to claim");
					stage = -2;
					break;
				}
				if (player.isInGroup(ForumGroups.DIAMOND_MEMBER)) {
					sendNPCDialogue(npcId, ChatAnimation.NORMAL, "Since you are a diamond member, you can have these", "back for free!");
					stage = 1;
				} else {
					sendNPCDialogue(npcId, ChatAnimation.NORMAL, "You will have to pay a fee per lost item.", "Diamond members don't have this limitation.", "Are you sure you want to do this?");
					stage = 2;
				}
				break;
			case FOURTH:
				Map<String, Long> kills = ImmutableSortedMap.copyOf(player.getMonstersKilled(), new Comparator<String>() {

					@Override
					public int compare(String arg0, String arg1) {
						return arg0.compareTo(arg1);
					}
				});
				Iterator<Entry<String, Long>> it$ = kills.entrySet().iterator();
				List<String> messages = new ArrayList<>();
				while(it$.hasNext()) {
					Entry<String, Long> entry = it$.next();
					long killCount = entry.getValue();
					messages.add("<col=" + ChatColors.MAROON + ">" + entry.getKey() + "</col>:<col=" + ChatColors.BLUE + ">" + killCount + "</col> kill" + (killCount > 1 ? "s" : ""));
				}
				Scrollable.sendQuestScroll(player, "Tracking Monster Kills", messages.toArray(new String[messages.size()]));
				break;
			}
			break;
		case 0:
			final Sir_Tiffy_Cashien d = this;
			player.getPackets().sendInputIntegerScript("Enter Dose", new IntegerInputAction() {

				@Override
				public void handle(int input) {
					if (input > 4) {
						d.end();
						return;
					}
					if (player.takeMoney(Decanting.getPotionCost(player))) {
						Decanting.decantInventory(player, input);
						d.end();
					} else {
						d.sendPlayerDialogue(ChatAnimation.SAD, "I don't have that much money actually...");
						d.stage = -2;
					}
				}
			});
			break;
		case 1:
			claimBackLost();
			sendNPCDialogue(npcId, ChatAnimation.LISTEN_LAUGH, "Enjoy your lost items back!");
			stage = -2;
			break;
		case 2:
			sendOptionsDialogue("Pay " + Utils.format(getTotalCost()) + " for " + Utils.format(player.getUnclaimedUntradeables().size()) + " items?", "Yes", "No");
			stage = 3;
			break;
		case 3:
			switch (option) {
			case FIRST:
				if (player.takeMoney(getTotalCost())) {
					claimBackLost();
				} else {
					sendPlayerDialogue(ChatAnimation.SAD, "I don't have that much money...");
					stage = -2;
				}
				break;
			case SECOND:
				end();
				stage = -2;
				break;
			}
			break;
		}
	}

	@Override
	public void finish() {

	}

	/**
	 * The total cost the player has to pay to receive their items back
	 * 
	 * @return
	 */
	private int getTotalCost() {
		int cost = 0;
		for (Item item : player.getUnclaimedUntradeables()) {
			cost += item.getDefinitions().getValue();
		}
		return cost;
	}

	/**
	 * The player interacting with the npc will receive all of their lost items
	 * back via {@code Player#getUnclaimedUntradeables()}
	 */
	public void claimBackLost() {
		ListIterator<Item> it$ = player.getUnclaimedUntradeables().listIterator();
		while (it$.hasNext()) {
			Item item = it$.next();
			player.getInventory().addDroppable(item);
			it$.remove();
		}
	}

	int npcId;
}
