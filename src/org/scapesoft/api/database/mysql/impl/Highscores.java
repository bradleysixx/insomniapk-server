package org.scapesoft.api.database.mysql.impl;

import static org.scapesoft.game.player.Skills.CONSTRUCTION;
import static org.scapesoft.game.player.Skills.DUNGEONEERING;
import static org.scapesoft.game.player.Skills.HUNTER;
import static org.scapesoft.game.player.Skills.SUMMONING;

import java.sql.SQLException;
import java.sql.Statement;

import org.scapesoft.Constants;
import org.scapesoft.api.database.DatabaseConnection;
import org.scapesoft.game.World;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.misc.Utils;

/**
 * Handles the updating of the player's highscores information
 * 
 * @author Tyluur<itstyluur@gmail.com>
 * @since December 13th 2013
 */
public class Highscores {

	/**
	 * Updates the mysql table with the players skilling information
	 *
	 * @param player
	 *            The player to update it for
	 */
	public static void saveNewHighscores(Player player) {
		try {
			if (player.getRights() > 2 || !Constants.isVPS)
				return;
			DatabaseConnection connection = World.getConnectionPool().nextFree();
			String name = Utils.formatPlayerNameForDisplay(player.getUsername());
			try {
				Statement stmt = connection.createStatement();
				if (stmt == null) {
					return;
				}
				stmt.executeUpdate("DELETE FROM `highscores` WHERE username = '" + name + "';");
				stmt.executeUpdate("INSERT INTO `highscores` (`username`,`rights`,`overall_xp`,`attack_xp`,`defence_xp`,`strength_xp`,`constitution_xp`,`ranged_xp`,`prayer_xp`,`magic_xp`,`cooking_xp`,`woodcutting_xp`,`fletching_xp`,`fishing_xp`,`firemaking_xp`,`crafting_xp`,`smithing_xp`,`mining_xp`,`herblore_xp`,`agility_xp`,`thieving_xp`,`slayer_xp`,`farming_xp`,`runecrafting_xp`, `hunter_xp`, `construction_xp`, `summoning_xp`, `dungeoneering_xp`) VALUES ('" + name + "','" + player.getRights() + "','" + (player.getSkills().getTotalExp()) + "'," + player.getSkills().getXp(0) + "," + player.getSkills().getXp(1) + "," + player.getSkills().getXp(2) + "," + player.getSkills().getXp(3) + "," + player.getSkills().getXp(4) + "," + player.getSkills().getXp(5) + "," + player.getSkills().getXp(6) + "," + player.getSkills().getXp(7) + "," + player.getSkills().getXp(8) + "," + player.getSkills().getXp(9) + "," + player.getSkills().getXp(10) + "," + player.getSkills().getXp(11) + "," + player.getSkills().getXp(12) + "," + player.getSkills().getXp(13) + "," + player.getSkills().getXp(14) + "," + player.getSkills().getXp(15) + "," + player.getSkills().getXp(16) + "," + player.getSkills().getXp(17) + "," + player.getSkills().getXp(18) + "," + player.getSkills().getXp(19) + "," + player.getSkills().getXp(20) + "," + player.getSkills().getXp(HUNTER) + "," + player.getSkills().getXp(CONSTRUCTION) + "," + player.getSkills().getXp(SUMMONING) + "," + player.getSkills().getXp(DUNGEONEERING) + ");");
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				connection.returnConnection();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}