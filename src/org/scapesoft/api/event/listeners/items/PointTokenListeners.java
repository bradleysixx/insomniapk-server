package org.scapesoft.api.event.listeners.items;

import org.scapesoft.api.event.EventListener;
import org.scapesoft.game.item.Item;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.dialogues.Dialogue;
import org.scapesoft.game.player.dialogues.impl.SimpleItemMessage;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Jan 17, 2015
 */
public class PointTokenListeners extends EventListener {

	@Override
	public int[] getEventIds() {
		return new int[] { 4278, 12852 };
	}

	@Override
	public boolean handleItemClick(Player player, Item item, ClickOption option) {
		switch (item.getId()) {
		case 4278:
			switch (option) {
			case FIRST:
				player.getDialogueManager().startDialogue(SimpleItemMessage.class, item.getId(), "This token can be exchanged for gold points.", "Select the \"Exchange\" option on this items' right-click", "menu to receive gold points for this item.");
				break;
			case SECOND:
				player.getDialogueManager().startDialogue(new ConfirmDialogue() {

					@Override
					public void run(int interfaceId, int option) {
						if (option == FIRST) {
							player.getInventory().deleteItem(item);
							player.getFacade().setGoldPoints(player.getFacade().getGoldPoints() + item.getAmount());
							sendItemDialogue(item.getId(), "You receive " + item.getAmount() + " points from the " + item.getName());
							stage = -2;
						} else {
							end();
						}
					}
				
				}, item.getId(), item.getAmount(), 0);
				break;
			default:
				break;
			}
			break;
		case 12852:
			switch (option) {
			case FIRST:
				player.getDialogueManager().startDialogue(SimpleItemMessage.class, item.getId(), "This token can be exchanged for wilderness points.", "Select the \"Exchange\" option on this items' right-click", "menu to receive gold points for this item.");
				break;
			case SECOND:
				player.getDialogueManager().startDialogue(new ConfirmDialogue() {

					@Override
					public void run(int interfaceId, int option) {
						if (option == FIRST) {
							player.getInventory().deleteItem(item);
							player.getFacade().addWildernessPoints(item.getAmount());
							int type = (int) parameters[2];
							sendItemDialogue(item.getId(), "You receive " + item.getAmount() + " " + (type == 0 ? "Gold" : "Wilderness") + " points from the token.");
							stage = -2;
						} else {
							end();
						}
					}
				
				}, item.getId(), item.getAmount(), 1);
				break;
			default:
				break;
			}
			break;
		}
		return true;
	}

	private abstract class ConfirmDialogue extends Dialogue {

		@Override
		public void start() {
			int amount = (int) parameters[1];
			int type = (int) parameters[2];
			sendOptionsDialogue("Exchange " + amount + "x tokens for " + amount + "<br>" + (type == 0 ? "Gold" : "Wilderness") + " points?", "Yes", "No");
		}

		@Override
		public abstract void run(int interfaceId, int option);

		@Override
		public void finish() {
		}

	}

}
