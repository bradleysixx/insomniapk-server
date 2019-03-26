package org.scapesoft.game.player;

import org.scapesoft.utilities.misc.ChatMessage;

public class PublicChatMessage extends ChatMessage {

	private int effects;

	public PublicChatMessage(String message, int effects) {
		super(message);
		this.effects = effects;
	}

	public int getEffects() {
		return effects;
	}

}
