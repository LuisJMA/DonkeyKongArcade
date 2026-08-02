package com.donkeykong.model;

import java.awt.Rectangle;
import java.util.List;

public class Player {
    private int x, y, width, height;
    private int xVelocity = 0;
    private double yVelocity = 0;
    
    private final double GRAVITY = 0.5;
    private final double JUMP_STRENGTH = -10.0;
    private boolean isJumping = false;

    public Player(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void update(List<Platform> platforms) {
        // 1. Movimiento horizontal y colisiones en X
        x += xVelocity;
        for (Platform p : platforms) {
            if (getBounds().intersects(new Rectangle(p.getX(), p.getY(), p.getWidth(), p.getHeight()))) {
                if (xVelocity > 0) { // Chocaendo hacia la derecha
                    x = p.getX() - width;
                } else if (xVelocity < 0) { // Chocando hacia la izquierda
                    x = p.getX() + p.getWidth();
                }
            }
        }

        // 2. Aplicar gravedad y movimiento vertical
        yVelocity += GRAVITY;
        y += (int) yVelocity;

        // 3. Colisiones verticales (Suelos y Techos)
        for (Platform p : platforms) {
            Rectangle playerRect = getBounds();
            Rectangle platRect = new Rectangle(p.getX(), p.getY(), p.getWidth(), p.getHeight());

            if (playerRect.intersects(platRect)) {
                if (yVelocity > 0) { // Cayendo (aterrizando sobre la plataforma)
                    y = p.getY() - height;
                    yVelocity = 0;
                    isJumping = false;
                } else if (yVelocity < 0) { // Saltando y golpeando la plataforma por debajo
                    y = p.getY() + p.getHeight();
                    yVelocity = 0; // Se frena al golpear el techo
                }
            }
        }

        // Límites de la pantalla
        if (x < 0) x = 0;
        if (x > 800 - width) x = 800 - width;
    }

    public void jump() {
        if (!isJumping) {
            yVelocity = JUMP_STRENGTH;
            isJumping = true;
        }
    }

    public void setXVelocity(int vel) {
        this.xVelocity = vel;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}