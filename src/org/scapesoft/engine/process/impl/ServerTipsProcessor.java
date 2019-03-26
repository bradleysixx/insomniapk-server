package org.scapesoft.engine.process.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.scapesoft.engine.process.TimedProcess;
import org.scapesoft.game.World;
import org.scapesoft.utilities.misc.Utils;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Aug 26, 2014
 */
public class ServerTipsProcessor implements TimedProcess {

	@Override
	public Timer getTimer() {
		return new Timer(10, TimeUnit.MINUTES);
	}

	@Override
	public void execute() {
		if (usedTips.size() >= tips.length - 1)
			usedTips.clear();
		String message = tips[Utils.random(tips.length)];
		while (usedTips.contains(message))
			message = tips[Utils.random(tips.length)];
		sendMessage("<col=4F1880>Tip:<img=9></col> <col=9C4C06>" + message);
		usedTips.add(message);
	}

	private void sendMessage(String message) {
		World.players().forEach(p -> p.sendMessage(message));
	}

	private List<String> usedTips = new ArrayList<String>();

	private String[] tips = {
			"You can use the Spirit Tree for quick teleports.",
			"A lot of useful items can be bought from the grand exchange.",
			"Voting gives you GREAT ingame rewards and access to new features.",
			"Donating helps the server and gives you rewards in return!",
			"View your quest tab to see the quests you can do for access to brand-new content.",
			"You can change your character's appearane with the '?' button.",
			"Akrisae Brother's minigame is a great way to make money!",
			"You can right-click a stat to tell everyone your level in it quickly.",
			"Party Pete has custom shops such as loyalty rewards shop at home!",
			"You get loyalty points which can be exchanged for rewards when you're online.",
			"View your Information Tab to see what staff are online if you need help.",
			"Clans are fully functional! Create one today and invite your friends!",
			"View your Items Kept on Death to see what items you keep before you die!",
			"If you haven't yet, change your email on forums to a real one for security."};

}