package org.scapesoft.utilities.misc;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jboss.netty.channel.Channel;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Nov 21, 2014
 */
public class SessionLimiter {

	/**
	 * Getting the amount of connections for our channel
	 * 
	 * @param channel
	 *            The channel connected to us
	 * @return
	 */
	public static Integer getConnections(Channel channel) {
		Integer count = CURRENT_CONNECTIONS.get(Utils.getIP(channel));
		return count == null ? 0 : count;
	}

	/**
	 * Adds the connection to the map
	 * 
	 * @param channel
	 */
	public static void addConnection(Channel channel) {
		addIp(Utils.getIP(channel));
	}

	/**
	 * Removes the connection from the map
	 * 
	 * @param channel
	 */
	public static void removeConnection(Channel channel) {
		CURRENT_CONNECTIONS.remove(Utils.getIP(channel));
	}

	/**
	 * Adding the ip to the map if it doesn't yet exist. If it does exist, the
	 * connections count will increase by 1
	 * 
	 * @param ip
	 *            The ip
	 */
	private static void addIp(String ip) {
		Integer count = CURRENT_CONNECTIONS.get(ip);
		if (count == null) {
			CURRENT_CONNECTIONS.put(ip, 1);
		} else {
			CURRENT_CONNECTIONS.replace(ip, count + 1);
		}
	}

	/**
	 * The map of session connected to us
	 */
	private static final Map<String, Integer> CURRENT_CONNECTIONS = Collections.synchronizedMap(new HashMap<>());

}
