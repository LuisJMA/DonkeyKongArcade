package com.donkeykong.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

public class Player {
    private int x, y;
    private int width, height;
    private double xSpeed, ySpeed;
    private boolean isJumping;

    private static final double GRAVITY = 0.5;
    private static final double JUMP_STRENGTH = -10.0;
    private static final double MOVE_SPEED = 3.0;

    public Player(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.width = 30;
        this.height = 40;
        this.xSpeed = 0;
        this.ySpeed = 0;
        this.isJumping = false;
    }

    public void update(List<Platform> platforms) {
        // Aplicar gravedad
        ySpeed += GRAVITY;

        // Movimiento horizontal
        x += xSpeed;
        
        // Colisión horizontal básica con pantallas (bordes de 800x600)
        if (x < 0) x = 0;
        if (x > 800 - width) x = 800 - width;

        // Movimiento vertical y colisión con plataformas
        y += (int) ySpeed;

        for (Platform p : platforms) {
            Rectangle playerBounds = getBounds();
            Rectangle platformBounds = p.getBounds();

            if (playerBounds.intersects(platformBounds)) {
                // Si cae sobre la plataforma
                if (ySpeed > 0 && y + height - (int)ySpeed <= p.getBounds().y + 10) {
                    y = p.getBounds().y - height;
                    ySpeed = 0;
                    isJumping = false;
                }
            }
        }
    }

    public void jump() {
        if (!isJumping) {
            ySpeed = JUMP_STRENGTH;
            isJumping = true;
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, width, height);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void setXSpeed(double xSpeed) {
        this.xSpeed = xSpeed;
    }
}