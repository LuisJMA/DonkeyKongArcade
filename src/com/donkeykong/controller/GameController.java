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

        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            player.setXSpeed(-4);
        } else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            player.setXSpeed(4);
        } else if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
            player.setYSpeed(-4);
        } else if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
            player.setYSpeed(4);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A || key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            player.setXSpeed(0);
        }
        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W || key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
            player.setYSpeed(0);
        }
    }
}