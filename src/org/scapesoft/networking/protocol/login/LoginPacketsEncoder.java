package org.scapesoft.networking.protocol.login;

import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.scapesoft.game.player.Player;
import org.scapesoft.networking.Session;
import org.scapesoft.networking.codec.Encoder;
import org.scapesoft.networking.codec.stream.OutputStream;
import org.scapesoft.utilities.game.player.ReturnCode;

public final class LoginPacketsEncoder extends Encoder {

	public LoginPacketsEncoder(Session connection) {
		super(connection);
	}

	public final void sendStartUpPacket() {
		OutputStream stream = new OutputStream(1);
		stream.writeByte(0);
		session.write(stream);
	}
	
	public final void sendClientPacket(Object object) {
		OutputStream stream = new OutputStream(1);
		stream.writeByte((object instanceof ReturnCode) ? ((ReturnCode) object).getOpcode() : (Integer) object);
		ChannelFuture future = session.write(stream);
		if (future != null) {
			future.addListener(ChannelFutureListener.CLOSE);
		} else {
			session.getChannel().close();
		}
	}

	public final void sendLoginDetails(Player player) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarByte(player, 2);
		stream.writeByte(player.getRights());
		stream.writeByte(0);
		stream.writeByte(0);
		stream.writeByte(0);
		stream.writeByte(1);
		stream.writeByte(0);
		stream.writeShort(player.getIndex());
		stream.writeByte(1);
		stream.write24BitInteger(0);
		stream.writeByte(1);
		stream.writeString(player.getDisplayName());
		stream.endPacketVarByte();
		session.write(stream);
	}

}
