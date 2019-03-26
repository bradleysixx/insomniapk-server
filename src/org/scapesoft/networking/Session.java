package org.scapesoft.networking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.scapesoft.game.player.Player;
import org.scapesoft.networking.codec.Decoder;
import org.scapesoft.networking.codec.Encoder;
import org.scapesoft.networking.codec.stream.InputStream;
import org.scapesoft.networking.codec.stream.OutputStream;
import org.scapesoft.networking.protocol.ClientPacketsDecoder;
import org.scapesoft.networking.protocol.game.DefaultGameDecoder;
import org.scapesoft.networking.protocol.game.DefaultGameEncoder;
import org.scapesoft.networking.protocol.js5.GrabPacketsDecoder;
import org.scapesoft.networking.protocol.js5.GrabPacketsEncoder;
import org.scapesoft.networking.protocol.login.LoginPacketsDecoder;
import org.scapesoft.networking.protocol.login.LoginPacketsEncoder;

public class Session {

	private Channel channel;
	private Decoder decoder;
	private Encoder encoder;
	private Player player;

	private final List<InputStream> queuedPackets = Collections.synchronizedList(new ArrayList<>());

	public Session(Channel channel) {
		this.channel = channel;
		setDecoder(0);
	}

	public final ChannelFuture write(OutputStream outStream) {
		if (outStream == null || !channel.isConnected())
			return null;
		ChannelBuffer buffer = ChannelBuffers.copiedBuffer(outStream.getBuffer(), 0, outStream.getOffset());
		synchronized (channel) {
			return channel.write(buffer);
		}
	}

	public final ChannelFuture write(ChannelBuffer outStream) {
		if (outStream == null || !channel.isConnected())
			return null;
		synchronized (channel) {
			return channel.write(outStream);
		}
	}

	public final Channel getChannel() {
		return channel;
	}

	public final Decoder getDecoder() {
		return decoder;
	}

	public GrabPacketsDecoder getGrabPacketsDecoder() {
		return (GrabPacketsDecoder) decoder;
	}

	public final Encoder getEncoder() {
		return encoder;
	}

	public void queuePacket(InputStream stream) {
		getQueuedPackets().add(stream);
	}

	public final void setDecoder(int stage) {
		setDecoder(stage, null);
	}

	public final void setDecoder(int stage, Object attachement) {
		switch (stage) {
		case 0:
			decoder = new ClientPacketsDecoder(this);
			break;
		case 1:
			decoder = new GrabPacketsDecoder(this);
			break;
		case 2:
			decoder = new LoginPacketsDecoder(this);
			break;
		case 3:
			decoder = new DefaultGameDecoder(this, (Player) attachement);
			break;
		case -1:
		default:
			decoder = null;
			break;
		}
	}

	public final void setEncoder(int stage) {
		setEncoder(stage, null);
	}

	public final void setEncoder(int stage, Object attachement) {
		switch (stage) {
		case 0:
			encoder = new GrabPacketsEncoder(this);
			break;
		case 1:
			encoder = new LoginPacketsEncoder(this);
			break;
		case 2:
			this.player = (Player) attachement;
			encoder = new DefaultGameEncoder(this, player);
			break;
		case -1:
		default:
			encoder = null;
			break;
		}
	}

	public LoginPacketsEncoder getLoginPackets() {
		return (LoginPacketsEncoder) encoder;
	}

	public GrabPacketsEncoder getGrabPackets() {
		return (GrabPacketsEncoder) encoder;
	}

	public DefaultGameEncoder getWorldPackets() {
		return (DefaultGameEncoder) encoder;
	}

	public String getIP() {
		return channel == null ? "" : channel.getRemoteAddress().toString().split(":")[0].replace("/", "");
	}

	public String getLocalAddress() {
		return channel.getLocalAddress().toString();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Session) {
			Session s = (Session) o;
			if (s.getChannel().getId() == getChannel().getId()) {
				return true;
			}
		}
		return false;
	}

	public List<InputStream> getQueuedPackets() {
		return queuedPackets;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

}
