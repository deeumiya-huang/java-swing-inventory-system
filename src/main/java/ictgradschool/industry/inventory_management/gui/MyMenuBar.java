package ictgradschool.industry.inventory_management.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MyMenuBar extends JMenuBar {
    private final JFrame currentFrame;
    private final JFrame mainFrame;

    public MyMenuBar(JFrame currentFrame, JFrame mainFrame) {
        this(currentFrame, mainFrame, null);
    }

    public MyMenuBar(JFrame currentFrame, JFrame mainFrame, Runnable onBack) {
        this.currentFrame = currentFrame;
        this.mainFrame = mainFrame;
        JMenu menu = new JMenu("Menu");
        menu.add(new JMenuItem(new BackToWelcomeAction(onBack)));

        menu.addSeparator();
        menu.add(new JMenuItem(new ExitAction()));
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
        Runnable onBack;

        public BackToWelcomeAction() {
            putValue(Action.NAME, "Back to Welcome Screen");
        }

        public BackToWelcomeAction(Runnable onBack) {
            this();
            this.onBack = onBack;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (onBack != null) {
                onBack.run();
            }

            currentFrame.dispose();
            mainFrame.setVisible(true);
        }
    }
}
