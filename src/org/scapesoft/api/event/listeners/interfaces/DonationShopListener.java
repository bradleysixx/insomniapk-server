package org.scapesoft.api.event.listeners.interfaces;

import org.scapesoft.api.event.EventListener;
import org.scapesoft.game.WorldObject;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.item.Item;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.console.gson.GsonHandler;
import org.scapesoft.utilities.console.gson.impl.ShopsLoader;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jul 28, 2014
 */
public class DonationShopListener extends EventListener {

	@Override
	public int[] getEventIds() {
		return new int[] { 6 };
	}

	/**
	 * Displays the shop interface
	 * 
	 * @param player
	 *            The player to display it to
	 */
	public static void display(Player player) {
		player.closeInterfaces();

		int interfaceId = 6;

		int[] toHide = { 6, 7, 10, 11, 4, 5, 3, 2 };

		for (int comp : toHide) {
			player.getPackets().sendHideIComponent(interfaceId, comp, true);
		}

		player.getPackets().sendIComponentText(interfaceId, 22, "Member-Only Armour");
		player.getPackets().sendIComponentText(interfaceId, 20, "PvM Armour");
		player.getPackets().sendIComponentText(interfaceId, 19, "Weapons");
		player.getPackets().sendIComponentText(interfaceId, 16, "Untradeables");	
		player.getPackets().sendIComponentText(interfaceId, 14, "Rares");
		player.getPackets().sendIComponentText(interfaceId, 43, "Select a Shop");

		player.getPackets().sendItemOnIComponent(interfaceId, 23, 21476, 1);
		player.getPackets().sendItemOnIComponent(interfaceId, 21, 20139, 1);
		player.getPackets().sendItemOnIComponent(interfaceId, 18, 14484, 1);
		player.getPackets().sendItemOnIComponent(interfaceId, 17, 6570, 1);
		player.getPackets().sendItemOnIComponent(interfaceId, 15, 1055, 1);

		player.getInterfaceManager().sendInterface(interfaceId);
	}

	@Override
	public boolean handleButtonClick(Player player, int interfaceId, int buttonId, int packetId, int slotId, int itemId) {
		switch (buttonId) {
		case 24:
			GsonHandler.<ShopsLoader> getJsonLoader(ShopsLoader.class).openShop(player, "Gold Points Only");
			break;
		case 25:
			GsonHandler.<ShopsLoader> getJsonLoader(ShopsLoader.class).openShop(player, "Gold Points Armours");
			break;
		case 26:
			GsonHandler.<ShopsLoader> getJsonLoader(ShopsLoader.class).openShop(player, "Gold Points Weapons");
			break;
		case 27:
			GsonHandler.<ShopsLoader> getJsonLoader(ShopsLoader.class).openShop(player, "Gold Points Untradeables");
			break;
		case 28:
			GsonHandler.<ShopsLoader> getJsonLoader(ShopsLoader.class).openShop(player, "Gold Points Rares");
			break;
		}
		return true;
	}

	@Override
	public boolean handleObjectClick(Player player, int objectId, WorldObject worldObject, WorldTile tile, ClickOption option) {
		return false;
	}

	@Override
	public boolean handleNPCClick(Player player, NPC npc, ClickOption option) {
		return false;
	}

	@Override
	public boolean handleItemClick(Player player, Item item, ClickOption option) {
		return false;
	}

}
