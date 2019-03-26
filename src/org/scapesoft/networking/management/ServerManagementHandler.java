package org.scapesoft.networking.management;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.scapesoft.networking.management.query.ServerQueryHandler;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Nov 21, 2014
 */
public class ServerManagementHandler extends SimpleChannelUpstreamHandler {
	
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		System.out.println(e.getChannel() + " connected");
		sendMessage(e.getChannel(), "Welcome to the Server Management Tool.");
		sendMessage(e.getChannel(), "Type 'commands' to view commands.");
	}
	
	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		System.out.println(e.getChannel() + " disconnected");
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		sendMessage(e.getChannel(), ServerQueryHandler.handleQuery(e.getMessage().toString()));
	}
	
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
    	
    }
    
    private void sendMessage(Channel channel, String message) {
    	channel.write("\t" + message + "\n");
    }

}
