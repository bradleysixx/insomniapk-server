package org.scapesoft.game.player.content;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.scapesoft.api.event.listeners.interfaces.Scrollable;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.Skills;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Nov 1, 2014
 */
public class SkillScrolls {

	/**
	 * Loading all skill scrolls from text files
	 * 
	 * @throws IOException
	 */
	public void load() throws IOException {
		File[] files = new File("./info/scrolls").listFiles();
		for (File file : files) {
			if (file == null || file.isDirectory())
				continue;
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			List<String> text = new ArrayList<String>();
			while ((line = reader.readLine()) != null) {
				text.add(line);
			}
			String name = file.getName().substring(0, file.getName().indexOf("."));
			String title = text.get(0);
			text.remove(0);
			SkillScroll scroll = new SkillScroll(title, text.toArray(new String[text.size()]));
			scrolls.put(name, scroll);
			reader.close();
		}
	}

	/**
	 * Displays the scroll for the player
	 * 
	 * @param player
	 *            The player
	 * @param skillId
	 *            The skill id of the scroll
	 */
	public void displayScroll(Player player, int skillId) {
		String skillName = Skills.SKILL_NAME[skillId];
		SkillScroll scroll = getScrollByName(skillName);
		if (scroll == null) {
			displayScroll(player, scrolls.get("none"));
			return;
		}
		displayScroll(player, scroll);
	}

	/**
	 * Gets a scroll from the map by name
	 * 
	 * @param name
	 *            The name of the scroll
	 * @return
	 */
	private SkillScroll getScrollByName(String name) {
		Iterator<Entry<String, SkillScroll>> it$ = scrolls.entrySet().iterator();
		while (it$.hasNext()) {
			Entry<String, SkillScroll> entry = it$.next();
			if (entry.getKey().equalsIgnoreCase(name)) {
				return entry.getValue();
			}
		}
		return null;
	}

	/**
	 * Displays the scroll
	 * 
	 * @param player
	 *            The player
	 * @param scroll
	 *            The scroll to display
	 */
	private void displayScroll(Player player, SkillScroll scroll) {
		Scrollable.sendScroll(player, scroll.getTitle(), scroll.getText());
	}

	/**
	 * Getting the singleton
	 * 
	 * @return
	 */
	public static SkillScrolls get() {
		return SINGLETON;
	}

	/**
	 * The map of scrolls that information will be pulled from
	 */
	private Map<String, SkillScroll> scrolls = new HashMap<>();

	/**
	 * The instance of the class
	 */
	private static final SkillScrolls SINGLETON = new SkillScrolls();

	private class SkillScroll {

		public SkillScroll(String title, String[] text) {
			this.title = title;
			this.text = text;
		}

		/**
		 * The title of the scroll
		 */
		private final String title;

		/**
		 * The text that is shown on the scroll
		 */
		private final String[] text;

		@Override
		public String toString() {
			return "[title=" + getTitle() + ", text=" + getText() + "]";
		}

		public String getTitle() {
			return title;
		}

		public String[] getText() {
			return text;
		}
	}

}
