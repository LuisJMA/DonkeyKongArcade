package com.donkeykong.view;

import com.donkeykong.controller.GameController;
import com.donkeykong.model.Barrel;
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
    private List<Ladder> ladders; // Se mantiene solo para que el jugador suba si gustas
    private GameController controller;

    private List<Barrel> barrels;
    private int barrelSpawnTimer = 0;
    private int gameTicks = 0;
    private final int MAX_GAME_TICKS = 60 * 60;

    public GamePanel() {
        setBackground(Color.BLACK);
        setFocusable(true);

        // Definimos las plataformas como rampas inclinadas en zigzag
        // Definición de las plataformas (manteniendo tus inclinaciones actuales)
        platforms = new ArrayList<>();
        platforms.add(new Platform(70, 520, 690, 560)); // Nivel 0 (Base)
        platforms.add(new Platform(120, 460, 690, 420)); // Nivel 1 
        platforms.add(new Platform(70, 320, 636, 360)); // Nivel 2 
        platforms.add(new Platform(120, 260, 690, 220)); // Nivel 3 
        platforms.add(new Platform(70, 120, 636, 160)); // Nivel 4 (Superior)

        // Escaleras cuadradas perfectamente en los extremos de las rampas
        ladders = new ArrayList<>();
        ladders.add(new Ladder(120, 460, 17, 65)); // Conecta el extremo derecho del Nivel 0 con el Nivel 1
        ladders.add(new Ladder(620, 360, 17, 60));  // Conecta el extremo izquierdo del Nivel 1 con el Nivel 2
        ladders.add(new Ladder(120, 260, 17, 65)); // Conecta el extremo derecho del Nivel 2 con el Nivel 3
        ladders.add(new Ladder(620, 160, 17, 65));  // Conecta el extremo izquierdo del Nivel 3 con el Nivel Superior

        barrels = new ArrayList<>();
        player = new Player(670, 480, 14, 22);

        controller = new GameController(player);
        addKeyListener(controller);

        timer = new Timer(16, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        player.update(platforms, ladders);

        // Incrementamos el contador del tiempo de juego (máximo 60 segundos)
        if (gameTicks < MAX_GAME_TICKS) {
            gameTicks++;
        }

        barrelSpawnTimer++;
        if (barrelSpawnTimer >= 90) { // Frecuencia ajustada de salida de barriles
            Platform topPlatform = platforms.get(4);
            int startX = 100;
            int startY = topPlatform.getYAt(startX) - 24;
            
            Barrel newBarrel = new Barrel(startX, startY);
            newBarrel.setFalling(false);
            
            // Aumento progresivo de la velocidad del barril basado en los 60 segundos máximos
            double progressiveSpeed = 3.0 + (2.5 * ((double) gameTicks / MAX_GAME_TICKS));
            newBarrel.setSpeedX(progressiveSpeed);

            barrels.add(newBarrel);
            barrelSpawnTimer = 0;
        }

        java.util.Iterator<Barrel> iterator = barrels.iterator();
        while (iterator.hasNext()) {
            Barrel barrel = iterator.next();
            barrel.update();

            boolean onAnyPlatform = false;
            int centerX = barrel.getX() + barrel.getWidth() / 2;

            // 1. SI ESTÁ RODANDO SOBRE UNA RAMPA
            if (!barrel.isFalling()) {
                for (int i = 0; i < platforms.size(); i++) {
                    Platform p = platforms.get(i);
                    int minX = Math.min(p.getX1(), p.getX2());
                    int maxX = Math.max(p.getX1(), p.getX2());

                    // Si está en la plataforma base (índice 0) y sale de los límites, se destruye
                    if (i == 0 && (centerX < minX || centerX > maxX)) {
                        iterator.remove();
                        break;
                    }

                    // Si el centro está estrictamente dentro del rango horizontal de la rampa
                    if (centerX >= minX && centerX <= maxX) {
                        int expectedY = p.getYAt(centerX);
                        
                        // Mantenemos el barril pegado a la pendiente
                        if (Math.abs((barrel.getY() + barrel.getHeight()) - expectedY) < 25) {
                            barrel.setY(expectedY - barrel.getHeight());
                            onAnyPlatform = true;
                            break;
                        }
                    }
                }

                // En cuanto el centro del barril rebase el ancho de la rampa, se activa la caída libre vertical
                if (!onAnyPlatform) {
                    barrel.setFalling(true);
                }
            }
            // 2. SI ESTÁ CAYENDO EN LÍNEA RECTA AL VACÍO
            else {
                for (int i = 0; i < platforms.size(); i++) {
                    Platform p = platforms.get(i);
                    int minX = Math.min(p.getX1(), p.getX2()) - 10; // Margen extra a la izquierda
                    int maxX = Math.max(p.getX1(), p.getX2()) + 10; // Margen extra a la derecha

                    // Si cae dentro del ancho horizontal (con margen) de la plataforma inferior
                    if (centerX >= minX && centerX <= maxX) {
                        int expectedY = p.getYAt(centerX);
                        
                        // Si la base del barril toca o reasa ligeramente la altura de la rampa bajando
                        if (barrel.getY() + barrel.getHeight() >= expectedY && 
                            barrel.getY() + barrel.getHeight() <= expectedY + 25 && 
                            barrel.getSpeedY() > 0) {
                            
                            barrel.setY(expectedY - barrel.getHeight());
                            barrel.setFalling(false);
                            barrel.setSpeedY(0);
                            barrel.reverseDirection(); // Cambia de sentido para rodar en dirección contraria
                            break;
                        }
                    }
                }
            }
        }

        if (player.hasFallenOffScreen(getHeight())) {
            player.loseLife(670, 480);
            if (player.getLives() <= 0) {
                player = new Player(670, 480, 14, 22);
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

        g2d.setColor(Color.WHITE);
        g2d.drawString("Usa A-D para moverte y W para saltar", 50, 30);
        g2d.drawString("Vidas: " + player.getLives(), 50, 55);

        // Dibujar escaleras (opcional para el jugador)
        g2d.setColor(new Color(180, 180, 180));
        for (Ladder ladder : ladders) {
            g2d.fillRect(ladder.getX(), ladder.getY(), ladder.getWidth(), ladder.getHeight());
        }

        // Dibujar plataformas inclinadas (líneas o rampas)
        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(8)); // Grosor para que parezcan barras de rampa
        for (Platform p : platforms) {
            g2d.drawLine(p.getX1(), p.getY1(), p.getX2(), p.getY2());
        }

        // Dibujar barriles
        g2d.setColor(new Color(139, 69, 19));
        for (Barrel barrel : barrels) {
            g2d.fillOval(barrel.getX(), barrel.getY(), barrel.getWidth(), barrel.getHeight());
        }

        // Dibujar jugador
        g2d.setColor(Color.RED);
        g2d.fillRect(player.getX(), player.getY(), player.getWidth(), player.getHeight());
    }
}