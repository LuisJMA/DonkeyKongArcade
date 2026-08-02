package com.donkeykong.view;

import com.donkeykong.controller.GameController;
import com.donkeykong.model.Player;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GamePanel extends JPanel implements ActionListener {
    private Timer gameTimer;
    private Player player;
    private GameController controller;

    public GamePanel() {
        setBackground(Color.BLACK);
        setFocusable(true);

        player = new Player(100, 450);
        controller = new GameController(player);
        addKeyListener(controller);

        gameTimer = new Timer(16, this);
        gameTimer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        player.update();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.setColor(Color.WHITE);
        g.drawString("Mueve al personaje con las flechas o W-A-S-D", 50, 50);
        
        player.draw(g);
    }
}