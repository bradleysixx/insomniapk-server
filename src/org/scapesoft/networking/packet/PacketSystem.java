package org.scapesoft.networking.packet;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.scapesoft.utilities.misc.FileClassLoader;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jun 19, 2014
 */
public class PacketSystem {

	/**
	 * Loads all packet handlers
	 */
	public static void load() {
		FileClassLoader.getClassesInDirectory(PacketSystem.class.getPackage().getName() + ".impl").stream().forEach((packet) -> {
			PacketHandler skeleton = (PacketHandler) packet;
			if (packet.getClass().getAnnotations().length == 0)
				throw new RuntimeException(packet.getClass() + " has no @PacketInfo attribute.");
			Stream.of(packet.getClass().getAnnotations()).filter((a) -> a instanceof PacketInformation).forEach((a) -> {
				PacketInformation info = (PacketInformation) a;
				String listeners = info.listeners();
				String[] split = listeners.split(",");
				for (String listener : split) {
					Integer packetId = Integer.valueOf(listener);
					LISTENERS.put(packetId, skeleton);
				}
			});
		});
	}

	/**
	 * The array of loaded packets
	 */
	private final static Map<Integer, PacketHandler> LISTENERS = new HashMap<>();

	/**
	 * Gets the packet handler by the packet id
	 * 
	 * @param packetId
	 *            The packet id
	 * @return
	 */
	public static PacketHandler getHandler(int packetId) {
		PacketHandler handler = LISTENERS.get(packetId);
		return handler;
	}

}
