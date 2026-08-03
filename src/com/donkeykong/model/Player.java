package com.donkeykong.model;

import java.awt.Rectangle;
import java.util.List;

public class Player {
    private int x, y, width, height;
    private int xVelocity = 0;
    private double yVelocity = 0;
    private int lives = 3;
    
    private final double GRAVITY = 0.5;
    private final double JUMP_STRENGTH = -6.5;
    private boolean isOnGround = false;
    private boolean jumpRequested = false;

    private boolean isOnLadder = false;
    private boolean isClimbing = false;
    private Ladder currentLadder = null;

    public Player(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void update(List<Platform> platforms, List<Ladder> ladders) {
        // Verificar colisión con escaleras
        isOnLadder = false;
        for (Ladder ladder : ladders) {
            if (getBounds().intersects(ladder.getBounds())) {
                isOnLadder = true;
                currentLadder = ladder;
                break;
            }
        }

        if (!isOnLadder) {
            isClimbing = false;
            currentLadder = null;
        }

        if (isClimbing) {
            yVelocity = 0;
            if (currentLadder != null) {
                // Centrar horizontalmente al jugador en la escalera y bloquear movimiento horizontal externo
                x = currentLadder.getX() + (currentLadder.getWidth() - width) / 2;
            }
        } else {
            // Movimiento horizontal (A y D) solo cuando NO está escalando
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

            isOnGround = false;
            yVelocity += GRAVITY;
            y += (int) yVelocity;

            // Colisiones verticales con plataformas (SOLO CUANDO NO ESTÁ ESCALANDO)
            for (Platform p : platforms) {
                Rectangle playerRect = getBounds();
                Rectangle platRect = new Rectangle(p.getX(), p.getY(), p.getWidth(), p.getHeight());

                if (playerRect.intersects(platRect)) {
                    if (yVelocity > 0) {
                        y = p.getY() - height;
                        yVelocity = 0;
                        isOnGround = true;
                    } else if (yVelocity < 0) {
                        y = p.getY() + p.getHeight();
                        yVelocity = 0;
                    }
                }
            }

            if (jumpRequested && isOnGround) {
                yVelocity = JUMP_STRENGTH;
                isOnGround = false;
                jumpRequested = false;
            }
        }

        if (x < 0) x = 0;
        if (x > 800 - width) x = 800 - width;
    }

    public void climbUp() {
        if (isOnLadder) {
            isClimbing = true;
            y -= 3; // Velocidad de subida

            // Si llega arriba de la escalera, sale de ella y se posicione justo encima
            if (currentLadder != null && y < currentLadder.getY()) {
                isClimbing = false;
                y = currentLadder.getY() - height;
                currentLadder = null;
            }
        }
    }

    public void climbDown() {
        if (isOnLadder) {
            isClimbing = true;
            y += 3; // Velocidad de bajada
            if (currentLadder != null && y > currentLadder.getY() + currentLadder.getHeight()) {
                isClimbing = false;
                currentLadder = null;
            }
        }
    }

    public void jump() {
        if (isClimbing) {
            isClimbing = false;
            currentLadder = null;
            yVelocity = JUMP_STRENGTH;
            isOnGround = false;
        } else if (isOnGround) {
            yVelocity = JUMP_STRENGTH;
            isOnGround = false;
            jumpRequested = false;
        } else {
            jumpRequested = true;
        }
    }

    public void setXVelocity(int vel) {
        if (!isClimbing) {
            this.xVelocity = vel;
        }
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
        this.isClimbing = false;
        this.currentLadder = null;
    }

    public boolean isClimbing() { return isClimbing; }
    public int getLives() { return lives; }
    public Rectangle getBounds() { return new Rectangle(x, y, width, height); }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}