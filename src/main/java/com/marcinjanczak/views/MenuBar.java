package com.marcinjanczak.views;

import javax.swing.*;

public class MenuBar extends JMenuBar {
    private final JMenu fileMenu;

    private final JMenuItem loadItem;
    private final JMenuItem exportItem;
    private final JMenuItem exitMenuItem;


    public MenuBar() {
        fileMenu = new JMenu("Pliki");

        loadItem = new JMenuItem("Otwórz");
        exportItem = new JMenuItem("Zapisz");
        exitMenuItem = new JMenuItem("Zakończ");

        fileMenu.add(loadItem);
        fileMenu.add(exitMenuItem);
        fileMenu.add(exitMenuItem);

        add(fileMenu);
    }

    public JMenuItem getLoadItem() {
        return loadItem;
    }

    public JMenuItem getExportItem() {
        return exportItem;
    }

    public JMenuItem getExitMenuItem() {
        return exitMenuItem;
    }
}
