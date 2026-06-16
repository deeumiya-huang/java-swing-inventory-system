package ictgradschool.industry.inventory_management.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MyMenuBar extends JMenuBar {
    private final JFrame currentFrame;
    private final JFrame mainFrame;

    public MyMenuBar(JFrame currentFrame, JFrame mainFrame) {
        this.currentFrame = currentFrame;
        this.mainFrame = mainFrame;
        JMenu menu = new JMenu("Menu");
        JMenuItem item;
        menu.add(item = new JMenuItem(new BackToWelcomeAction()));

        menu.addSeparator();
        menu.add(item = new JMenuItem(new ExitAction()));
        add(menu);
    }

    private class ExitAction extends AbstractAction {
        public ExitAction() {
            putValue(Action.NAME, "Exit");
            putValue(Action.SHORT_DESCRIPTION, "Exit the application");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    private class BackToWelcomeAction extends AbstractAction {
        public BackToWelcomeAction() {
            putValue(Action.NAME, "Back to Welcome Screen");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            currentFrame.dispose();
            mainFrame.setVisible(true);
        }
    }
}
