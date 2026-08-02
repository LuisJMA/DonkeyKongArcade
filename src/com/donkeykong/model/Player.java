package com.donkeykong.model;

import java.awt.Color;
import java.awt.Graphics;

public class Player {
    private int x, y;
    private int width, height;
    private int xSpeed, ySpeed;

    public Player(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.width = 30;
        this.height = 40;
        this.xSpeed = 0;
        this.ySpeed = 0;
    }

    public void update() {
        x += xSpeed;
        y += ySpeed;
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, width, height); // Representación temporal del jugador
    }

    public void setXSpeed(int xSpeed) {
        this.xSpeed = xSpeed;
    }

    public void setYSpeed(int ySpeed) {
        this.ySpeed = ySpeed;
    }
}