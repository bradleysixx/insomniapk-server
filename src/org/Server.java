package org;

import org.scapesoft.Constants;
import org.scapesoft.engine.CoresManager;
import org.scapesoft.engine.game.GameLoader;
import org.scapesoft.game.World;
import org.scapesoft.networking.ServerChannelHandler;
import org.scapesoft.utilities.console.logging.LoggerSetup;
import org.scapesoft.utilities.misc.Config;
import org.scapesoft.utilities.misc.Stopwatch;
import org.scapesoft.utilities.misc.Utils;
import org.scapesoft.utilities.player.Saving;

public class Server {

	public static void main(String[] args) {
		try {
			Config.get().load();
			LoggerSetup.registerServerLoggers();
			System.out.println("Executing game server!");
			GameLoader.get().getBackgroundLoader().waitForPendingTasks().shutdown();
			System.out.println("Server loaded in " + STOPWATCH.elapsed() + "ms with SQL " + (Constants.SQL_ENABLED ? "en" : "dis") + "abled.");
			System.err.println(Utils.getItemDefinitionsSize());
			STARTUP_TIME = System.currentTimeMillis();
			RESTART_TIME = STARTUP_TIME + Constants.AUTOMATIC_RESTART_TIME;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void shutdown() {
		closeServices();
		System.out.println("Shutting down...");
	}

	public static void closeServices() {
		System.out.println("Shutting down server channel...");
		ServerChannelHandler.shutdown();
		System.out.println("Shutting down cores manager...");
		CoresManager.shutdown();
	}

	public static void restart() {
		System.out.println("Saving players...");
		World.players().forEach((p) -> Saving.savePlayer(p)); 
		System.out.println("Saved all players!");
		System.out.println("Closing services....");
		closeServices();
		System.out.println("Services closed! Now exiting...");
		System.exit(-1);
	}

	/**
	 * The time when the server is fully prepared
	 */
	public static long STARTUP_TIME = -1;
	public static long RESTART_TIME;

	/**
	 * The stopwatch that is clocked when the server starts
	 */
	public static final Stopwatch STOPWATCH = Stopwatch.start();
	
}