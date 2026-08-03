package com.donkeykong.model;

import java.awt.Rectangle;
import java.util.List;

public class Player {
    private int x, y, width, height;
    private int xVelocity = 0;
    private double yVelocity = 0;
    private int lives = 3;
    
    private final double GRAVITY = 0.5;
    private final double JUMP_STRENGTH = -7.5;
    private boolean isOnGround = false;
    private boolean jumpRequested = false; // Bandera para registrar la intención de salto

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
                if (xVelocity > 0) {
                    x = p.getX() - width;
                } else if (xVelocity < 0) {
                    x = p.getX() + p.getWidth();
                }
            }
        }

        // Asumimos que inicialmente no está en el suelo antes de evaluar colisiones verticales
        isOnGround = false;

        // 2. Aplicar gravedad y movimiento vertical
        yVelocity += GRAVITY;
        y += (int) yVelocity;

        // 3. Colisiones verticales (Suelos y Techos)
        for (Platform p : platforms) {
            Rectangle playerRect = getBounds();
            Rectangle platRect = new Rectangle(p.getX(), p.getY(), p.getWidth(), p.getHeight());

            if (playerRect.intersects(platRect)) {
                if (yVelocity > 0) { // Cayendo (aterrizando)
                    y = p.getY() - height;
                    yVelocity = 0;
                    isOnGround = true;
                } else if (yVelocity < 0) { // Golpeando techo
                    y = p.getY() + p.getHeight();
                    yVelocity = 0;
                }
            }
        }

        // Si el jugador había presionado saltar y ahora ya tocó el suelo, ejecutamos el salto inmediatamente
        if (jumpRequested && isOnGround) {
            yVelocity = JUMP_STRENGTH;
            isOnGround = false;
            jumpRequested = false; // Consumimos la petición
        }

        // Límites de la pantalla
        if (x < 0) x = 0;
        if (x > 800 - width) x = 800 - width;
    }

    public void jump() {
        if (isOnGround) {
            yVelocity = JUMP_STRENGTH;
            isOnGround = false;
            jumpRequested = false;
        } else {
            // Si pulsa en el aire o justo antes de aterrizar, guardamos la intención por si toca tierra de inmediato
            jumpRequested = true;
        }
    }

    public void setXVelocity(int vel) {
        this.xVelocity = vel;
    }

    public boolean hasFallenOffScreen(int screenHeight) {
        return y > screenHeight;
    }

    public void loseLife(int startX, int startY) {
        lives--;
        resetPosition(startX, startY);
    }

    public void resetPosition(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.xVelocity = 0;
        this.yVelocity = 0;
        this.isOnGround = false;
        this.jumpRequested = false;
    }

    public int getLives() { return lives; }
    public Rectangle getBounds() { return new Rectangle(x, y, width, height); }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}
