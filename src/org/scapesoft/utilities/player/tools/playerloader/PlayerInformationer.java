/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scapesoft.utilities.player.tools.playerloader;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.table.DefaultTableModel;

import org.scapesoft.game.item.Item;
import org.scapesoft.utilities.player.tools.playerloader.PlayerLoaderTool.FileType;

/**
 *
 * @author DeLL
 */
@SuppressWarnings("serial")
public class PlayerInformationer extends javax.swing.JFrame {

	/**
	 * Creates new form PlayerInformationer
	 */
	public PlayerInformationer(PlayerLoaderTool tool, Map<FileType, List<Item>> map) {
		initComponents();
		this.tool = tool;
		this.container = map;
	}

	private void initComponents() {
		fileTypeBox = new javax.swing.JComboBox<FileType>(FileType.values());
		fileTypeBox.addActionListener(getFileTypeListener());
		jScrollPane2 = new javax.swing.JScrollPane();
		playerItemsTable = new javax.swing.JTable();
		reloadButton = new javax.swing.JButton();
		setPlayerNameLabel(new javax.swing.JLabel());

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		playerItemsTable.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { {}, {}, {}, {} }, new String[] {

		}));
		jScrollPane2.setViewportView(playerItemsTable);
		reloadButton.setText("Reload");
		reloadButton.addActionListener(getReloadListener());

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 494, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addComponent(playerNameLabel).addGap(0, 0, Short.MAX_VALUE)).addGroup(layout.createSequentialGroup().addComponent(fileTypeBox, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(reloadButton))).addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(playerNameLabel).addGap(7, 7, 7).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(fileTypeBox, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(reloadButton)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 494, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));

		pack();
	}// </editor-fold>

	private ActionListener getFileTypeListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				FileType selected = (FileType) fileTypeBox.getSelectedItem();
				refreshTable(selected);
			}

		};
	}
	
	private ActionListener getReloadListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				tool.displayAccountRequest();
			}
		};
	}

	public void refreshTable(FileType type) {
		List<Item> items = container.get(type);
		if (items == null)
			items = new ArrayList<>();

		DefaultTableModel model = new DefaultTableModel() {

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		model.addColumn("Name");
		model.addColumn("Id");
		model.addColumn("Amount");

		for (Item item : items) {
			if (item == null) {
				continue;
			}
			model.addRow(new Object[] { item.getDefinitions().getName(), item.getDefinitions().id, item.getAmount() });
		}

		playerItemsTable.setModel(model);
		playerItemsTable.scrollRectToVisible(playerItemsTable.getCellRect(playerItemsTable.getRowCount() - 1, playerItemsTable.getColumnCount(), true));

	}

	public javax.swing.JLabel getPlayerNameLabel() {
		return playerNameLabel;
	}

	public void setPlayerNameLabel(javax.swing.JLabel playerNameLabel) {
		this.playerNameLabel = playerNameLabel;
	}

	// Variables declaration - do not modify
	private javax.swing.JLabel playerNameLabel;
	private javax.swing.JComboBox<FileType> fileTypeBox;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JTable playerItemsTable;
	private Map<FileType, List<Item>> container;
	private PlayerLoaderTool tool;
	private javax.swing.JButton reloadButton;
	// End of variables declaration
}
