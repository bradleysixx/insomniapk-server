package org.scapesoft.utilities.game.player;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.scapesoft.api.database.DatabaseConnection;
import org.scapesoft.game.World;
import org.scapesoft.utilities.console.logging.FileLogger;

/**
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since Aug 11, 2013
 */
public class GlobalPlayerInfo {

	/**
	 * Updates the player count on the website if an update is required
	 */
	public void updateIfNecessary() {
		int players = getPlayersOnline();
		int max = getMaxPlayers();
		if (players > max)
			updateMaxPlayers(players);
	}

	/**
	 * Updates the new players text file with the amount of players in the day.
	 */
	public void updateNewPlayers(String username) {
		List<String> lines = FileLogger.getFileLogger().getFileText("newplayers/");
		List<String> newText = new ArrayList<>();
		newText.add("Amount of new players: " + lines.size());
		newText.add(username);
		for (int i = 0; i < lines.size(); i++) {
			if (i == 0)
				continue;
			String line = lines.get(i);
			if (line.length() > 0) {
				newText.add(line);
			}
		}
		String text = "";
		for (String line : newText) {
			text += line + "\n";
		}
		text = text.trim();
		FileLogger.getFileLogger().writeDatelessLog("newplayers/", text, false);
	}
	
	public void updatePlayersOnline() {
		DatabaseConnection connection = World.getConnectionPool().nextFree();
		try {
			int count = getPlayersOnline();
			Statement stmt = connection.createStatement();
			stmt.execute("UPDATE `players` SET playersOnline='" + count + "'");
			System.out.println("Updated the player count to " + count + " successfully.");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null)
				connection.returnConnection();
		}
	}

	/**
	 * Sends the update query to the database for the player count
	 *
	 * @param newCount
	 *            The player count
	 */
	private void updateMaxPlayers(int newCount) {
		DatabaseConnection connection = World.getConnectionPool().nextFree();
		try {
			Statement stmt = connection.createStatement();
			stmt.executeUpdate("UPDATE `players` SET `maxPlayers` = '" + newCount + "'");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.returnConnection();
			}
		}
	}

	/**
	 * Finds the max amount of players ever on the server from the database
	 *
	 * @return The number
	 */
	public int getMaxPlayers() {
		DatabaseConnection connection = World.getConnectionPool().nextFree();
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM `players` WHERE `maxPlayers` >= 0");
			if (rs.next())
				return rs.getInt("maxPlayers");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null)
				connection.returnConnection();
		}
		return -1;
	}

	/**
	 * Gets the total amount of players online.
	 *
	 * @return
	 */
	public int getPlayersOnline() {
		return World.getPlayers().size();
	}

	/**
	 * @return the instance
	 */
	public static GlobalPlayerInfo get() {
		return INSTANCE;
	}

	private static final GlobalPlayerInfo INSTANCE = new GlobalPlayerInfo();

}