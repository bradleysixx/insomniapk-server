package org.scapesoft.engine.game;

import java.io.IOException;
import java.util.concurrent.Executors;

import org.scapesoft.api.event.EventManager;
import org.scapesoft.api.event.command.CommandHandler;
import org.scapesoft.cache.Cache;
import org.scapesoft.cache.loaders.ItemEquipIds;
import org.scapesoft.engine.BlockingExecutorService;
import org.scapesoft.engine.CoresManager;
import org.scapesoft.engine.process.ProcessManagement;
import org.scapesoft.engine.service.login.LoginService;
import org.scapesoft.game.RegionBuilder;
import org.scapesoft.game.World;
import org.scapesoft.game.npc.combat.CombatScriptsHandler;
import org.scapesoft.game.player.actions.mining.Mining;
import org.scapesoft.game.player.clans.ClansManager;
import org.scapesoft.game.player.content.FishingSpotsHandler;
import org.scapesoft.game.player.content.FriendChatsManager;
import org.scapesoft.game.player.content.SkillScrolls;
import org.scapesoft.game.player.content.achievements.AchievementManager;
import org.scapesoft.game.player.content.randoms.RandomEventManager;
import org.scapesoft.game.player.content.scrolls.ScrollSystem;
import org.scapesoft.game.player.content.wild.WildernessActivityManager;
import org.scapesoft.game.player.controlers.ControllerHandler;
import org.scapesoft.game.player.cutscenes.CutscenesHandler;
import org.scapesoft.game.player.dialogues.DialogueHandler;
import org.scapesoft.game.player.quests.QuestManager;
import org.scapesoft.networking.ServerChannelHandler;
import org.scapesoft.networking.management.query.ServerQueryHandler;
import org.scapesoft.networking.packet.PacketSystem;
import org.scapesoft.utilities.console.gson.GsonHandler;
import org.scapesoft.utilities.game.cache.MapAreas;
import org.scapesoft.utilities.game.cache.MapXTEA;
import org.scapesoft.utilities.game.item.ItemExamines;
import org.scapesoft.utilities.game.item.ItemNames;
import org.scapesoft.utilities.game.npc.NPCCombatDefinitionsL;
import org.scapesoft.utilities.game.npc.NPCExamines;
import org.scapesoft.utilities.game.npc.Nonmoving;
import org.scapesoft.utilities.game.object.ObjectRemoval;
import org.scapesoft.utilities.game.player.Censor;
import org.scapesoft.utilities.misc.DateManager;
import org.scapesoft.utilities.security.Huffman;

/**
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since Feb 27, 2014
 */
public class GameLoader {

	public GameLoader() {
		load();
	}

	/**
	 * The getter
	 *
	 * @return
	 */
	public static GameLoader get() {
		return LOADER;
	}

	public BlockingExecutorService getBackgroundLoader() {
		return backgroundLoader;
	}

	/**
	 * An executor service which handles background loading tasks.
	 */
	private final BlockingExecutorService backgroundLoader = new BlockingExecutorService(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));

	/**
	 * Loads everything here
	 *
	 * @throws IOException
	 */
	public void load() {
		/** Setting the server clock time */
		DateManager.get().setTime();
		try {
			Cache.init();
			CoresManager.init();
			ServerChannelHandler.init();
			World.init();
		} catch (IOException e) {
			e.printStackTrace();
		}
		getBackgroundLoader().submit(() -> {
			ItemEquipIds.init();
			ItemNames.loadNames();
			Huffman.init();
			return null;
		});
		getBackgroundLoader().submit(() -> {
			RandomEventManager.get().initialize();
			ScrollSystem.get().load();
			Mining.load();
			return null;
		});
		getBackgroundLoader().submit(() -> {
			MapXTEA.init();
			MapAreas.init();
			return null;
		});
		getBackgroundLoader().submit(() -> {
			WildernessActivityManager.getSingleton().load();
			return null;
		});
		getBackgroundLoader().submit(() -> {
			FriendChatsManager.init();
			ClansManager.init();
			NPCCombatDefinitionsL.init();
			Nonmoving.loadList();
			PacketSystem.load();
			return null;
		});
		getBackgroundLoader().submit(() -> {
			ItemExamines.init();
			try {
				NPCExamines.loadExamines();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		});
		getBackgroundLoader().submit(() -> {
			QuestManager.load();
			AchievementManager.load();
			return null;
		});
		getBackgroundLoader().submit(() -> {
			FishingSpotsHandler.init();
			CombatScriptsHandler.init();
			DialogueHandler.init();
			ControllerHandler.init();
			CutscenesHandler.init();
			Censor.init();
			return null;
		});
		getBackgroundLoader().submit(() -> {
			RegionBuilder.init();
			return null;
		});
		getBackgroundLoader().submit(() -> {
			CommandHandler.get().initialize();
			ProcessManagement.get().registerEvents();
			ObjectRemoval.initialize();
			return null;
		});
		getBackgroundLoader().submit(() -> {
			EventManager.get().load();
			LoginService.getSingleton().init();
			ServerQueryHandler.load();
			SkillScrolls.get().load();
			GsonHandler.initialize();
			return null;
		});
	}

	/**
	 * The instance of the loader
	 */
	private static final GameLoader LOADER = new GameLoader();

}