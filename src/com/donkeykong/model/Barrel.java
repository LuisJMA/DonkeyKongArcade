package com.donkeykong.model;

public class Barrel {
    private int x, y;
    private int width, height;
    private double speedX;
    private double speedY;
    private double speedIncrement; // Para hacer que aumente progresivamente
    private int direction; // 1 para derecha, -1 para izquierda
    private boolean falling;

    public Barrel(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.width = 24;
        this.height = 24;
        this.speedX = 3.0; // Velocidad base inicial
        this.speedY = 0;
        this.speedIncrement = 0.0005; // Incremento sutil por frame
        this.direction = 1; 
        this.falling = false;
    }

    public void update() {
        // Aumento progresivo de la velocidad horizontal
        this.speedX += this.speedIncrement;

        // Movimiento horizontal según la dirección actual
        this.x += (int)(this.speedX * this.direction);

        // Simulación básica de gravedad si está cayendo por un vacío
        if (falling) {
            this.speedY += 0.4; // Aceleración de gravedad
            this.y += (int)this.speedY;
        } else {
            this.speedY = 0;
        }
    }

    // Métodos para cambiar de dirección o manejar colisiones
    public void reverseDirection() {
        this.direction *= -1;
    }

    public void setFalling(boolean falling) {
        this.falling = falling;
        if (falling) {
            this.speedY = 1.0; // Impulso inicial al caer
        }
    }

    // Setter necesario para ajustar la posición vertical exacta al colisionar con plataformas
    public void setY(int y) {
        this.y = y;
    }

    // Getters necesarios para la vista (GamePanel / Renderer)
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}