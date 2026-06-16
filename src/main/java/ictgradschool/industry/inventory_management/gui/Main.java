package ictgradschool.industry.inventory_management.gui;

import ictgradschool.industry.inventory_management.model.Repository;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class Main extends JFrame {
    /* The filestore for repository */
    private Repository repositoryModel;

    private JFrame inventoryManager;
    private JFrame pos;
    private final JPanel mainContainer;
    private final CardLayout cardLayout;

    public Main() {
        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        JPanel filestoreSelect = new FilestoreSelect();
        JPanel systemSelect = new SystemSelect();

        buildGui(mainContainer, filestoreSelect, systemSelect);
    }

    private void buildGui(JPanel mainContainer, JPanel filestoreSelect, JPanel systemSelect) {
        mainContainer.add(filestoreSelect, "FILESTORE_PANEL");
        mainContainer.add(systemSelect, "SYSTEM_PANEL");

        setTitle("Welcome to inventory system");
        setPreferredSize(new Dimension(400,300));
        setContentPane(mainContainer);
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void changeToSystemSelectPanel() {
        cardLayout.show(mainContainer, "SYSTEM_PANEL");
    }

    private void changeToFilestoreSelectPanel() {
        cardLayout.show(mainContainer, "FILESTORE_PANEL");
    }

    private class SystemSelect extends JPanel {
        JButton backBtn;
        JButton inventoryManagerBtn;
        JButton posBtn;

        public SystemSelect() {
            backBtn = new JButton("Close filestore");
            inventoryManagerBtn = new JButton("Open Inventory Manager");
            posBtn = new JButton("Open Point of Sale");
            buildPanelGui(backBtn, inventoryManagerBtn, posBtn);

            backBtn.addActionListener(e -> {
                // reset repo
                repositoryModel = null;
                changeToFilestoreSelectPanel();
            });

            inventoryManagerBtn.addActionListener(e -> {
                inventoryManager = new InventoryManager(Main.this, repositoryModel);
                inventoryManager.setVisible(true);
                Main.this.setVisible(false);
            });

            posBtn.addActionListener(e -> {
                pos = new PointOfSale(repositoryModel);
                pos.setVisible(true);
                Main.this.setVisible(false);
            });
        }

        private void buildPanelGui(JButton backBtn, JButton inventoryManagerBtn, JButton posBtn) {
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.insets = new Insets(10, 10, 10, 10);
            add(backBtn, gbc);

            gbc.gridy = 1;
            add(inventoryManagerBtn, gbc);

            gbc.gridy = 2;
            add(posBtn, gbc);
        }
    }

    private class FilestoreSelect extends JPanel {

        JButton newFileBtn;
        JButton existingFileBtn;

        public FilestoreSelect() {
            newFileBtn = new JButton("Create a new Filestore");
            existingFileBtn = new JButton("Select an existing filestore");
            buildPanelGui(newFileBtn, existingFileBtn);

            existingFileBtn.addActionListener(e -> {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON file only (*.json)", "json");
                fileChooser.setFileFilter(filter);
                int returnVal = fileChooser.showOpenDialog(Main.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    // pass file path into repository for saving data.
                    repositoryModel = new Repository(file);
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)); // change cursor to waiting mode to let user know that the file is loading.

                    // load data in the background, and put into repository model when loading is done.
                    Worker worker = new Worker();
                    worker.execute();
                    // change cursor back and change to systemSelectPanel will be execute in Worker.done();
                }
            });

            newFileBtn.addActionListener(e -> {
                JFileChooser fileChooser = new JFileChooser();
                int returnVal = fileChooser.showSaveDialog(Main.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    // if user forget to type ".json", add it on automatically
                    if (!selectedFile.getName().endsWith(".json")) {
                        selectedFile = new File(selectedFile.getAbsolutePath() + ".json");
                    }
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                    if (selectedFile.exists()) {
                        int response = JOptionPane.showConfirmDialog(Main.this,
                                "The file already exists! Would you like to overwrite it and create a new filestore? (Note: Old data will be lost)",
                                "Confirm Overwrite",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.WARNING_MESSAGE
                        );
                        if (response == JOptionPane.NO_OPTION) {
                            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                            return;
                        }
                    } else {
                        try {
                            boolean isCreated = selectedFile.createNewFile();
                            if (!isCreated) {
                                throw  new IOException("Failed to create a new file.");
                            }
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                    }
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

                    repositoryModel = new Repository(selectedFile);
                    changeToSystemSelectPanel();
                }
            });
        }

        private void buildPanelGui(JButton newFileBtn, JButton existingFileBtn) {
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.insets = new Insets(10, 10, 10, 10);
            add(newFileBtn, gbc);

            gbc.gridy = 1;
            add(existingFileBtn, gbc);
        }

        private class Worker extends SwingWorker<Integer, Void> {
            @Override
            protected Integer doInBackground() {
                return repositoryModel.loadData();
            }

            @Override
            protected void done() {
                try {
                    int result = get();

                    if (result == -1) {
                        // No data loaded.
                        JOptionPane.showMessageDialog(
                                Main.this,
                                "Unable to load filestore. The data file is empty, missing or corrupt.",
                                "Load error", JOptionPane.WARNING_MESSAGE);
                    } else {
                        // data successfully loaded.
                        changeToSystemSelectPanel();
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                } finally {
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main());
    }
}
