package org.scapesoft.networking;

import java.net.InetSocketAddress;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.scapesoft.Constants;
import org.scapesoft.engine.CoresManager;
import org.scapesoft.game.player.Player;
import org.scapesoft.networking.codec.stream.InputStream;
import org.scapesoft.networking.management.ServerManagement;
import org.scapesoft.utilities.misc.SessionLimiter;
import org.scapesoft.utilities.misc.Utils;

public final class ServerChannelHandler extends SimpleChannelUpstreamHandler {

	public static final void init() {
		new ServerChannelHandler();
	}

	private ServerChannelHandler() {
		bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(CoresManager.serverBossChannelExecutor, CoresManager.serverWorkerChannelExecutor, CoresManager.serverWorkersCount));
		bootstrap.getPipeline().addLast("handler", this);

		bootstrap.setOption("reuseAddress", true);
		bootstrap.setOption("child.tcpNoDelay", true);
		bootstrap.setOption("child.connectTimeoutMillis", Constants.CONNECTION_TIMEOUT);
		bootstrap.setOption("child.TcpAckFrequency", true);
		bootstrap.bind(new InetSocketAddress(Constants.PORT_ID));
		ServerManagement.getInstance().listen();
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		if (SessionLimiter.getConnections(e.getChannel()) >= 10) {
			System.err.println("Several connections from " + Utils.getIP(e.getChannel()) + "! Stopping them...");
			e.getChannel().close();
			return;
		}
		ctx.setAttachment(new Session(e.getChannel()));
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		Object sessionObject = ctx.getAttachment();
		if (sessionObject != null && sessionObject instanceof Session) {
			Session session = (Session) sessionObject;
			Player player = session.getPlayer();
			if (player != null) {
				player.finish();
			}
		}
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		try {
			if (!(e.getMessage() instanceof ChannelBuffer))
				return;
			Object sessionObject = ctx.getAttachment();
			if (sessionObject != null && sessionObject instanceof Session) {
				Session session = (Session) sessionObject;
				if (session.getDecoder() == null)
					return;
				ChannelBuffer buf = (ChannelBuffer) e.getMessage();
				buf.markReaderIndex();
				int avail = buf.readableBytes();
				if (avail < 1 || avail > Constants.RECEIVE_DATA_LIMIT) {
					System.out.println("Avail is: " + avail);
					return;
				}
				byte[] buffer = new byte[buf.readableBytes()];
				buf.readBytes(buffer);
				try {
					session.getDecoder().decode(new InputStream(buffer));
				} catch (Throwable er) {
					er.printStackTrace();
				}
			}
		} catch (Throwable t) {
			e.getChannel().close();
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
		// e.getCause().printStackTrace();
	}

	@Override
	public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) {
		channels.add(e.getChannel());
		SessionLimiter.addConnection(e.getChannel());
	}

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) {
		channels.remove(e.getChannel());
		SessionLimiter.removeConnection(e.getChannel());
	}

	public static final void shutdown() {
		bootstrap.releaseExternalResources();
	}

	/**
	 * The {@code ServerBootstrap} to use.
	 */
	private static ServerBootstrap bootstrap;
	public static ChannelGroup channels;

}