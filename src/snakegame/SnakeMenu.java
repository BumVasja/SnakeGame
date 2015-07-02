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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Eiskalt
 */
public class SnakeMenu {

    ArrayList<String> hsm = new ArrayList<String>();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        new SnakeMenu().startGraphicInterface();
    }

    private void startGraphicInterface() {
        JFrame mainWindow = new JFrame("Snake menu");
        mainWindow.setLocationRelativeTo(null);
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
                //Вывод рекордов по нажатию
                //showRecords();
                JOptionPane.showMessageDialog(highscoreB, hsm, "Highscores", JOptionPane.WARNING_MESSAGE);
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
                int n = JOptionPane.showOptionDialog(event.getWindow(), "Save score?",
                        "Snake", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, options,
                        options[0]);
                if (n == 0) {
                    Object[] options1 = {"Yes", "No!"};
                    String name = "Anonymouse";
                    JOptionPane k = new JOptionPane();
                    JDialog dialog = k.createDialog(null, "Score");

                    name = k.showInputDialog(null, "Your name", "Score", 1);
                    if (name != null) {
                        name = name + " ";
                        hsm.add(name + sn.getPoints());
                        // запись в файл
                        //writeHighScore(name, sn.getPoints());
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

    public void writeHighScore(String name, int points) {
        File file = new File("C:/highscores.txt");

        try {
            //проверяем, что если файл не существует то создаем его
            if (!file.exists()) {
                file.createNewFile();
            }

            //PrintWriter обеспечит возможности записи в файл
            PrintWriter out = new PrintWriter(file.getAbsoluteFile());

            try {
                //Записываем текст в файл
                out.print(name + points);
            } finally {
                //После чего мы должны закрыть файл
                //Иначе файл не запишется
                out.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showRecords() {

        BufferedReader input = null;

        try {
            File file = new File("C:/highscores.txt");
            input = new BufferedReader(new FileReader(file));
            String line = null;
            String[][] a = new String[10][2];
            //Производим запись в массив
            for (int y = 0; (line = input.readLine()) != null; y++) {
                a[y] = line.split(" ");
                /*System.out.println(a[y][0]);
                 System.out.println(a[y][1]);*/
            }
            //Сортируем его(а вообще при записи надо сортировать)
            for (int i = 0; i < a.length - 1; i++) {
                for (int j = 0; j < a.length - i - 1; j++) {
                    System.out.println(a[j][1]);
                    if (Integer.parseInt(a[j][1]) > Integer.parseInt(a[j + 1][1])) {
                        String[] temp = new String[2];
                        temp = a[j];
                        a[j] = a[j + 1];
                        a[j + 1] = a[j];
                    }
                }
            }
            for (int i = 0; i < 10; i++) {
                System.out.println(a[i][0]);
                System.out.println(a[i][1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
