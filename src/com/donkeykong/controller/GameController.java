package com.donkeykong.controller;

import com.donkeykong.model.Player;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameController extends KeyAdapter {
    private Player player;

    public GameController(Player player) {
        this.player = player;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_A) {
            player.setXVelocity(-3);
        }
        if (key == KeyEvent.VK_D) {
            player.setXVelocity(3);
        }
        if (key == KeyEvent.VK_W) {
            if (player.isClimbing()) {
                player.climbUp();
            } else {
                player.climbUp(); // Intenta engancharse a escalera si la toca
                player.jump();    // Si no, ejecuta salto normal
            }
        }
        if (key == KeyEvent.VK_S) {
            player.climbDown();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_A || key == KeyEvent.VK_D) {
            player.setXVelocity(0);
        }
    }
}