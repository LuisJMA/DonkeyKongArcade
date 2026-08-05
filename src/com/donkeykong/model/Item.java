package com.donkeykong.model;

public class Item {
    private int x, y;
    private int width = 12;
    private int height = 12;

    public Item(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}