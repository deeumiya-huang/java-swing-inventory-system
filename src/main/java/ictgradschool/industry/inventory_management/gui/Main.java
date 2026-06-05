package ictgradschool.industry.inventory_management.gui;

import ictgradschool.industry.inventory_management.admin.FilestoreManager;
import ictgradschool.industry.inventory_management.model.Product;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.*;
import java.util.List;

public class Main extends JFrame {
    List<Product> products;
    JButton newFileBtn;
    JButton existingFileBtn;
    public Main() {
        JPanel panel = new JPanel();
        newFileBtn = new JButton("Create a new Filestore");
        existingFileBtn = new JButton("Select an existing filestore");
        buildGui(panel, newFileBtn, existingFileBtn);

        existingFileBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnVal = fileChooser.showOpenDialog(Main.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile(); // todo: do I have to try catch here? But I already try catch in FilestoreManager
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)); // change cursor to waiting mode to let user know that the file is loading.
                    products = FilestoreManager.readData(file); //todo: do i have to use worker doInBackground?
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
//                    changeToSelectScreen();
                }
            }
        });
    }

    private void buildGui(JPanel panel, JButton btn1, JButton btn2) {
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        panel.add(btn1, gbc);

        gbc.gridy = 1;
        panel.add(btn2, gbc);

        setTitle("Welcome to inventory system");
        setPreferredSize(new Dimension(400,300));
        setContentPane(panel);
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main());
    }
}
