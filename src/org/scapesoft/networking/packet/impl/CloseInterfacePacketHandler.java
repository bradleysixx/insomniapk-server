package org.scapesoft.networking.packet.impl;

import org.scapesoft.game.player.Player;
import org.scapesoft.networking.codec.stream.InputStream;
import org.scapesoft.networking.packet.PacketHandler;
import org.scapesoft.networking.packet.PacketInformation;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jun 19, 2014
 */
@PacketInformation(listeners = "56")
public class CloseInterfacePacketHandler extends PacketHandler {

	@Override
	public void handle(Player player, Integer packetId, Integer length, InputStream stream) {
		if (!player.isRunning()) {
			player.run();
			return;
		}
		player.stopAll();
	}

}
