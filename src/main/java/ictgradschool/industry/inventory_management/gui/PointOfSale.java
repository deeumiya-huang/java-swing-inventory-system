package ictgradschool.industry.inventory_management.gui;

import ictgradschool.industry.inventory_management.model.Repository;

import javax.swing.*;

public class PointOfSale extends JFrame {
    private Repository repository;
    public PointOfSale(Repository repository) {
        this.repository = repository;
        setTitle("Point of sale");
    }
}
