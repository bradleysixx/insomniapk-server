package org.scapesoft.utilities.console.dumpers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class DonationCalculator {

	private static LinkedHashMap<String, Integer> donations = new LinkedHashMap<>();

	public static void main(String[] args) throws IOException {
		String month = "Nov";
		List<String> lines = Files.readAllLines(new File("./info/script/donations.txt").toPath());
		lines.forEach(l -> {
			if (month.isEmpty() || (!month.isEmpty() && l.contains(month))) {
				int amt = Integer.parseInt(l.substring(l.indexOf("$") + 1, l.length() - 1));
				String name = l.substring(l.indexOf("]") + 2, l.indexOf("has") - 1);
				addDonation(name, amt);
				total += amt;
				count++;
			}
		});
		System.out.println("In " + month + ", we made a total of $" + total + ", an average of $" + (total / count) + " per donation");
		System.out.println();

		donations = sortByValue(donations);
		for (String name : donations.keySet()) {
			System.out.println(name + " has donated a total of $" + donations.get(name) + "!");
		}
	}

	public static <K, V extends Comparable<? super V>> LinkedHashMap<K, V> sortByValue(Map<K, V> map) {
		Map<K, V> result = new LinkedHashMap<>();
		map.entrySet().stream().sorted(Comparator.comparing(e -> e.getValue())).forEach(e -> result.put(e.getKey(), e.getValue()));

		return (LinkedHashMap<K, V>) result;
	}

	public static void addDonation(String username, int value) {
		if (donations.containsKey(username))
			donations.put(username, donations.get(username) + value);
		else
			donations.put(username, value);
	}

	private static int total = 0;
	private static int count = 0;

}