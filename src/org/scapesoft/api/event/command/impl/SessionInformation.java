package org.scapesoft.api.event.command.impl;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.api.event.listeners.interfaces.Scrollable;
import org.scapesoft.game.World;
import org.scapesoft.game.player.Player;
import org.scapesoft.networking.Session;
import org.scapesoft.utilities.game.player.Rights;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Dec 3, 2014
 */
public class SessionInformation extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.OWNER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "getsession" };
	}

	@Override
	public void execute(Player player) {
		Player target = World.getPlayer(getCompleted(cmd, 1));
		if (target == null) {
			player.sendMessage("No such player... try again.");
			return;
		}
		String info = new String();
		Session session = target.getSession();
		info += "[ip=" + session.getIP();
		info += ", decoder=" + (session.getDecoder() == null ? "null" : session.getDecoder().getClass().getSimpleName());
		info += ", isWritable=" + session.getChannel().isWritable();
		info += ", isOpen=" + session.getChannel().isOpen();
		info += ", isBound=" + session.getChannel().isBound();
		info += ", isConnected=" + session.getChannel().isConnected();
		info += ", isReadable=" + session.getChannel().isReadable();
		info += ", player=" + (session.getPlayer() != null ? session.getPlayer().getUsername() : "null");
		info += "]";
		System.out.println(info);
		Scrollable.sendScroll(player, "Session Information", info);
	}

}
