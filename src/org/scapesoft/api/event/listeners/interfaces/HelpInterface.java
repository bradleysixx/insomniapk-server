package org.scapesoft.api.event.listeners.interfaces;

import org.scapesoft.Constants;
import org.scapesoft.api.event.EventListener;
import org.scapesoft.game.WorldObject;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.item.Item;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.content.TicketSystem;
import org.scapesoft.utilities.misc.ChatColors;
import org.scapesoft.utilities.misc.Utils;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jun 20, 2014
 */
public class HelpInterface extends EventListener {

	@Override
	public int[] getEventIds() {
		return new int[] { 761 };
	}

	private enum HelpOption {

		SHOPS("Where do I buy items?") {
			@Override
			public void onClick(Player player) {
				Scrollable.sendQuestScroll(player, "F.A.Q", "We don't have normal shops, here at " + Constants.SERVER_NAME + " every item you", "essentially need can be bought from the Grand Exchange", " south of home.", "<br><br>", "Need a general store? Talk to the shopkeeper at home too.");
			}
		},

		TRAINING("Where can I train fast?") {

			@Override
			public void onClick(Player player) {
				Scrollable.sendQuestScroll(player, "F.A.Q", "Speak to the Wizard walking around home and navigate", "to training locations and select \"Training Island\".");
			}

		},

		MONEY_MAKING("How can I make money quickly?") {

			@Override
			public void onClick(Player player) {
				Scrollable.sendQuestScroll(player, "F.A.Q", "A great way to make money is to kill monsters all around the world. They all have a chance of dropping caskets, which you can open to receive a cash reward.", "<br><br>", "You can also talk to Mandrith at home to find out about Wilderness Points which can be exchanged for <col=" + ChatColors.MAROON + ">valuable</col> rewards.");
			}
		},

		MEMBERSHIP("Where can I donate/buy items?") {

			@Override
			public void onClick(Player player) {
				Scrollable.sendQuestScroll(player, "F.A.Q", "<col=" + ChatColors.MAROON + ">Type ::member to purchase items and membership ranks.");
			}

		},
		
		STAFF_HELP("I need help from a staff member NOW") {

			@Override
			public void onClick(Player player) {
				TicketSystem.requestTicket(player);
			}
			
		};

		HelpOption(String line) {
			this.line = line;
		}

		private final String line;

		public abstract void onClick(Player player);
	}

	/**
	 * Displays the help interface to the player
	 * 
	 * @param player
	 *            The player to display the interface to
	 */
	public static void display(Player player) {
		player.closeInterfaces();
		int interfaceId = 761;
		int startLine = 10;
		int lineCount = Utils.getInterfaceDefinitionsComponentsSize(interfaceId);
		for (int k = 0; k < lineCount; k++) {
			player.getPackets().sendIComponentText(interfaceId, k, "");
		}
		for (HelpOption option : HelpOption.values()) {
			player.getPackets().sendIComponentText(interfaceId, startLine, "<col=" + ChatColors.LIGHT_BLUE + ">" + option.line);
			startLine++;
		}

		player.getPackets().sendIComponentText(interfaceId, 6, "What do you need help with?");
		player.getPackets().sendIComponentText(interfaceId, 23, "If you can't find what you need help with here, request staff assistance.");
		player.getInterfaceManager().sendInterface(interfaceId);
	}

	@Override
	public boolean handleButtonClick(Player player, int interfaceId, int buttonId, int packetId, int slotId, int itemId) {
		if (buttonId >= 10 && buttonId <= 21) {
			int index = (buttonId - 10);
			for (HelpOption option : HelpOption.values()) {
				if (option.ordinal() == index) {
					option.onClick(player);
					break;
				}
			}
		}
		return true;
	}

	@Override
	public boolean handleObjectClick(Player player, int objectId, WorldObject worldObject, WorldTile tile, ClickOption option) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean handleNPCClick(Player player, NPC npc, ClickOption option) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean handleItemClick(Player player, Item item, ClickOption option) {
		// TODO Auto-generated method stub
		return false;
	}

}
