package org.scapesoft.game.player.dialogues.impl;

import java.util.concurrent.TimeUnit;

import org.scapesoft.Constants;
import org.scapesoft.api.input.StringInputAction;
import org.scapesoft.game.player.dialogues.ChatAnimation;
import org.scapesoft.game.player.dialogues.Dialogue;
import org.scapesoft.utilities.console.gson.GsonHandler;
import org.scapesoft.utilities.console.gson.impl.DisplayNameLoader;
import org.scapesoft.utilities.misc.Utils;
import org.scapesoft.utilities.misc.Utils.CombatRates;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jul 26, 2014
 */
public class Security_Guard extends Dialogue {

	int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendNPCDialogue(npcId, ChatAnimation.NORMAL, "Hello, how may I be of service to you?");
	}

	@Override
	public void run(int interfaceId, int option) {
		//CombatRates myRate = player.getFacade().getRates();
		switch (stage) {
		case -1:
			sendOptionsDialogue("Select an Option", (player.getFacade().isExpLocked() ? "Unlock" : "Lock") + " Experience", "Purchase Double XP", "Display name options");
			stage = 0;
			break;
		case 0:
			switch (option) {
			case FIRST:
				player.getFacade().setExpLocked(!player.getFacade().isExpLocked());
				sendNPCDialogue(npcId, ChatAnimation.NORMAL, "Your experience is now " + (player.getFacade().isExpLocked() ? "" : "un") + "locked.");
				stage = -2;
				break;
		/*	case SECOND:
				if (myRate.ordinal() <= 2) {
					sendNPCDialogue(npcId, ChatAnimation.NORMAL, "You can't change your experience rate from this.");
					stage = -2;
					return;
				}
				List<String> rateList = new ArrayList<String>();
				rateList.add("Select an Option");
				boolean ignoreLoot = false;
				for (CombatRates rate : CombatRates.values()) {
					if (rate.ordinal() >= myRate.ordinal())
						continue;
					StringBuilder bldr = new StringBuilder();
					bldr.append(Utils.formatPlayerNameForDisplay(rate.name()));
					bldr.append("[x" + rate.getCombat() + " <col=" + ChatColors.BLUE + ">combat</col> ");
					bldr.append("x" + rate.getSkill() + " <col=" + ChatColors.BLUE + ">skill</col>");
					if (ignoreLoot) {
						bldr.append("]");
					} else {
						bldr.append(" " + rate.getLoot() + "% <col=" + ChatColors.BLUE + ">drop rates</col>]");
					}
					rateList.add(bldr.toString());
				}
				sendOptionsDialogue(rateList.toArray(new String[rateList.size()]));
				stage = 1;
				break;*/
			case SECOND:
				if (Constants.isDoubleExp && !(player.isDonator() || player.isSupporter())) {
					sendNPCDialogue(npcId, ChatAnimation.SAD, "The server is already in double experience mode!", "You can purchase triple experience if you are a donator!");
					stage = -1;
				} else {
					if (player.hasDoubleExp()) {
						sendNPCDialogue(npcId, ChatAnimation.SNOBBY, "You already have " + TimeUnit.MINUTES.convert(player.getDoubleExpTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS) + " minutes remaining.");
						stage = -1;
					} else {
						sendNPCDialogue(npcId, ChatAnimation.THINKING, "It will cost you 50M GP for 2 hours", "are you sure you want to do this?");
						stage = 4;
					}
				}
				break;
			case THIRD:
				sendOptionsDialogue("Select an Option", "Remove display name", "Change display name");
				stage = 8;
				break;
			}
			break;
		case 1:
			CombatRates rates = null;
			switch (option) {
			case FIRST:
				rates = CombatRates.EASY;
				break;
			case SECOND:
				rates = CombatRates.NORMAL;
				break;
			case THIRD:
				rates = CombatRates.HARD;
				break;
			case FOURTH:
				rates = CombatRates.LEGEND;
				break;
			case FIFTH:
				rates = CombatRates.ELITE;
				break;
			}
			if (rates != null) {
				sendNPCDialogue(npcId, ChatAnimation.NORMAL, "It will cost you " + Utils.format(XP_RATE_CHANGE_COST) + " GP to change your rate", "are you sure you want to do this?");
				player.getAttributes().put("experience_rate_switch", rates);
				stage = 2;
			} else {
				end();
			}
			break;
		case 2:
			sendOptionsDialogue("Select an Option", "Yes", "No");
			stage = 3;
			break;
		case 3:
			if (player.takeMoney(XP_RATE_CHANGE_COST)) {
				player.getFacade().setModifiers((CombatRates) player.getAttributes().get("experience_rate_switch"));
				sendPlayerDialogue(ChatAnimation.NORMAL, "Thank you! I'll enjoy " + player.getFacade().getRates().name().toLowerCase() + " experience rates now.");
			} else {
				sendNPCDialogue(npcId, ChatAnimation.SAD, "You don't have enough money to pay for this.");
			}
			stage = -2;
			break;
		case 4:
			sendOptionsDialogue("Are you sure?", "Yes", "No");
			stage = 5;
			break;
		case 5:
			switch (option) {
			case FIRST:
				if (player.takeMoney(DOUBLE_XP_COST)) {
					player.setDoubleExpTime(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(2));
					sendPlayerDialogue(ChatAnimation.NORMAL, "Thank you! I'll enjoy double experience rates now.");
				} else
					sendNPCDialogue(npcId, ChatAnimation.SAD, "You don't have enough money to pay for this.");
				break;
			default:
				end();
				break;
			}
			break;
		case 6:
			sendOptionsDialogue("Are you sure?", "Yes", "No");
			stage = 7;
			break;
		case 7:
			switch (option) {
			case FIRST:
				player.getPackets().sendInputNameScript("Enter Name:", new StringInputAction() {

					@Override
					public void handle(String input) {
						if (player.takeMoney(NAME_CHANGE_COST)) {
							input = input.replaceAll("_", " ").trim();
							if (Utils.invalidAccountName(input)) {
								stage = -2;
								player.getInventory().addItem(995, NAME_CHANGE_COST);
								sendNPCDialogue(npcId, ChatAnimation.NORMAL, "The entered name is invalid, please try again.");
							} else {
								String exister = GsonHandler.<DisplayNameLoader> getJsonLoader(DisplayNameLoader.class).displayNameExists(input);
								if (exister == null) {
									GsonHandler.<DisplayNameLoader> getJsonLoader(DisplayNameLoader.class).setDisplayName(player.getUsername(), input);
									player.setDisplayName(input);
									player.setChangedDisplay(true);
									player.getAppearence().generateAppearenceData();
									sendPlayerDialogue(ChatAnimation.NORMAL, "Thank you!");
									stage = -2;
								} else {
									stage = -2;
									player.getInventory().addItem(995, NAME_CHANGE_COST);
									sendNPCDialogue(npcId, ChatAnimation.NORMAL, "That name is already taken by " + exister + ".");
								}
							}
						} else {
							sendNPCDialogue(npcId, ChatAnimation.SAD, "You don't have enough money to pay for this.");
							stage = -2;
						}
					}
				});
				break;
			default:
				end();
				break;
			}
			break;
		case 8:
			switch (option) {
			case FIRST:
				sendNPCDialogue(npcId, ChatAnimation.NORMAL, "Your display name has been removed.");
				player.setChangedDisplay(false);
				player.getAppearence().generateAppearenceData();
				stage = -2;
				break;
			case SECOND:
				sendNPCDialogue(npcId, ChatAnimation.NORMAL, "It costs " + Utils.format(NAME_CHANGE_COST) + " coins to change your display name.", "Are you sure?");
				stage = 6;
				break;
			}
			break;
		default:
			break;
		}
	}

	@SuppressWarnings("unused")
	private void changeName() {
		if (player.getFacade().getLastDisplayNameChange() == -1 || TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - player.getFacade().getLastDisplayNameChange()) >= 7) {
			player.getPackets().sendInputNameScript("Enter Name:", new StringInputAction() {

				@Override
				public void handle(String input) {
					player.setDisplayName(input);
					player.getFacade().setLastDisplayNameChange(System.currentTimeMillis());
					player.getAppearence().generateAppearenceData();
					end();
				}
			});
		} else {
			long days = player.getFacade().getLastDisplayNameChange() + TimeUnit.DAYS.toMillis(7);
			long hours = TimeUnit.MILLISECONDS.toHours(days - System.currentTimeMillis());
			String timeInfo = "";
			if (hours > 1)
				timeInfo = "Hours Left: " + hours;
			else {
				long minutes = TimeUnit.MILLISECONDS.toMinutes(days - System.currentTimeMillis());
				timeInfo = "Minutes Left: " + minutes;
			}
			sendDialogue("You cannot change your display name yet!", timeInfo);
			stage = -2;
		}
	}

	@Override
	public void finish() {
	}

	private static final int XP_RATE_CHANGE_COST = 20_000_000;
	private static final int DOUBLE_XP_COST = 50_000_000;
	private static final int NAME_CHANGE_COST = 5_000_000;

}