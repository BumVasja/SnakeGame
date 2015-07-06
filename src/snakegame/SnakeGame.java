/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakegame;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.geom.Area;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author Eiskalt
 */
public class SnakeGame extends JPanel {

    private static final int wallStep = 20;//Размер одной клетки в пикселях
    private Shape walls1 = new Polygon();
    private Shape walls2 = new Polygon();
    private Shape walls3 = new Polygon();
    private Snake snake = new Snake(2, 2, wallStep);
    private Point orange = new Point();
    private Point cherry = new Point();
    private int points = 0;
    private boolean GameOver;
    private int lastPressedKey = 0;
    private final Random rand = new Random();
    private String message = null;
    private int level = 1;

    public SnakeGame() {
        super(true);
        Dimension d = getLevel("levels/sn"+level+".txt");
        putOrange();
        putCherry();
        setPreferredSize(d);
        setMinimumSize(d);
        setMaximumSize(d);
        setSize(d);

        Thread th = new Thread(new Runnable() {

            public void run() {
                while (true) {
                    gameCycle();
                    //Переход на следующий уровень
                    nextLevel();
                    try {
                        Thread.sleep(500 - snake.getSpeed());
                    } catch (InterruptedException e) {
                    }
                }
            }
        });
        th.start();
    }

    private void putOrange() {
        int x = 5;
        int y = 5;
        while (walls2.contains(x, y)) {
            x = wallStep * rand.nextInt(40) + 2;
            y = wallStep * rand.nextInt(40) + 2;
        }
        orange.setLocation(x, y);
    }

    private void putCherry() {
        int x = 5;
        int y = 5;
        while (walls2.contains(x, y)) {
            x = wallStep * rand.nextInt(40) + 2;
            y = wallStep * rand.nextInt(40) + 2;
        }
        cherry.setLocation(x, y);
    }

    public Snake getSnake() {
        return snake;
    }

    private Dimension getLevel(String fileName) {
        int minX = 500, maxX = 0, minY = 500, maxY = 0;
        Area w1 = new Area();
        Area w2 = new Area();
        Area w3 = new Area();

        BufferedReader input = null;
        try {
            File file = new File(fileName);
            input = new BufferedReader(new FileReader(file));
            String line = null;

            for (int y = 0; (line = input.readLine()) != null; y++) {
                for (int x = 0; x < line.length(); x++) {
                    if (line.charAt(x) == '1') {
                        minX = Math.min(minX, x * wallStep);
                        maxX = Math.max(maxX, (x + 1) * wallStep + 4);
                        minY = Math.min(minY, y * wallStep);
                        maxY = Math.max(maxY, (y + 1) * wallStep + 4);

                        w1.add(new Area(new Rectangle(x * wallStep, y * wallStep, wallStep, wallStep)));
                        w2.add(new Area(new Rectangle(x * wallStep + 2, y * wallStep + 2, wallStep, wallStep)));
                        w3.add(new Area(new Rectangle(x * wallStep + 4, y * wallStep + 4, wallStep, wallStep)));
                    }
                }
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
        walls1 = w1;
        walls2 = w2;
        walls3 = w3;
        Dimension d = new Dimension(maxX - minX, maxY - minY);
        return d;
    }

    public void gameCycle() {
        if (snake.getDirection() != Snake.DIR_POUSE) {
            setMessage(null);
        }
        Point p = snake.move();
        if (p.x == orange.x && p.y == orange.y) {
            points += 50;
            snake.expand();
            putOrange();
        }
        if (p.x == cherry.x && p.y == cherry.y) {
            points += 100;
            snake.expand();
            putCherry();
        }
        if (walls2.contains(p)) {
            if (snake.getDirection() != Snake.DIR_POUSE) {
                points -= 50;
                GameOver = true;
            }
            snake.setDirection(Snake.DIR_POUSE);
            setMessage("Game over!");
        }
        this.repaint();
    }

    public void nextLevel() {
        if (points >= 300*level & level < 5) {
            getLevel("levels/sn"+(++level)+".txt");//! Баг: Блоки закрывают вкусняшки
            putOrange();
            putCherry();
            snake.setDirection(Snake.DIR_POUSE);
        }
    }

    private void setMessage(String msg) {
        message = msg;
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        ImageIcon brickIcon = new ImageIcon("brick.png");
        brickIcon.paintIcon(this, g2, orange.x, orange.y);
        g2.setColor(Color.white);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setColor(Color.gray);
        g2.fill(walls3);
        g2.setColor(Color.lightGray);
        g2.fill(walls1);
        g2.setColor(Color.white);//Цвет закраски
        g2.fill(walls2);
        //paint orange
        ImageIcon orangeIcon = new ImageIcon("orange.png");
        orangeIcon.paintIcon(this, g2, orange.x, orange.y);
        //g2.drawImage(fruits.getImage(), orange.x, orange.y, Color.white, this);
        /*g2.setColor(Color.orange);
        g2.fillArc(orange.x, orange.y, wallStep, wallStep, 0, 360);
        g2.setColor(Color.black);
        g2.drawArc(orange.x, orange.y, wallStep, wallStep, 0, 360);*/
        //paint cherry
        ImageIcon cherryIcon = new ImageIcon("cherry.png");
        cherryIcon.paintIcon(this, g2, cherry.x, cherry.y);
        /*g2.setColor(Color.red);
        g2.fillArc(cherry.x, cherry.y, wallStep, wallStep, 0, 360);
        g2.setColor(Color.black);
        g2.drawArc(cherry.x, cherry.y, wallStep, wallStep, 0, 360);*/
        snake.paint(g2);
        g2.setColor(Color.black);
        g2.drawString("Points: " + points, 2, 10);
        g2.drawString("Level: " + level, 320, 10);
        if (message != null) {
            g2.setColor(Color.red);
            g2.fillRect(150, 100, 100, 30);
            g2.setColor(Color.black);
            g2.drawRect(150, 100, 100, 30);
            g2.drawString(message, 160, 120);
        }
    }

    public void processKey(KeyEvent ev) {
        Snake snake = getSnake();
        switch (ev.getKeyCode()) {
            case KeyEvent.VK_RIGHT:
                if (lastPressedKey != KeyEvent.VK_LEFT && !GameOver) {
                    snake.setDirection(Snake.DIR_RIGHT);
                    lastPressedKey = KeyEvent.VK_RIGHT;
                }
                break;
            case KeyEvent.VK_LEFT:
                if (lastPressedKey != KeyEvent.VK_RIGHT && !GameOver) {
                    snake.setDirection(Snake.DIR_LEFT);
                    lastPressedKey = KeyEvent.VK_LEFT;
                }
                break;
            case KeyEvent.VK_DOWN:
                if (lastPressedKey != KeyEvent.VK_UP && !GameOver) {
                    snake.setDirection(Snake.DIR_DOWN);
                    lastPressedKey = KeyEvent.VK_DOWN;
                }
                break;
            case KeyEvent.VK_UP:
                if (lastPressedKey != KeyEvent.VK_DOWN && !GameOver) {
                    snake.setDirection(Snake.DIR_UP);
                    lastPressedKey = KeyEvent.VK_UP;
                }
                break;
            case KeyEvent.VK_ESCAPE:
                System.exit(0);
                break;
        }
    }

    void setScore(int x) {
        points = points + x;
    }

    void newGame() {
        GameOver = false;
        points = 0;
        lastPressedKey = 0;
    }

    int getPoints() {
        return points;
    }
}
