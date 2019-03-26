package org.scapesoft.networking.management;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.scapesoft.Constants;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Nov 21, 2014
 */
public class ServerManagement {

	private ServerManagement() {
		bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool(), 1));
	}

	/**
	 * Starts the listening process on port {@value #PORT}
	 */
	public void listen() {
		bootstrap.setPipelineFactory(new ServerManagementFactory());
		bootstrap.setOption("reuseAddress", true);
		bootstrap.setOption("child.tcpNoDelay", true);
		bootstrap.setOption("child.connectTimeoutMillis", Constants.CONNECTION_TIMEOUT);
		bootstrap.setOption("child.TcpAckFrequency", true);
		bootstrap.bind(new InetSocketAddress(PORT));
		System.out.println("Listening on port " + PORT);
	}

	/**
	 * The getter of this class instasnce
	 * @return
	 */
	public static ServerManagement getInstance() {
		return INSTANCE;
	}

	/**
	 * The bootstrap
	 */
	private ServerBootstrap bootstrap;

	/**
	 * The server management instance
	 */
	private static final ServerManagement INSTANCE = new ServerManagement();

	/**
	 * The port we listen on
	 */
	private static final int PORT = 5555;

}
