package org.scapesoft.engine.process.impl;

import java.util.concurrent.TimeUnit;

import org.scapesoft.engine.process.TimedProcess;
import org.scapesoft.game.World;
import org.scapesoft.utilities.player.Saving;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Apr 3, 2014
 */
public class SavingFileProcess implements TimedProcess {

	@Override
	public Timer getTimer() {
		return new Timer(15, TimeUnit.MINUTES);
	}

	@Override
	public void execute() {
		World.players().filter(p -> p.controlerAvailable()).forEach(p -> Saving.savePlayer(p));
	}

}