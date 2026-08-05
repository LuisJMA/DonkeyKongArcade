package com.donkeykong.model;

public class Platform {
    private int x1, y1, x2, y2; // Coordenadas inicial y final para la pendiente

    public Platform(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    // Calcula la altura exacta de la rampa en un punto X dado
    public int getYAt(int x) {
        if (x < Math.min(x1, x2) || x > Math.max(x1, x2)) {
            return -1; // Fuera del rango de la plataforma
        }
        if (x1 == x2) return y1;
        
        double slope = (double)(y2 - y1) / (x2 - x1);
        return (int)(y1 + slope * (x - x1));
    }

    public boolean containsX(int x) {
        return x >= Math.min(x1, x2) && x <= Math.max(x1, x2);
    }

    public int getX1() { return x1; }
    public int getY1() { return y1; }
    public int getX2() { return x2; }
    public int getY2() { return y2; }
}