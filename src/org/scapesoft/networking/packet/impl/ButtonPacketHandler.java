package org.scapesoft.networking.packet.impl;

import org.scapesoft.game.player.Player;
import org.scapesoft.networking.codec.handlers.ButtonHandler;
import org.scapesoft.networking.codec.stream.InputStream;
import org.scapesoft.networking.packet.PacketHandler;
import org.scapesoft.networking.packet.PacketInformation;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jun 19, 2014
 */
@PacketInformation(listeners = "61,64,4,52,81,18,10,25,91,20")
public class ButtonPacketHandler extends PacketHandler {

	@Override
	public void handle(Player player, Integer packetId, Integer length, InputStream stream) {
		ButtonHandler.handleButtons(player, stream, packetId);
	}

}