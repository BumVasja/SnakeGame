/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakegame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

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

        JButton startB = new JButton("Начать игру");
        startB.setLocation(30, 30);
        startB.setSize(200, 40);
        startB.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });
        mainWindow.add(startB);

        JButton highscoreB = new JButton("Рекорды");
        highscoreB.setLocation(30, 90);
        highscoreB.setSize(200, 40);
        highscoreB.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                /*game.highscores.HighscoreManager hm = new game.highscores.HighscoreManager();
                 JOptionPane.showMessageDialog(highscore, hm.getHighscoreString(), "Highscores", JOptionPane.WARNING_MESSAGE);*/
            }
        });
        mainWindow.add(highscoreB);

        JButton exitB = new JButton("Выход");
        exitB.setLocation(30, 150);
        exitB.setSize(200, 40);
        exitB.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(exitB, "Goodbye!", "OK", JOptionPane.WARNING_MESSAGE);
                System.exit(0);
            }
        });
        mainWindow.add(exitB);
    }

    public void startGame() {
        JDialog dlg = new JDialog((JFrame) null, "Snake v.0.1");
        dlg.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        final SnakeGame sn = new SnakeGame();
        dlg.getContentPane().add(sn);
        sn.newGame();
        dlg.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ev) {
                sn.processKey(ev);
            }
        });
        dlg.setVisible(true);
        dlg.pack();
        dlg.setResizable(true);
        dlg.setLocation(300, 200);
        dlg.addWindowListener(new WindowListener() {

            public void windowActivated(WindowEvent event) {
            }

            public void windowClosed(WindowEvent event) {
            }

            public void windowClosing(WindowEvent event) {
                Object[] options = {"Yes", "No!"};
                int n = JOptionPane.showOptionDialog(event.getWindow(), "Save score?",//You can save the score
                        "Snake", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, options,
                        options[0]);
                if (n == 0) {
                    Object[] options1 = {"Yes", "No!"};
                    String name = "Anonymouse";
                    JOptionPane k = new JOptionPane();
                    JDialog dialog = k.createDialog(null, "Score");

                    name = k.showInputDialog(null, "Your name", "Score", 1);
                    //HighscoreManager hm = new HighscoreManager();
                    if (name != null) {
                        name = name + " ";
                        //hm.addScore(name, sn.getPoints()); 
                    }
                    event.getWindow().dispose();

                } else {
                    event.getWindow().dispose();
                }
            }

            public void windowDeactivated(WindowEvent event) {
            }

            public void windowDeiconified(WindowEvent event) {
            }

            public void windowIconified(WindowEvent event) {
            }

            public void windowOpened(WindowEvent event) {
            }
        });
    }
}
