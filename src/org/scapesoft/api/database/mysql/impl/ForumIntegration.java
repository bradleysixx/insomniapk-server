package org.scapesoft.api.database.mysql.impl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.scapesoft.Constants;
import org.scapesoft.api.database.DatabaseConnection;
import org.scapesoft.game.World;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.dialogues.SimpleMessage;
import org.scapesoft.utilities.game.player.ForumGroup.ForumGroups;
import org.scapesoft.utilities.game.web.WebPage;
import org.scapesoft.utilities.misc.ChatColors;
import org.scapesoft.utilities.misc.Utils;

public class ForumIntegration {

	/**
	 * Bans the user in the forum
	 *
	 * @param username
	 *            The name of the user to ban
	 */
	public static void ban(String username) {
		DatabaseConnection connection = World.getConnectionPool().nextFree();
		if (connection == null)
			throw new RuntimeException("Could not ban user: " + username + "; connection to database was null");
		try {
			Statement stmt = connection.createStatement();
			stmt.executeUpdate("UPDATE `xf_user` SET  `user_group_id` ='" + ForumGroups.BANNED.getId() + "' WHERE " + "username='" + Utils.formatPlayerNameForDisplay(username) + "'");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.returnConnection();
			}
		}
	}

	/**
	 * Finds out if the name is banned on the forum
	 *
	 * @param username
	 *            The username
	 * @return
	 */
	public static boolean isBanned(String username) {
		DatabaseConnection connection = World.getConnectionPool().nextFree();
		if (connection == null) {
			throw new RuntimeException("Could not create database connection!");
		}
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM `ipb_members` WHERE " + "name='" + Utils.formatPlayerNameForDisplay(username) + "' AND member_group_id='" + ForumGroups.BANNED.getId() + "'");
			if (rs.next()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.returnConnection();
			}
		}
		return false;
	}

	/**
	 * Unbans the user from the forum if they are banned
	 *
	 * @param username
	 *            The name of the user to unban
	 */
	public static boolean unban(String username) {
		if (!isBanned(username)) {
			return true;
		}
		DatabaseConnection connection = World.getConnectionPool().nextFree();
		if (connection == null) {
			throw new RuntimeException("Could not unban user: " + username + "; connection to database was null");
		}
		try {
			Statement stmt = connection.createStatement();
			stmt.executeUpdate("UPDATE `xf_user` SET  `user_group_id` ='" + ForumGroups.MEMBER.getId() + "' WHERE " + "username='" + Utils.formatPlayerNameForDisplay(username) + "'");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.returnConnection();
			}
		}
		return false;
	}
	
	/**
	 * Tells you whether the credentials provided from login are correct. If you
	 * are on the local machine it will always be correct
	 *
	 * @param username
	 *            The username to check for login
	 * @param password
	 *            The password for the username to check for login
	 * @return
	 */
	public static IntegrationReturns correctCredentials(String username, String password) {
		if (!Constants.isVPS) {
			return IntegrationReturns.CORRECT;
		}
		String tempName = Utils.formatPlayerNameForDisplay(username);
		DatabaseConnection connection = World.getConnectionPool().nextFree();
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM `ipb_members` WHERE name='" + tempName + "';");
			if (rs.next()) {
				String salt = rs.getString("members_pass_salt");
				String forumHash = rs.getString("members_pass_hash");
				String ourHash = MD5(MD5(salt) + MD5(password));
				if (ourHash.equals(forumHash)) {
					return IntegrationReturns.CORRECT;
				} else {
					return IntegrationReturns.WRONG_CREDENTIALS;
				}
			} else {
				return IntegrationReturns.NON_EXISTANT_USERNAME;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return IntegrationReturns.SQL_ERROR;
		} finally {
			if (connection != null) {
				connection.returnConnection();
			}
		}
	}

	/**
	 * Registering a user into the forum
	 * 
	 * @param name
	 *            The name of the user
	 * @param password
	 *            The password of the user
	 */
	public static void registerUser(String name, String password) {
		String tempName = Utils.formatPlayerNameForDisplay(name);
		try {
			tempName = tempName.replaceAll(" ", "%20");
			WebPage page = new WebPage("http://scapesoft.org/community/index.php?/page/register.php?crypt=199219971992&name=" + tempName + "&pass=" + password);
			page.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String passwordToHash(String password, String salt) {
		String pass2 = "";
		try {
			pass2 = convertPassword(password);
			pass2 = convertPassword(pass2 + salt);
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return pass2;
	}

	public static String convertPassword(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest md;
		md = MessageDigest.getInstance("MD5");
		byte[] md5hash = new byte[32];
		md.update(text.getBytes("iso-8859-1"), 0, text.length());
		md5hash = md.digest();
		return convertToHex(md5hash);
	}

	public static String convertToHex(byte[] data) {
		StringBuffer buf = new StringBuffer();
		for (byte element : data) {
			int halfbyte = (element >>> 4) & 0x0F;
			int two_halfs = 0;
			do {
				if ((0 <= halfbyte) && (halfbyte <= 9)) {
					buf.append((char) ('0' + halfbyte));
				} else {
					buf.append((char) ('a' + (halfbyte - 10)));
				}
				halfbyte = element & 0x0F;
			} while (two_halfs++ < 1);
		}
		return buf.toString();
	}

	public static String SHA(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest md;
		md = MessageDigest.getInstance("SHA");
		byte[] md5hash = new byte[32];
		md.update(text.getBytes("iso-8859-1"), 0, text.length());
		md5hash = md.digest();
		return convertToHex(md5hash);
	}

	public static String MD5(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest md;
		md = MessageDigest.getInstance("MD5");
		byte[] md5hash = new byte[32];
		md.update(text.getBytes("iso-8859-1"), 0, text.length());
		md5hash = md.digest();
		return convertToHex(md5hash);
	}

	public enum IntegrationReturns {
		CORRECT, NON_EXISTANT_USERNAME, WRONG_CREDENTIALS, SQL_ERROR
	}

	/**
	 * Claims the gold points the player has purchased from the sql database
	 * 
	 * @param player
	 *            The player
	 */
	@SuppressWarnings("deprecation")
	public static void claimGoldPoints(Player player) {
		DatabaseConnection connection = World.getConnectionPool().nextFree();
		String name = player.getUsername();
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM purchases WHERE delivered='0' AND acc='" + name.replaceAll("_", " ") + "';");
			boolean found = false;
			while (rs.next()) {
				double price = Double.parseDouble(rs.getString(3));
				connection.createStatement().executeUpdate("UPDATE `purchases` SET delivered='1' WHERE delivered='0' AND `acc`='" + name.replaceAll("_", " ") + "';");

				int goldPointsPurchased = (int) Math.ceil(100 * price);
				player.getFacade().rewardCoins(goldPointsPurchased);
				player.getFacade().setTotalPointsPurchased((long) (player.getFacade().getTotalPointsPurchased() + price));

				player.updateMembershipGroup();
				player.getForumGroups().clear();
				player.addForumGroups();

				player.getDialogueManager().startDialogue(SimpleMessage.class, "You have just received " + goldPointsPurchased + " gold points.", "Exchange them at Party Pete in the Edgeville bank.", "<col=" + ChatColors.MAROON + ">You have now purchased $" + player.getFacade().getTotalPointsPurchased() + " in gold points.", "Thank you for supporting the server!");

				exportDonationRecord("[" + new Date().toLocaleString() + "] " + player.getDisplayName() + " has purchased Gold Points for $" + Utils.format((long) price) + ".");
				World.sendWorldMessage(player.getDisplayName() + " has just purchased gold points! Thank them for their generosity", false, false);
				found = true;
			}
			if (!found) {
				player.sendMessage("You have no gold points to claim.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (connection != null)
				connection.returnConnection();
		}
	}

	private static void exportDonationRecord(String data) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("info/script/membership.txt", true));
			writer.write(data + "\n");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}