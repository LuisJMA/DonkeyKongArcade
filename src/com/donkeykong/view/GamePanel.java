package com.donkeykong.view;

import com.donkeykong.controller.GameController;
import com.donkeykong.model.Platform;
import com.donkeykong.model.Player;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends JPanel implements ActionListener {
    private Timer gameTimer;
    private Player player;
    private GameController controller;
    private List<Platform> platforms;

    public GamePanel() {
        setBackground(Color.BLACK);
        setFocusable(true);

        // Inicializar plataformas del escenario
        platforms = new ArrayList<>();
        platforms.add(new Platform(100, 500, 600, 20)); // Suelo principal
        platforms.add(new Platform(200, 380, 400, 20)); // Plataforma intermedia
        platforms.add(new Platform(100, 260, 400, 20)); // Plataforma superior

        // Inicializar jugador y controles
        player = new Player(120, 440);
        controller = new GameController(player);
        addKeyListener(controller);

        gameTimer = new Timer(16, this);
        gameTimer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        player.update(platforms);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.setColor(Color.WHITE);
        g.drawString("Usa A-D para moverte y W o Flecha Arriba para saltar", 50, 30);
        
        // Dibujar plataformas
        for (Platform p : platforms) {
            p.draw(g);
        }

        // Dibujar jugador
        player.draw(g);
    }
}