package org.scapesoft.api.event.command.impl;

import java.io.BufferedWriter;
import java.io.FileWriter;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.game.player.Rights;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Dec 25, 2014
 */
public class StopObjectSpawning extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.OWNER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "stos" };
	}

	@Override
	public void execute(Player player) {
		try(BufferedWriter bw = new BufferedWriter(new FileWriter("data/map/nonspawning.txt", true))) {
			bw.append("-1 " + player.getX() + " " + player.getY() + " " + player.getPlane() + "\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
