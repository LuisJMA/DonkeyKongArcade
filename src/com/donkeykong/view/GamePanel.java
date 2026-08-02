package com.donkeykong.view;

import com.donkeykong.controller.GameController;
import com.donkeykong.model.Platform;
import com.donkeykong.model.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends JPanel implements ActionListener {
    private Timer timer;
    private Player player;
    private List<Platform> platforms;
    private GameController controller;

    public GamePanel() {
        setBackground(Color.BLACK);
        setFocusable(true);

        // Inicializar las 5 plataformas clásicas estilo arcade
        platforms = new ArrayList<>();
        platforms.add(new Platform(50, 520, 700, 25)); // Suelo base
        platforms.add(new Platform(100, 430, 600, 20)); // Plataforma 1
        platforms.add(new Platform(50, 340, 600, 20));  // Plataforma 2
        platforms.add(new Platform(100, 250, 600, 20)); // Plataforma 3
        platforms.add(new Platform(150, 160, 500, 20)); // Plataforma superior

        // Jugador proporcional
        player = new Player(80, 470, 24, 32);

        // Controlador de teclado
        controller = new GameController(player);
        addKeyListener(controller);

        // Bucle del juego a 60 FPS (16 ms)
        timer = new Timer(16, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        player.update(platforms);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Renderizar instrucciones
        g2d.setColor(Color.WHITE);
        g2d.drawString("Usa A-D para moverte y W o Flecha Arriba para saltar", 50, 30);

        // Renderizar plataformas
        g2d.setColor(Color.CYAN);
        for (Platform p : platforms) {
            g2d.fillRect(p.getX(), p.getY(), p.getWidth(), p.getHeight());
        }

        // Renderizar jugador
        g2d.setColor(Color.RED);
        g2d.fillRect(player.getX(), player.getY(), player.getWidth(), player.getHeight());
    }
}