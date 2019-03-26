package org.scapesoft.engine.game;

import org.scapesoft.Constants;
import org.scapesoft.engine.CoresManager;
import org.scapesoft.game.World;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.tasks.WorldTasksManager;
import org.scapesoft.networking.Session;
import org.scapesoft.utilities.misc.Stopwatch;
import org.scapesoft.utilities.misc.Utils;

/**
 * This class processes all the entities in the game world.
 * 
 * @author Tyluur<itstyluur@gmail.com>
 * @since December 3rd, 2014
 */
public class GameWorker implements Runnable {

	public GameWorker() {
		Thread.currentThread().setName("Game Worker");
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
	}

	@SuppressWarnings("unused")
	@Override
	public final void run() {
		while (!CoresManager.shutdown) {
			final long currentTime = Utils.currentTimeMillis();
			try {
				StringBuilder b = new StringBuilder();
				Stopwatch w = Stopwatch.start();
				WorldTasksManager.processTasks(b);
				b.append("Tasks took: " + w.elapsed() + "ms\n");

				w = Stopwatch.start();
				StringBuilder playersBldr = new StringBuilder();
				for (Player player : World.getPlayers()) {
					if (player == null || !player.hasStarted()) {
						continue;
					}
					Session session = player.getSession();
					boolean first = (player.isActivated() && (!session.getChannel().isWritable()));
					boolean second = (currentTime - player.getPacketsDecoderPing() > Constants.AFK_LOGOUT_DELAY/* && player.getSession().getChannel().isOpen()*/);
					
					if (first || second) {
						player.finish();
						//System.out.println("Booted " + player.getUsername() + "!\t[inactiveChannel=" + first + ", afk_delay=" + second + "]");
					}
					player.processEntity();

				}
				b.append("Process(Player) took: " + w.elapsed() + "ms\n");

				w = Stopwatch.start();
				World.npcs().forEach((n) -> n.processEntity());
				b.append("Process(NPC) took: " + w.elapsed() + "ms\n");

				w = Stopwatch.start();
				for (Player p : World.getPlayers()) {
					if (p != null && p.hasStarted() && !p.hasFinished()) {
						p.getPackets().sendLocalPlayersUpdate();
						p.getPackets().sendLocalNPCsUpdate();
					}
				}
				b.append("Updating took: " + w.elapsed() + "ms\n");

				w = Stopwatch.start();
				World.entities().parallel().forEach((e) -> e.resetMasks());
				b.append("Reset took: " + w.elapsed() + "ms\n");

				setDelay(Utils.currentTimeMillis() - currentTime);
				if (DEBUGGING && delay > 600) {
					System.err.println("=================================================================");
					System.err.println("Engine processing debug!");
					System.err.println(b.toString());
					System.err.println(playersBldr.toString());
					System.err.println("=================================================================");
				}
				long sleepTime = Constants.WORLD_CYCLE_TIME + currentTime - Utils.currentTimeMillis();
				if (sleepTime > 0) {
					Thread.sleep(sleepTime);
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}

	public long getDelay() {
		return delay;
	}

	public void setDelay(long delay) {
		this.delay = delay;
		if (delay > 600) {
			System.err.println("Dangerous game cycle: " + delay + "ms");
		}
	}

	private long delay;
	
	/**
	 * If we shold print debugging errors
	 */
	private static final boolean DEBUGGING = false;

}