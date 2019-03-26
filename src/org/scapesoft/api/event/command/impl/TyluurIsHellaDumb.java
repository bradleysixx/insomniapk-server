package org.scapesoft.api.event.command.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.scapesoft.api.database.DatabaseConnection;
import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.World;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.dialogues.SimpleMessage;
import org.scapesoft.utilities.game.player.Rights;
import org.scapesoft.utilities.misc.ChatColors;

public final class TyluurIsHellaDumb extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.OWNER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "masteryi" };
	}

	@Override
	public void execute(Player player) {
		String name = getCompleted(cmd, 1).replaceAll("_", " ");
		Player target = World.getPlayerByDisplayName(name);
		if (target == null) {
			player.sendMessage("No such player.");
		}

		DatabaseConnection connection = World.getConnectionPool().nextFree();
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT * FROM purchases WHERE delivered='0' AND acc='"
							+ name.replaceAll("_", " ") + "';");
			boolean found = false;
			while (rs.next()) {
				double price = Double.parseDouble(rs.getString(3));
				connection.createStatement().executeUpdate(
						"UPDATE `purchases` SET delivered='1' WHERE delivered='0' AND `acc`='"
								+ name + "';");

				int goldPointsPurchased = (int) Math.ceil(100 * price);
				player.getFacade().rewardCoins(goldPointsPurchased);
				player.getFacade()
						.setTotalPointsPurchased(
								(long) (player.getFacade()
										.getTotalPointsPurchased() + price));

				player.getAttributes().remove("checked_groups");
				player.getForumGroups().clear();
				player.addForumGroups();

				player.getDialogueManager().startDialogue(
						SimpleMessage.class,
						"You have just received " + goldPointsPurchased
								+ " gold points.",
						"Exchange them at Party Pete in the Edgeville bank.",
						"<col=" + ChatColors.MAROON
								+ ">You have now purchased $"
								+ player.getFacade().getTotalPointsPurchased()
								+ " in gold points.",
						"Thank you for supporting the server!");

				player.sendMessage("He paid");
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

}
