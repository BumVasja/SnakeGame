/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakegame;

import javax.swing.JFrame;

/**
 *
 * @author Eiskalt
 */
public class SnakeMenu {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        new SnakeMenu().startGraphicInterface();
    }

    private void startGraphicInterface() {
        JFrame mainWindow = new JFrame("Snake menu");
        mainWindow.setLayout(null);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setSize(270, 240);
        mainWindow.setVisible(true);
    }
    
    
}
