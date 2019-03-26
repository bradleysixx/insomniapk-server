package org.scapesoft.api.event.command.impl;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.engine.CoresManager;
import org.scapesoft.game.RegionBuilder;
import org.scapesoft.game.World;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.tasks.WorldTask;
import org.scapesoft.game.tasks.WorldTasksManager;
import org.scapesoft.utilities.game.player.Rights;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Mar 23, 2014
 */
public class LoopInterface extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.OWNER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[]{"loopi"};
	}

	@Override
	public void execute(final Player player) {
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				boolean stop = player.getTemporaryAttributtes().remove("stop_loop") != null;
				if (stop || !World.containsPlayer(player.getUsername())) {
					stop();
				} else {
/*					player.sendMessage("Sent interface: " + i);
					System.out.println("Sent interface: " + i);
					player.getPackets().sendHideIComponent(762, i, true);
					i++;*/
					CoresManager.slowExecutor.execute(new Runnable() {
						@Override
						public void run() {
							// finds empty map bounds
							int[] boundChuncks = RegionBuilder.findEmptyChunkBound(8, 8);
							// copys real map into the empty map
							// 552 640
							RegionBuilder.copyAllPlanesMap(302, 639, boundChuncks[0], boundChuncks[1], 64);
							RegionBuilder.copyAllPlanesMap(296, 632, boundChuncks[0], boundChuncks[1], 64);
						}
					});
				}
			}
		}, 3, 1);
	}

}
