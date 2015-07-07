/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakegame;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.ImageIcon;

/**
 *
 * @author Eiskalt
 */
public class Snake {

    public static final int DIR_POUSE = 0;
    public static final int DIR_UP = 1;
    public static final int DIR_RIGHT = 2;
    public static final int DIR_DOWN = 3;
    public static final int DIR_LEFT = 4;
    private ArrayList<Point> body = new ArrayList<Point>();
    private static int speed = 410;

    public Point getBody(int i) {
        return body.get(i);
    }

    private int bodySize;

    public int getBodyPoints() {
        return body.size();
    }
    private int direction = DIR_POUSE;

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    /**
     * @param x0 X-coordinates
     * @param y0 Y-coordinates
     * @param sz Size of snake
     */
    Snake(int x0, int y0, int sz) {
        bodySize = sz;
        int x = x0 * sz + 2;
        int y = y0 * sz + 2;
        for (int i = 0; i < 1; i++) {
            body.add(new Point(x, y));
        }
    }

    public ArrayList<Point> getBody() {
        return body;
    }

    /**
     * Render snake on gameplay window(dialog)
     *
     * @param g2 Graphics2D
     */
    public void paint(Graphics2D g2, int lastPressedKey) {
        for (Point p : body) {
            //paint body
            ImageIcon bodyIcon = new ImageIcon("body.png");
            bodyIcon.paintIcon(null, g2, p.x, p.y);
        }
        Point p = body.get(body.size() - 1);
        //Выбор рисунка в зависимости от последней нажатой кнопки
        ImageIcon headIcon = null;
        if (lastPressedKey == KeyEvent.VK_RIGHT) {
            headIcon = new ImageIcon("head_right.png");
        } else if (lastPressedKey == KeyEvent.VK_LEFT) {
            headIcon = new ImageIcon("head_left.png");
        } else if (lastPressedKey == KeyEvent.VK_UP) {
            headIcon = new ImageIcon("head_up.png");
        } else {
            headIcon = new ImageIcon("head_down.png");
        }
        headIcon.paintIcon(null, g2, p.x, p.y);
    }

    /**
     * Return new snake position and void checking for self-eating
     *
     * @return New snake position
     */
    public Point move() { //Баг: Змейка жрет сама себя, а может фича?:D
        Point last = body.get(body.size() - 1);
        Point pp = last;
        switch (direction) {
            case DIR_POUSE:
                break;
            case DIR_UP:
                body.remove(0);
                pp = new Point(last.x, last.y - bodySize);
                body.add(pp);
                delete();
                break;
            case DIR_RIGHT:
                body.remove(0);
                pp = new Point(last.x + bodySize, last.y);
                body.add(pp);
                delete();
                break;
            case DIR_DOWN:
                body.remove(0);
                pp = new Point(last.x, last.y + bodySize);
                body.add(pp);
                delete();
                break;
            case DIR_LEFT:
                body.remove(0);
                pp = new Point(last.x - bodySize, last.y);
                body.add(pp);
                delete();
                break;
        }
        return pp;
    }

    /**
     *
     * @return time for Thread.sleep
     * @link SnakeGame#SnakeGame()
     * @see SnakeGame#SnakeGame
     */
    public int getSpeed() {
        return speed;
    }

    public static void setSpeed(int speed) {
        Snake.speed = speed;
    }

    /**
     * Add fruit to snake
     * @link SnakeGame#gameCycle()
     * @see SnakeGame#gameCycle
     *
     */
    public void expand() {
        body.add(0, new Point(body.get(0).x, body.get(0).y));
    }

    /**
     * Check self-eating of snake
     * @link move
     * @see  move()
     */
    public void delete() {
        for (int i = 1; i < body.size(); i++) {
            if (body.get(i).equals(body.get(0))) {
                body.removeAll(body.subList(i, body.size()));
            }
        }
    }
}
