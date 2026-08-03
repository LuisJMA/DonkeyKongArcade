package com.donkeykong.model;

import java.awt.Rectangle;

public class Ladder {
    private int x, y, width, height;

    public Ladder(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    // Método para verificar si el jugador está colisionando con la escalera
    public boolean intersects(Rectangle playerBounds) {
        return getBounds().intersects(playerBounds);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}
