package com.donkeykong.model;

public class Barrel {
    private int x, y;
    private int width, height;
    private double speedX;
    private double speedY;
    private int direction;
    private boolean falling;

    public Barrel(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.width = 24;
        this.height = 24;
        this.speedX = 3.0;
        this.speedY = 0;
        this.direction = 1; // 1 = derecha, -1 = izquierda
        this.falling = false;
    }

    public void update() {
        // Movimiento horizontal constante según la dirección
        this.x += (int)(this.speedX * this.direction);

        // Si está cayendo, aplicamos gravedad vertical pura en línea recta
        if (falling) {
            this.speedY += 0.4;
            this.y += (int)this.speedY;
        } else {
            this.speedY = 0;
        }
    }

    public void reverseDirection() {
        this.direction *= -1;
    }

    public void setFalling(boolean falling) {
        this.falling = falling;
        if (falling && speedY == 0) {
            this.speedY = 1.0;
        }
    }

    public void setY(int y) { this.y = y; }
    public void setX(int x) { this.x = x; }
    
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public boolean isFalling() { return falling; }
    public double getSpeedY() { return speedY; }
    public void setSpeedY(double speedY) { this.speedY = speedY; }
    public void setSpeedX(double speedX) {
        this.speedX = speedX;
    }
};


