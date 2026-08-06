package com.donkeykong.model;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
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

    private BufferedImage spriteSheet; // NUEVO: Hoja de sprites

    public Player(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        loadSpriteSheet(); // NUEVO: Carga la imagen al instanciar
    }

    private void loadSpriteSheet() {
        try {
            spriteSheet = ImageIO.read(getClass().getResourceAsStream("/com/donkeykong/images/player.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("No se pudo cargar la imagen del jugador.");
        }
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
                x = currentLadder.getX() + (currentLadder.getWidth() - width) / 2;
            }
        } else {
            // Movimiento horizontal
            x += xVelocity;

            isOnGround = false;
            yVelocity += GRAVITY;
            y += (int) yVelocity;

            // Colisiones con plataformas inclinadas (Rampas)
            for (Platform p : platforms) {
                if (p.containsX(x + width / 2)) {
                    int expectedY = p.getYAt(x + width / 2);
                    
                    // Si el jugador cae sobre la rampa o está caminando cerca de ella
                    if (yVelocity >= 0 && y + height >= expectedY && y + height <= expectedY + 15) {
                        y = expectedY - height;
                        yVelocity = 0;
                        isOnGround = true;
                        break;
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
            y -= 3;
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
            y += 3;
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
    public BufferedImage getSpriteSheet() { return spriteSheet; } // NUEVO GETTER
}