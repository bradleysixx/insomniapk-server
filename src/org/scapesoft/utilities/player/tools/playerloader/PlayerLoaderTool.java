package org.scapesoft.utilities.player.tools.playerloader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.scapesoft.Constants;
import org.scapesoft.cache.Cache;
import org.scapesoft.game.item.Item;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.misc.Config;
import org.scapesoft.utilities.player.Saving;
import org.scapesoft.utilities.player.scripts.GameScript;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Nov 29, 2014
 */
public class PlayerLoaderTool extends GameScript {

	public static void main(String... args) throws IOException {
		Cache.init();
		Config.get().load();
		PlayerLoaderTool tool = new PlayerLoaderTool();
		tool.displayAccountRequest();
	}
	
	public void displayAccountRequest() {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					container.clear();
					JFileChooser chooser = new JFileChooser();
					chooser.setFileFilter(new FileNameExtensionFilter(Constants.SERVER_NAME + " Player Files", "p"));
					int returnVal = chooser.showOpenDialog(chooser);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						storeCharacterInformation(chooser.getSelectedFile());
					}
					displayFrame();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Storing the character's information in the map so it can be easily called
	 * for later access
	 * 
	 * @param file
	 *            The character's file
	 */
	private void storeCharacterInformation(File file) {
		try {
			Player player = (Player) Saving.loadSerializedFile(file);
			if (player != null) {
				for (Item item : player.getInventory().getItems().toArray()) {
					if (item == null)
						continue;
					addItem(FileType.INVENTORY, item);
				}
				for (Item item : player.getEquipment().getItems().toArray()) {
					if (item == null)
						continue;
					addItem(FileType.EQUIPMENT, item);
				}
				for (Item item : player.getBank().getContainerCopy()) {
					if (item == null)
						continue;
					addItem(FileType.BANK, item);
				}
				if (player.getFamiliar() != null && player.getFamiliar().getBob() != null) {
					for (Item item : player.getFamiliar().getBob().getBeastItems().toArray()) {
						if (item == null)
							continue;
						addItem(FileType.FAMILIAR, item);
					}
				}
			} else {
				System.err.println("Player is null, didn't do anything.");
			}
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		mainFile = file;
	}

	/**
	 * Adding the item to the list of items for the {@link #FileType} applicable
	 * 
	 * @param type
	 *            The type of file
	 * @param item
	 *            The item
	 */
	private void addItem(FileType type, Item item) {
		List<Item> items = container.get(type);
		if (items == null) {
			items = new ArrayList<>();
		}
		items.add(item);
		container.put(type, items);
	}

	/**
	 * Displays the frame with all the information
	 */
	private void displayFrame() {
		PlayerInformationer informationer = new PlayerInformationer(this, container);
		informationer.getPlayerNameLabel().setText("Player: " + mainFile.getAbsolutePath());
		informationer.refreshTable(FileType.BANK);
		informationer.setVisible(true);
	}

	private File mainFile;
	private final Map<FileType, List<Item>> container = new HashMap<>();

	public enum FileType {
		BANK, INVENTORY, FAMILIAR, EQUIPMENT
	}

}
