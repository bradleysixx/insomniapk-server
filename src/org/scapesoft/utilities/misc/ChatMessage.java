package org.scapesoft.utilities.misc;

import org.scapesoft.game.player.QuickChatMessage;
import org.scapesoft.utilities.game.player.Censor;

public class ChatMessage {

	private String message;
	private String filteredMessage;

	public ChatMessage(String message) {
		if (!(this instanceof QuickChatMessage)) {
			filteredMessage = Censor.getFilteredMessage(message);
			this.message = Utils.fixChatMessage(message);
		} else
			this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public String getMessage(boolean filtered) {
		if (this instanceof QuickChatMessage)
			return message;
		return filtered ? filteredMessage : message;
	}

}