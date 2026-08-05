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
    private final int MAX_GAME_TICKS = 70 * 60;
    private final int GRACE_PERIOD_TICKS = 10 * 60; // Los primeros 300 ticks (5 segundos)

    public GamePanel() {
        setBackground(Color.BLACK);
        setFocusable(true);

        // Definimos las plataformas como rampas inclinadas en zigzag
        
        platforms = new ArrayList<>();
        platforms.add(new Platform(40, 520, 710, 560)); // Nivel 0 (Base)
        platforms.add(new Platform(150, 460, 720, 420)); // Nivel 1 
        platforms.add(new Platform(40, 320, 615, 360)); // Nivel 2 
        platforms.add(new Platform(150, 260, 720, 220)); // Nivel 3 
        platforms.add(new Platform(70, 120, 620, 160)); // Nivel 4 (Superior)

        // Escaleras cuadradas perfectamente en los extremos de las rampas
        ladders = new ArrayList<>();
        ladders.add(new Ladder(130, 460, 17, 65)); // Conecta el extremo derecho del Nivel 0 con el Nivel 1
        ladders.add(new Ladder(620, 360, 17, 65));  // Conecta el extremo izquierdo del Nivel 1 con el Nivel 2
        ladders.add(new Ladder(130, 260, 17, 65)); // Conecta el extremo derecho del Nivel 2 con el Nivel 3
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
        // Incrementamos el contador del tiempo de juego
        if (gameTicks < MAX_GAME_TICKS) {
            gameTicks++;
        } else {
            // Si el tiempo llega al límite, se acaba el juego por completo (Game Over por tiempo)
            gameTicks = 0;
            player = new Player(670, 480, 14, 22); 
            barrels.clear(); 
            controller = new GameController(player);
            addKeyListener(controller);
        }

        // EL JUGADOR SOLO SE ACTUALIZA SI YA PASÓ EL PERIODO DE GRACIA (Primeros 5 segundos)
        if (gameTicks > GRACE_PERIOD_TICKS) {
            player.update(platforms, ladders);

            // Si el jugador cae fuera de la pantalla, consume una vida
            if (player.hasFallenOffScreen(getHeight())) {
                player.loseLife(670, 480);
                if (player.getLives() <= 0) {
                    player = new Player(670, 480, 14, 22);
                    controller = new GameController(player);
                    addKeyListener(controller);
                }
            }
        }

        // GENERACIÓN DE BARRILES (Empieza de inmediato para llenar las rampas)
        // Calculamos el progreso basado estrictamente en los 60 segundos de juego activo
        int activeTicks = Math.max(0, gameTicks - GRACE_PERIOD_TICKS);
        int effectiveMaxTicks = 60 * 60;

        // Intervalo de spawn dinámico progresivo
        int currentSpawnInterval = (int) (100 - (55 * ((double) activeTicks / effectiveMaxTicks)));

        barrelSpawnTimer++;
        if (barrelSpawnTimer >= currentSpawnInterval) {
            Platform topPlatform = platforms.get(4);
            int startX = 100;
            int startY = topPlatform.getYAt(startX) - 24;
            
            Barrel newBarrel = new Barrel(startX, startY);
            newBarrel.setFalling(false);
            
            // Aumento progresivo de la velocidad del barril
            double progressiveSpeed = 3.0 + (2.5 * ((double) activeTicks / effectiveMaxTicks));
            newBarrel.setSpeedX(progressiveSpeed);

            barrels.add(newBarrel);
            barrelSpawnTimer = 0;
        }

        java.util.Iterator<Barrel> iterator = barrels.iterator();
        while (iterator.hasNext()) {
            Barrel barrel = iterator.next();
            barrel.update();

            // --- NUEVO: COLISIÓN BARRIL VS JUGADOR ---
            // Creamos rectángulos de colisión aproximados para ambos
            Rectangle playerRect = new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight());
            Rectangle barrelRect = new Rectangle(barrel.getX(), barrel.getY(), barrel.getWidth(), barrel.getHeight());

            if (playerRect.intersects(barrelRect)) {
                // El jugador choca con un barril: pierde una vida y se reinicia su posición
                player.loseLife(670, 480);
                iterator.remove(); // Destruimos el barril que impactó

                // Si se queda sin vidas, se reinicia el juego por completo
                if (player.getLives() <= 0) {
                    player = new Player(670, 480, 14, 22);
                    controller = new GameController(player);
                    addKeyListener(controller);
                }
                continue; // Pasamos al siguiente barril para evitar errores en el iterador
            }
            // ------------------------------------------

            

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

        repaint();
    }



    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.WHITE);
        g2d.drawString("Usa A-D para moverte y W para saltar", 50, 30);
        g2d.drawString("Vidas: " + player.getLives(), 50, 55);

        // --- CÁLCULO Y PINTADO DEL TEMPORIZADOR CON PERIODO DE GRACIA ---
        if (gameTicks < GRACE_PERIOD_TICKS) {
            g2d.setColor(Color.YELLOW);
            g2d.drawString("¡PREPÁRATE!", 200, 55);
        } else {
            int activeTicks = gameTicks - GRACE_PERIOD_TICKS;
            int secondsPassed = activeTicks / 60;
            int timeLeft = Math.max(0, 60 - secondsPassed); // Cuenta regresiva de 60 a 0
            
            // Si quedan 10 segundos o menos, cambia a rojo
            if (timeLeft <= 10) {
                g2d.setColor(Color.RED);
            } else {
                g2d.setColor(Color.WHITE);
            }
            
            g2d.drawString("Tiempo: " + timeLeft + "s", 200, 55);
        }
        // ----------------------------------------------------------------

        // Dibujar escaleras (opcional para el jugador)
        g2d.setColor(new Color(180, 180, 180));
        for (Ladder ladder : ladders) {
            g2d.fillRect(ladder.getX(), ladder.getY(), ladder.getWidth(), ladder.getHeight());
        }

        // Dibujar plataformas inclinadas (líneas o rampas)
        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(8)); 
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