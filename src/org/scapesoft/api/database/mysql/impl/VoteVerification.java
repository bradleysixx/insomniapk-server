package org.scapesoft.api.database.mysql.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.scapesoft.api.database.DatabaseConnection;
import org.scapesoft.game.World;
import org.scapesoft.game.item.Item;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.content.achievements.impl.VoteEasyAchievement;
import org.scapesoft.game.player.dialogues.impl.SimpleItemMessage;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Mar 30, 2014
 */
public class VoteVerification {

	/**
	 * Checks for the vote by the player's name in the database and gives them
	 * their reward
	 * 
	 * @param player
	 *            The player checking their vote
	 */
	public static void checkVote(Player player) {
		DatabaseConnection connection = World.getConnectionPool().nextFree();
		String username = player.getUsername();
		try {
			Statement stmt = connection.createStatement();
			username = username.replaceAll(" ", "_");
			ResultSet rs = stmt.executeQuery("SELECT rewardid FROM `has_voted` WHERE username = '" + username + "' and given = '0'");
			if (rs.next()) {
				switch (rs.getInt("rewardid")) {
				case 0:
					player.getInventory().addItem(new Item(VOTE_TOKEN, 1));
					break;
				}
				/** Notifying the achievement to update */
				player.getAchievementManager().notifyUpdate(VoteEasyAchievement.class);

				/** Increments the times voted */
				player.getFacade().setTimesVoted(player.getFacade().getTimesVoted() + 1);

				/** Thanking the player for voting */
				player.getDialogueManager().startDialogue(SimpleItemMessage.class, VOTE_TOKEN, "You have successfully claimed your vote!", "Thank you.");

				/** Updating the MySQL table */
				stmt.execute("DELETE FROM `has_voted` WHERE username = '" + username + "'");
			} else {
				player.sendMessage("You have not voted yet.");
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connection.returnConnection();
		}
	}

	/**
	 * Checks the auth code in the database and gives a reward
	 * 
	 * @param player
	 *            The player
	 * @param auth
	 *            The auth code
	 */
	public static void checkAuth(Player player, String auth) {
		DatabaseConnection connection = World.getConnectionPool().nextFree();
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM `confirmed` WHERE `authCode` ='" + auth + "' AND `claimed` ='0'");
			if (rs.next()) {
				rs.getStatement().execute("UPDATE `confirmed` SET `claimed` = '1' WHERE `authCode` ='" + auth + "';");
				player.getInventory().addItem(new Item(VOTE_TOKEN, 1));
				/** Notifying the achievement to update */
				player.getAchievementManager().notifyUpdate(VoteEasyAchievement.class);
				/** Increments the times voted */
				player.getFacade().setTimesVoted(player.getFacade().getTimesVoted() + 1);
				/** Thanking the player for voting */
				player.getDialogueManager().startDialogue(SimpleItemMessage.class, VOTE_TOKEN, "You have successfully claimed your vote!", "Thank you.");
			} else {
				player.sendMessage("Authcode: " + auth + " is invalid.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connection.returnConnection();
		}
	}

	/**
	 * The id of the vote token
	 */
	private static final int VOTE_TOKEN = 7775;

}