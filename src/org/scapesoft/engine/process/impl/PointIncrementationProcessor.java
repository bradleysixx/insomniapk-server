package org.scapesoft.engine.process.impl;

import java.util.concurrent.TimeUnit;

import org.scapesoft.engine.process.TimedProcess;
import org.scapesoft.game.World;

/**
 * @author Tyluur<itstyluur@gmail.com
 * @since Oct 25, 2014
 */
public class PointIncrementationProcessor implements TimedProcess {

	@Override
	public Timer getTimer() {
		return new Timer(1, TimeUnit.SECONDS);
	}

	@Override
	public void execute() {
		try {
			World.players().forEach(p -> {
				Long lastTime = (Long) p.getAttributes().get("last_real_packet_time");
				/**
				 * If the player has sent the packets that are recognized as
				 * typing/clicking, the last_real_packet_time is set to the
				 * current time. We can then check for the differences in the
				 * current time and the time the player sent the packet. If the
				 * difference is greater than {@link #IDLE_TIME_ALLOWED}, we will
				 * not increment their playtime
				 */
				if (lastTime != null && TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - lastTime) <= IDLE_TIME_ALLOWED) {
					p.setSecondsPlayed(p.getSecondsPlayed() + 1);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * The amount of time the player is allowed to idle in which they will still
	 * receive playtime
	 */
	private static final int IDLE_TIME_ALLOWED = 30;

}
