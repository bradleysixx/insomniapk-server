package org.scapesoft.game.player.content;

import java.io.Serializable;

import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.misc.Utils;

public final class BankVault implements Serializable {

	private static final long serialVersionUID = -8586209855654889906L;

	private final Player player;
	private long currentAmt, vaultLimit;

	public BankVault(Player player) {
		this.player = player;
	}

	public void addMoney(long toAdd) {
		this.currentAmt += toAdd;
	}

	public void withdrawMoney(long toRemove) {
		if (toRemove > currentAmt)
			toRemove = currentAmt;
		if ((toRemove + player.getInventory().getCoinsAmount()) > Integer.MAX_VALUE)
			toRemove = Integer.MAX_VALUE - (player.getInventory().getCoinsAmount() + toRemove);

		this.currentAmt -= toRemove;
		player.getInventory().addItem(995, (int) toRemove);
		resendMessage(false);
	}

	public void upgradeBankVault(int amount) {
		vaultLimit += amount * 20;
		resendMessage(true);
	}

	public void resendMessage(boolean upgrade) {
		String word = upgrade ? "holds" : "contains";
		long amount = upgrade ? vaultLimit : currentAmt;
		player.sendMessage("Your bank vault now " + word + " " + Utils.format(amount) + " coins!");
	}

}