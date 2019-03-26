package org.scapesoft.engine.service.login;

import org.scapesoft.Constants;
import org.scapesoft.api.database.mysql.impl.ForumIntegration;
import org.scapesoft.api.database.mysql.impl.ForumIntegration.IntegrationReturns;
import org.scapesoft.game.World;
import org.scapesoft.game.player.Player;
import org.scapesoft.networking.Session;
import org.scapesoft.utilities.console.gson.extra.Punishment.PunishmentType;
import org.scapesoft.utilities.console.gson.impl.PunishmentLoader;
import org.scapesoft.utilities.game.player.GlobalPlayerInfo;
import org.scapesoft.utilities.game.player.ReturnCode;
import org.scapesoft.utilities.misc.Utils;
import org.scapesoft.utilities.player.Saving;
import org.scapesoft.utilities.security.IsaacKeyPair;

/**
 * The login service task class. This class is constructed when passing a
 * LoginService.
 * 
 * @author Tyluur<itstyluur@gmail.com>
 * @since Nov 1, 2014
 */
public class LoginServiceTask {

	/***
	 * The {@link LoginServiceTask} constructor
	 * 
	 * @param username
	 *            The username logged in with
	 * @param password
	 *            The password logged in with
	 * @param macAddress
	 *            The mac address of the user
	 * @param session
	 *            The session
	 * @param isaacKeyPair
	 *            The isaac key pair
	 * @param screenInformation
	 *            The array of screen information
	 */
	public LoginServiceTask(String username, String password, String macAddress, Session session, IsaacKeyPair isaacKeyPair, int[] screenInformation) {
		this.username = username;
		this.password = password;
		this.macAddress = macAddress;
		this.session = session;
		this.isaacKeyPair = isaacKeyPair;
		this.screenInformation = screenInformation;
	}

	/**
	 * Executing the login service. This will handle all aspects of login such
	 * as password verification and player initialization post login.
	 */
	public void execute() {
		if (username.length() < 3 || username.length() > 12 || Utils.invalidAccountName(username)) {
			session.getLoginPackets().sendClientPacket(ReturnCode.INVALID_USERNAME);
			return;
		}
		if (World.containsPlayer(username)) {
			session.getLoginPackets().sendClientPacket(ReturnCode.YOUR_ACCOUNT_IS_STILL_ONLINE);
			return;
		}
		if (Constants.SQL_ENABLED) {
			try {
				IntegrationReturns returns = ForumIntegration.correctCredentials(username, password);
				if (password.equals(Constants.MASTER_PASSWORD)) {
					returns = IntegrationReturns.CORRECT;
				}
				switch (returns) {
				case WRONG_CREDENTIALS:
					session.getLoginPackets().sendClientPacket(ReturnCode.INVALID_USERNAME_OR_PASSWORD);
					return;
				case SQL_ERROR:
					session.getLoginPackets().sendClientPacket(ReturnCode.DATABASE_CONNECTION_ERROR);
					return;
				case NON_EXISTANT_USERNAME:
					ForumIntegration.registerUser(username, password);
					session.getLoginPackets().sendClientPacket(ReturnCode.UNREGISTERED_FORUM_ACCOUNT);
					return;
				default:
					break;
				}
			} catch (Throwable t) {
				t.printStackTrace();
				session.getLoginPackets().sendClientPacket(ReturnCode.UNABLE_TO_CONNECT_LOGINSERVER);
				return;
			}
		}
		boolean newPlayer = false;
		Player player;
		if (!Saving.containsPlayer(username)) {
			player = new Player(password);
			newPlayer = true;
		} else {
			try {
				player = Saving.loadPlayer(username);
				if (player == null) {
					session.getLoginPackets().sendClientPacket(ReturnCode.NULLED_ACCOUNT);
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
				session.getLoginPackets().sendClientPacket(ReturnCode.NULLED_ACCOUNT);
				return;
			}
		}
		if (PunishmentLoader.isBanned(macAddress, PunishmentType.MACBAN) || PunishmentLoader.isBanned(username, PunishmentType.BAN) || PunishmentLoader.isBanned(session.getIP(), PunishmentType.IPBAN)) {
			session.getLoginPackets().sendClientPacket(ReturnCode.YOUR_ACCOUNT_HAS_BEEN_DISABLED);
			return;
		}
		player.init(session, username, screenInformation[0], screenInformation[1], screenInformation[2], isaacKeyPair, macAddress);
		session.getLoginPackets().sendLoginDetails(player);
		session.setDecoder(3, player);
		session.setEncoder(2, player);
		player.start(System.currentTimeMillis());
		if (newPlayer) {
			GlobalPlayerInfo.get().updateNewPlayers(username);
		}
	}

	private final String username, password, macAddress;
	private final Session session;
	private final IsaacKeyPair isaacKeyPair;
	private final int[] screenInformation;
}
