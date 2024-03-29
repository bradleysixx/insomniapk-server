package org.scapesoft.game.player.content;

import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.clans.Clan;
import org.scapesoft.game.player.clans.ClanLoader;
import org.scapesoft.utilities.misc.Utils;

/**
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since Dec 12, 2013
 */
public class ClanChatManager {

	/**
	 * Creates a clan for the player
	 * @param player The player making the clan
	 * @param name The name of th eclan
	 */
	public static void createClan(Player player, String name) {
		name = Utils.formatPlayerNameForDisplay(name);
		Clan clan = new Clan(name, player);
		ClanLoader.addClanAndSave(clan);
	}

	public static void joinMyClan(Player player) {
		if (ClanLoader.getClanByName(player.getDisplayName()) == null) {
			player.getDialogueManager().startDialogue("ClanCreateDialogue");
			return;
		}
	}

}
