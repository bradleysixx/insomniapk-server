package org.scapesoft.networking.packet;

import org.scapesoft.game.player.Player;
import org.scapesoft.networking.codec.stream.InputStream;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jun 19, 2014
 */
public abstract class PacketHandler {

	/**
	 * Handles an incoming packet
	 * 
	 * @param player
	 *            The player
	 * @param packetId
	 *            The packet id
	 * @param length
	 *            The length of the packet
	 * @param stream
	 *            The packet stream
	 */
	public abstract void handle(Player player, Integer packetId, Integer length, InputStream stream);

}
