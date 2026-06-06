package ictgradschool.industry.inventory_management.gui;

import ictgradschool.industry.inventory_management.admin.FilestoreManager;
import ictgradschool.industry.inventory_management.model.Product;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class Main extends JFrame {
    private File file;
    private List<Product> products;

    private final JPanel mainContainer;
    private final CardLayout cardLayout;
    private final JPanel filestoreSelect;
    private final JPanel systemSelect;

    public Main() {
        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        filestoreSelect = new FilestoreSelect();
        systemSelect = new SystemSelect();

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

    public class SystemSelect extends JPanel {
        JButton backBtn;
        JButton inventoryManagerBtn;
        JButton posBtn;

        public SystemSelect() {
            backBtn = new JButton("Close filestore");
            inventoryManagerBtn = new JButton("Open Inventory Manager");
            posBtn = new JButton("Open Point of Sale");
            buildPanelGui(backBtn, inventoryManagerBtn, posBtn);
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

    public class FilestoreSelect extends JPanel {
        JButton newFileBtn;
        JButton existingFileBtn;

        public FilestoreSelect() {
            newFileBtn = new JButton("Create a new Filestore");
            existingFileBtn = new JButton("Select an existing filestore");
            buildPanelGui(newFileBtn, existingFileBtn);

            existingFileBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFileChooser fileChooser = new JFileChooser();
                    int returnVal = fileChooser.showOpenDialog(Main.this);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        file = fileChooser.getSelectedFile(); // todo: do I have to try catch here? But I already try catch in FilestoreManager
                        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)); // change cursor to waiting mode to let user know that the file is loading.
                        products = FilestoreManager.readData(file); //todo: do i have to use worker doInBackground?
                        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                        changeToSystemSelectPanel();
                    }
                }
            });

            newFileBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
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

                        file = selectedFile;
                        products = new ArrayList<>();
                        changeToSystemSelectPanel();
                    }
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
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main());
    }
}
