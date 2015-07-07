/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakegame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.TexturePaint;
import java.awt.event.KeyEvent;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
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
    private boolean moved = false;

    public SnakeGame() {
        super(true);
        Dimension d = getLevel("levels/sn" + level + ".txt");
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
                    moved = true;
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
        if (orange.equals(cherry)) {
            putCherry(); //чтобы не появлялись на одном месте
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
        //Проверка столкновения с самим собой(с головным элементом, только если элементов больше 4)
        if (snake.getBodyPoints() > 4 && GameOver != true && snake.getDirection() != Snake.DIR_POUSE) {
            for (int i = 1; i < snake.getBodyPoints(); i++) {
                if (snake.getBody(0).x == snake.getBody(i).x && snake.getBody(0).y == snake.getBody(i).y) {
                    System.out.println(snake.getBody(0).x + " " + snake.getBody(i).x);
                    points -= 50;
                    GameOver = true;
                    snake.setDirection(Snake.DIR_POUSE);
                    setMessage("Game over!");
                    break;
                }
            }
        }
        //Проверка столкновения с апельсинкой
        if (p.x == orange.x && p.y == orange.y) {
            points += 50;
            snake.expand();
            putOrange();
        }
        //Проверка столкновения с вишенкой
        if (p.x == cherry.x && p.y == cherry.y) {
            points += 100;
            snake.expand();
            putCherry();
        }
        //Проверка столкновения со стеной
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
        if (points >= 300 * level & level < 5) {
            getLevel("levels/sn" + (++level) + ".txt");
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
        g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        /*ImageIcon brickIcon = new ImageIcon("brick.png");
        brickIcon.paintIcon(this, g2, orange.x, orange.y);*/
        g2.setColor(Color.white);//Цвет пола
        BufferedImage grass = null;
        try {
            grass = ImageIO.read(new File("grass.png"));
        } catch (IOException ex) {
            Logger.getLogger(SnakeGame.class.getName()).log(Level.SEVERE, null, ex);
        }
        TexturePaint gp = new TexturePaint(grass, new Rectangle(0, 0, 32, 32));
        g2.setPaint(gp);
        g2.fill(walls2);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setColor(Color.gray);
        g2.fill(walls3);
        g2.setColor(Color.lightGray);
        g2.fill(walls1);
        g2.setColor(Color.white);//Цвет закраски
        BufferedImage brick = null;
        TexturePaint tp = null;
        try {
            if (level < 4) {
                brick = ImageIO.read(new File("brick.png"));
                tp = new TexturePaint(brick, new Rectangle(0, 0, 20, 20));
            } else {
                brick = ImageIO.read(new File("heart.jpg"));
                tp = new TexturePaint(brick, new Rectangle(0, 0, 70, 70));
                snake.setDirection(Snake.DIR_POUSE);
                setMessage("You Win!");
            }
        } catch (IOException ex) {
            Logger.getLogger(SnakeGame.class.getName()).log(Level.SEVERE, null, ex);
        }
        //TexturePaint tp = new TexturePaint(brick, new Rectangle(0, 0, 20, 20));
        g2.setPaint(tp);
        g2.fill(walls2);
        //paint orange
        ImageIcon orangeIcon = new ImageIcon("orange.png");
        orangeIcon.paintIcon(this, g2, orange.x, orange.y);
        //paint cherry
        ImageIcon cherryIcon = new ImageIcon("cherry.png");
        cherryIcon.paintIcon(this, g2, cherry.x, cherry.y);
        snake.paint(g2, lastPressedKey);
        g2.setColor(Color.white);
        g2.drawString("Points: " + points, 2, 10);
        g2.drawString("Level: " + level, 320, 10);
        if (message != null) {
            if (level < 4) g2.setColor(Color.red);
            else g2.setColor(Color.blue);
            g2.fillRect(getWidth()/2-50, getHeight()/2-15, 100, 30);
            g2.setColor(Color.white);
            g2.drawRect(getWidth()/2-50, getHeight()/2-15, 100, 30);
            g2.drawString(message, getWidth()/2-30, getHeight()/2);
        }
    }

    //Баг: Самокушенье, запретить двигаться в сторону своих элементов
    public void processKey(KeyEvent ev) {
        Snake snake = getSnake();
        switch (ev.getKeyCode()) {
            case KeyEvent.VK_RIGHT:
                if (lastPressedKey != KeyEvent.VK_LEFT && !GameOver && moved == true) {
                    snake.setDirection(Snake.DIR_RIGHT);
                    lastPressedKey = KeyEvent.VK_RIGHT;
                    moved = false;
                }
                break;
            case KeyEvent.VK_LEFT:
                if (lastPressedKey != KeyEvent.VK_RIGHT && !GameOver && moved == true) {
                    snake.setDirection(Snake.DIR_LEFT);
                    lastPressedKey = KeyEvent.VK_LEFT;
                    moved = false;
                }
                break;
            case KeyEvent.VK_DOWN:
                if (lastPressedKey != KeyEvent.VK_UP && !GameOver && moved == true) {
                    snake.setDirection(Snake.DIR_DOWN);
                    lastPressedKey = KeyEvent.VK_DOWN;
                    moved = false;
                }
                break;
            case KeyEvent.VK_UP:
                if (lastPressedKey != KeyEvent.VK_DOWN && !GameOver && moved == true) {
                    snake.setDirection(Snake.DIR_UP);
                    lastPressedKey = KeyEvent.VK_UP;
                    moved = false;
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
