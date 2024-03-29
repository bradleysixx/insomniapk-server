package org.scapesoft.utilities.console.gson.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

import org.scapesoft.Constants;
import org.scapesoft.cache.loaders.ItemDefinitions;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.content.exchange.ExchangeOffer;
import org.scapesoft.game.player.content.exchange.ExchangeType;
import org.scapesoft.utilities.console.gson.GsonLoader;
import org.scapesoft.utilities.console.logging.FileLogger;
import org.scapesoft.utilities.misc.ChatColors;

import com.google.gson.reflect.TypeToken;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jun 22, 2014
 */
public class ExchangeItemLoader extends GsonLoader<ExchangeOffer> {

	@Override
	public void initialize() {
		try {
			synchronized (LOCK) {
				//long ms = System.currentTimeMillis();
				if (unlimitedNames == null) {
					try {
						unlimitedNames = (ArrayList<String>) Files.readAllLines(new File("./data/exchange/unlimited.txt").toPath(), Charset.defaultCharset());
					} catch (IOException e) {
						e.printStackTrace();
					}
					List<String> namesList = new ArrayList<>();
					for (String line : unlimitedNames) {
						if (line.startsWith("//"))
							continue;
						String[] split = line.split(": ");
						namesList.add(split[1]);
					}
					unlimitedNames = namesList;
				}
				getExchangeOffers().clear();
				for (String name : unlimitedNames) {
					int item = Integer.parseInt(name);
					// ExchangePriceLoader priceLoader = (ExchangePriceLoader)
					// JsonHandler.getJsonLoader(ExchangePriceLoader.class);
					ExchangeOffer offer = new ExchangeOffer("", item, ExchangeType.SELL, -1, Integer.MAX_VALUE, ExchangePriceLoader.getInfiniteQuantityPrice(item), true);
					getExchangeOffers().add(offer);
				}
				for (ExchangeOffer offer : generateList()) {
					getExchangeOffers().add(offer);
				}
				//System.out.println("Registered " + getExchangeOffers().size() + " exchange offers from " + getFileLocation() + " in " + (System.currentTimeMillis() - ms) + " ms");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the best offer from the list
	 * 
	 * @param type
	 *            The type of item to filter for
	 * @param itemId
	 *            The id of the item you're buying
	 * @return
	 */
	public ExchangeOffer getBestOffer(ExchangeType type, int itemId) {
		synchronized (LOCK) {
			List<ExchangeOffer> sorted = new ArrayList<>();
			for (ExchangeOffer offer : getExchangeOffers()) {
				if (offer.isAborted() || offer.isFinished())
					continue;
				if (offer.getType() == type && offer.getItemId() == itemId) {
					sorted.add(offer);
				}
			}
			Collections.sort(sorted, new Comparator<ExchangeOffer>() {

				@Override
				public int compare(ExchangeOffer arg0, ExchangeOffer arg1) {
					return Integer.compare(arg0.getPrice(), arg1.getPrice());
				}
			});
			if (sorted.size() > 0)
				return sorted.get(0);
			else
				return null;
		}
	}

	/**
	 * Sends the player a login notification of their exchange items
	 * 
	 * @param player
	 *            The player
	 */
	public void sendLogin(Player player) {
		List<ExchangeOffer> offersList = getOffersList(player.getUsername());
		if (offersList.size() == 0) {
			return;
		}
		int complete = 0;
		for (ExchangeOffer offer : offersList) {
			if (offer.getItemsToCollect().getUsedSlots() > 0)
				complete++;
		}
		if (complete > 0) {
			player.sendMessage("<col=" + ChatColors.PURPLE + ">You have " + complete + " item" + (complete == 1 ? "" : "s") + " from the Grand Exchange waiting in your collection box.");
		}
	}

	/**
	 * Gets a list of the offers owned by the owner
	 * 
	 * @param owner
	 *            The owner we're getting the offers of
	 */
	public List<ExchangeOffer> getOffersList(String owner) {
		synchronized (LOCK) {
			List<ExchangeOffer> playerOffers = new ArrayList<ExchangeOffer>();
			ListIterator<ExchangeOffer> it = getExchangeOffers().listIterator();
			while (it.hasNext()) {
				ExchangeOffer offer = it.next();
				if (offer.getOwner().equalsIgnoreCase(owner)) {
					playerOffers.add(offer);
				}
			}
			return playerOffers;
		}
	}

	/**
	 * Saves the data to the file
	 *
	 * @param data
	 *            The list to save
	 */
	@Override
	public void save(List<ExchangeOffer> data) {
		synchronized (LOCK) {
			/** So we don't save unlimited offers to the list */
			List<ExchangeOffer> newList = new ArrayList<ExchangeOffer>();
			if (data != null) {
				for (ExchangeOffer offer : data) {
					if (offer.isUnlimited())
						continue;
					newList.add(offer);
				}
			}
			try {
				File file = new File(getFileLocation());
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				FileWriter fw = new FileWriter(getFileLocation());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(gson.toJson(newList));
				bw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Adds an offer to the {@link #exchangeOffers} list
	 * 
	 * @param offer
	 *            The offer to add
	 */
	public void addOffer(ExchangeOffer offer) {
		synchronized (LOCK) {
			FileLogger.getFileLogger().writeLog("exchange/", "[" + offer.getType() + "]" + offer.getOwner() + ":\t" + offer.getAmountRequested() + "x " + ItemDefinitions.getItemDefinitions(offer.getItemId()).getName() + "(" + offer.getItemId() + ")", true);
			getExchangeOffers().add(offer);
			save(getExchangeOffers());
			initialize();
		}
	}

	/**
	 * Removes an offer from the {@link #exchangeOffers} list
	 * 
	 * @param offer
	 *            The offer to remove
	 */
	public void removeOffer(ExchangeOffer offer) {
		synchronized (LOCK) {
			getExchangeOffers().remove(offer);
			save(getExchangeOffers());
			initialize();
		}
	}

	/**
	 * Saves the progress of an offer by removing the previous one from the list
	 * and adding the new one
	 * 
	 * @param offer
	 *            The offer to save the progress of
	 */
	public void saveProgress(ExchangeOffer offer) {
		synchronized (LOCK) {
			synchronized (exchangeOffers) {
				ListIterator<ExchangeOffer> it$ = getExchangeOffers().listIterator();
				while (it$.hasNext()) {
					ExchangeOffer exchangeOffer = it$.next();
					if (exchangeOffer.equals(offer)) {
						// System.out.println("Found offer that equals it.");
						it$.remove();
						break;
					}
				}
				getExchangeOffers().add(offer);
				save(getExchangeOffers());
			}
		}
	}

	@Override
	public String getFileLocation() {
		return Constants.FILES_PATH + "exchange/offers.json";
	}

	@Override
	public List<ExchangeOffer> load() {
		synchronized (LOCK) {
			List<ExchangeOffer> autospawns = null;
			String json = null;
			try {
				File file = new File(getFileLocation());
				if (!file.exists()) {
					return null;
				}
				FileReader reader = new FileReader(file);
				char[] chars = new char[(int) file.length()];
				reader.read(chars);
				json = new String(chars);
				reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			autospawns = gson.fromJson(json, new TypeToken<List<ExchangeOffer>>() {
			}.getType());
			return autospawns;
		}
	}

	/**
	 * @return the exchangeOffers
	 */
	public List<ExchangeOffer> getExchangeOffers() {
		return exchangeOffers;
	}

	/**
	 * The list of names of offers which have unlimited stock
	 */
	private List<String> unlimitedNames;

	/**
	 * The list of grand exchange offers
	 */
	private final List<ExchangeOffer> exchangeOffers = Collections.synchronizedList(new ArrayList<ExchangeOffer>());

	/**
	 * The object with which to synchronize
	 */
	public static final Object LOCK = new Object();
}
