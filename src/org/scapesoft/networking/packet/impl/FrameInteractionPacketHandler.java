package org.scapesoft.networking.packet.impl;

import org.scapesoft.game.player.Player;
import org.scapesoft.networking.codec.stream.InputStream;
import org.scapesoft.networking.packet.PacketHandler;
import org.scapesoft.networking.packet.PacketInformation;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jun 19, 2014
 */
@PacketInformation(listeners = "84,29,68,75,93")
public class FrameInteractionPacketHandler extends PacketHandler {

	private final static int WINDOW_SWITCH_PACKET = 93;

	@Override
	public void handle(Player player, Integer packetId, Integer length, InputStream stream) {
		if (packetId == 68 || packetId == 84) { // Typing & clicking
			player.getInterfaceManager().setClientActive(true);
			player.setActivated();
		}
		if (packetId == WINDOW_SWITCH_PACKET) {
			int active = stream.readByte();
			player.getInterfaceManager().setClientActive(active == 1);
			return;
		}
	}

}
