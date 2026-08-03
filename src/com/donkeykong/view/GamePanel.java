package com.donkeykong.view;

import com.donkeykong.controller.GameController;
import com.donkeykong.model.Ladder;
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
    private List<Ladder> ladders;
    private GameController controller;

    public GamePanel() {
        setBackground(Color.BLACK);
        setFocusable(true);

        // Inicializar las 5 plataformas estilo zigzag clásico con menor ancho (más espacio libre)
        platforms = new ArrayList<>();
        platforms.add(new Platform(70, 550, 620, 10));  // Suelo base (ancho para moverse al inicio)
        platforms.add(new Platform(70, 430, 625, 10)); // Plataforma 1
        platforms.add(new Platform(70, 310, 620, 10)); // Plataforma 2
        platforms.add(new Platform(70, 205, 610, 10)); // Plataforma 3
        platforms.add(new Platform(70, 95, 600, 10));  // Plataforma superior (Donkey Kong)

        // Inicializar las escaleras conectando las plataformas
        ladders = new ArrayList<>();
        ladders.add(new Ladder(150, 440, 17, 110)); // Del suelo base a la Plataforma 1
        ladders.add(new Ladder(550, 320, 17, 110)); // De Plataforma 1 a Plataforma 2
        ladders.add(new Ladder(200, 215, 17, 95));  // De Plataforma 2 a Plataforma 3
        ladders.add(new Ladder(500, 105, 17, 100)); // De Plataforma 3 a la superior

        // Ajustamos la posición inicial del jugador al suelo base
        player = new Player(80, 480, 14, 22);

        // Controlador de teclado
        controller = new GameController(player);
        addKeyListener(controller);

        // Bucle del juego a 60 FPS (16 ms)
        timer = new Timer(16, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        player.update(platforms, ladders);

        // Verificar si cayó al vacío
        if (player.hasFallenOffScreen(getHeight())) {
            player.loseLife(80, 480);
            if (player.getLives() <= 0) {
                // Aquí manejaremos el Game Over definitivo más adelante
                // Por ahora reinicia las vidas para pruebas continuas
                player = new Player(80, 480, 14, 22);
                controller = new GameController(player);
                addKeyListener(controller);
            }
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Renderizar instrucciones y estado
        g2d.setColor(Color.WHITE);
        g2d.drawString("Usa A-D para moverte y W para saltar, Usa E para subir escaleras", 50, 30);
        g2d.drawString("Vidas: " + player.getLives(), 50, 55);

        // Renderizar escaleras
        g2d.setColor(new Color(180, 180, 180));
        for (Ladder ladder : ladders) {
            g2d.fillRect(ladder.getX(), ladder.getY(), ladder.getWidth(), ladder.getHeight());
        }

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