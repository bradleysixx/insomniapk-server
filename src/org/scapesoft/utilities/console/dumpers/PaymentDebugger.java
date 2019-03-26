package org.scapesoft.utilities.console.dumpers;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.scapesoft.api.database.ConnectionPool;
import org.scapesoft.api.database.DatabaseConnection;
import org.scapesoft.api.database.configuration.ConfigurationNode;
import org.scapesoft.api.database.configuration.ConfigurationParser;
import org.scapesoft.api.database.mysql.MySQLDatabaseConfiguration;
import org.scapesoft.api.database.mysql.MySQLDatabaseConnection;

public final class PaymentDebugger {

	public static ConnectionPool<MySQLDatabaseConnection> connectionPool;

	public static void main(String[] args) {
		try {
			loadConfiguration();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IOException e1) {
			e1.printStackTrace();
		}
		String name = "good_luck_m8";

		DatabaseConnection connection = connectionPool.nextFree();
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM purchases WHERE delivered='0' AND acc='" + name.replaceAll("_", " ") + "';");
			boolean found = false;
			while (rs.next()) {
				double price = Double.parseDouble(rs.getString(3));
				System.out.println("Price: " + price);
				System.out.println(name);
				connection.createStatement().executeUpdate("UPDATE `purchases` SET delivered='1' WHERE delivered='0' AND `acc`='" + name.replaceAll("_", " ") + "';");

				int goldPointsPurchased = (int) Math.ceil(100 * price);
				System.out.println("They earned " + goldPointsPurchased);

				found = true;
			}
			if (!found) {
				System.out.println("No points!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (connection != null)
				connection.returnConnection();
		}
	}

	private static void loadConfiguration() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		FileInputStream fis = new FileInputStream("data/server.conf");
		try {
			ConfigurationParser parser = new ConfigurationParser(fis);
			ConfigurationNode mainNode = parser.parse();
			if (mainNode.has("database")) {
				ConfigurationNode databaseNode = mainNode.nodeFor("database");
				MySQLDatabaseConfiguration config = new MySQLDatabaseConfiguration();
				config.setHost(databaseNode.getString("host"));
				config.setPort(databaseNode.getInteger("port"));
				config.setDatabase(databaseNode.getString("database"));
				config.setUsername(databaseNode.getString("username"));
				config.setPassword(databaseNode.getString("password"));
				connectionPool = new ConnectionPool<MySQLDatabaseConnection>(config);
			}
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			fis.close();
		}
	}
}
