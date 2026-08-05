package com.donkeykong.view;

import com.donkeykong.controller.GameController;
import com.donkeykong.model.Barrel;
import com.donkeykong.model.Item;
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

    private List<Barrel> barrels;
    private int barrelSpawnTimer = 0;
    private int gameTicks = 0;
    private final int MAX_GAME_TICKS = 70 * 60;
    private final int GRACE_PERIOD_TICKS = 10 * 60; 

    private List<Item> items = new ArrayList<>();
    private int itemSpawnTimer = 0;
    private int itemsCollected = 0;
    private final int TOTAL_ITEMS_TO_WIN = 12;

    public GamePanel() {
        setBackground(Color.BLACK);
        setFocusable(true);

        platforms = new ArrayList<>();
        platforms.add(new Platform(40, 520, 710, 560)); // Nivel 0 (Base)
        platforms.add(new Platform(150, 460, 720, 420)); // Nivel 1 
        platforms.add(new Platform(40, 320, 615, 360)); // Nivel 2 
        platforms.add(new Platform(150, 260, 720, 220)); // Nivel 3 
        platforms.add(new Platform(70, 120, 620, 160)); // Nivel 4 (Superior)

        ladders = new ArrayList<>();
        ladders.add(new Ladder(130, 460, 17, 65)); 
        ladders.add(new Ladder(620, 360, 17, 65));  
        ladders.add(new Ladder(130, 260, 17, 65)); 
        ladders.add(new Ladder(620, 160, 17, 65));  

        barrels = new ArrayList<>();
        player = new Player(670, 530, 14, 22);

        controller = new GameController(player);
        addKeyListener(controller);

        timer = new Timer(16, this);
        timer.start();
    }

    // Método auxiliar centralizado para reiniciar el juego de forma limpia y segura
    private void resetGame() {
        gameTicks = 0;
        itemsCollected = 0;
        itemSpawnTimer = 0;
        barrelSpawnTimer = 0;

        if (items == null) {
            items = new ArrayList<>();
        } else {
            items.clear();
        }

        if (barrels == null) {
            barrels = new ArrayList<>();
        } else {
            barrels.clear(); // Limpia y borra todos los barriles activos inmediatamente
        }

        player = new Player(670, 530, 14, 22); 
        controller = new GameController(player);
        addKeyListener(controller);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        boolean shouldReset = false;

        if (gameTicks < MAX_GAME_TICKS) {
            gameTicks++;
        } else {
            shouldReset = true; // Game Over por tiempo
        }

        if (gameTicks > GRACE_PERIOD_TICKS && !shouldReset) {
            player.update(platforms, ladders);

            if (player.hasFallenOffScreen(getHeight())) {
                player.loseLife(670, 530);
                if (player.getLives() <= 0) {
                    shouldReset = true;
                }
            }
        }

        int activeTicks = Math.max(0, gameTicks - GRACE_PERIOD_TICKS);
        int effectiveMaxTicks = 60 * 60;

        // --- APARICIÓN DE ÍTEMS ---
        if (!shouldReset && gameTicks > 0 && gameTicks % 600 == 0 && itemSpawnTimer != gameTicks) {
            itemSpawnTimer = gameTicks; 
            
            for (int i = 0; i < 2; i++) {
                Platform p = platforms.get((int)(Math.random() * platforms.size()));
                int randomX = Math.min(p.getX1(), p.getX2()) + (int)(Math.random() * Math.abs(p.getX2() - p.getX1()));
                int itemY = p.getYAt(randomX) - 15; 
                
                items.add(new Item(randomX, itemY));
            }
        }

        // --- COLISIÓN JUGADOR VS ÍTEMS ---
        if (!shouldReset) {
            java.util.Iterator<Item> itemIterator = items.iterator();
            Rectangle playerRect = new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight());

            while (itemIterator.hasNext()) {
                Item item = itemIterator.next();
                Rectangle itemRect = new Rectangle(item.getX(), item.getY(), item.getWidth(), item.getHeight());

                if (playerRect.intersects(itemRect)) {
                    itemIterator.remove();
                    itemsCollected++;
                    
                    if (itemsCollected >= TOTAL_ITEMS_TO_WIN) {
                        shouldReset = true;
                        break;
                    }
                }
            }
        }

        // --- GENERACIÓN DE BARRILES ---
        int currentSpawnInterval = (int) (100 - (55 * ((double) activeTicks / effectiveMaxTicks)));

        if (!shouldReset) {
            barrelSpawnTimer++;
            if (barrelSpawnTimer >= currentSpawnInterval) {
                Platform topPlatform = platforms.get(4);
                int startX = 100;
                int startY = topPlatform.getYAt(startX) - 24;
                
                Barrel newBarrel = new Barrel(startX, startY);
                newBarrel.setFalling(false);
                
                double progressiveSpeed = 3.0 + (2.5 * ((double) activeTicks / effectiveMaxTicks));
                newBarrel.setSpeedX(progressiveSpeed);

                barrels.add(newBarrel);
                barrelSpawnTimer = 0;
            }
        }

        // --- ACTUALIZACIÓN Y COLISIÓN DE BARRILES ---
        if (!shouldReset) {
            Rectangle playerRect = new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight());
            java.util.Iterator<Barrel> iterator = barrels.iterator();
            
            while (iterator.hasNext()) {
                Barrel barrel = iterator.next();
                barrel.update();

                Rectangle barrelRect = new Rectangle(barrel.getX(), barrel.getY(), barrel.getWidth(), barrel.getHeight());

                if (playerRect.intersects(barrelRect) && gameTicks > GRACE_PERIOD_TICKS) {
                    player.loseLife(670, 530);
                    iterator.remove(); 

                    if (player.getLives() <= 0) {
                        shouldReset = true;
                        break;
                    }
                    continue; 
                }

                boolean onAnyPlatform = false;
                int centerX = barrel.getX() + barrel.getWidth() / 2;

                if (!barrel.isFalling()) {
                    boolean removeThisBarrel = false;
                    for (int i = 0; i < platforms.size(); i++) {
                        Platform p = platforms.get(i);
                        int minX = Math.min(p.getX1(), p.getX2());
                        int maxX = Math.max(p.getX1(), p.getX2());

                        if (i == 0 && (centerX < minX || centerX > maxX)) {
                            removeThisBarrel = true;
                            break;
                        }

                        if (centerX >= minX && centerX <= maxX) {
                            int expectedY = p.getYAt(centerX);
                            
                            if (Math.abs((barrel.getY() + barrel.getHeight()) - expectedY) < 25) {
                                barrel.setY(expectedY - barrel.getHeight());
                                onAnyPlatform = true;
                                break;
                            }
                        }
                    }

                    if (removeThisBarrel) {
                        iterator.remove();
                        continue;
                    }

                    if (!onAnyPlatform) {
                        barrel.setFalling(true);
                    }
                } else {
                    for (int i = 0; i < platforms.size(); i++) {
                        Platform p = platforms.get(i);
                        int minX = Math.min(p.getX1(), p.getX2()) - 10; 
                        int maxX = Math.max(p.getX1(), p.getX2()) + 10; 

                        if (centerX >= minX && centerX <= maxX) {
                            int expectedY = p.getYAt(centerX);
                            
                            if (barrel.getY() + barrel.getHeight() >= expectedY && 
                                barrel.getY() + barrel.getHeight() <= expectedY + 25 && 
                                barrel.getSpeedY() > 0) {
                                
                                barrel.setY(expectedY - barrel.getHeight());
                                barrel.setFalling(false);
                                barrel.setSpeedY(0);
                                barrel.reverseDirection(); 
                                break;
                            }
                        }
                    }
                }
            }
        }

        // Si ocurrió un evento de reseteo o game over, se ejecuta aquí de forma segura fuera de los iteradores
        if (shouldReset) {
            resetGame();
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
        g2d.drawString("Ítems: " + itemsCollected + "/" + TOTAL_ITEMS_TO_WIN, 350, 55);

        if (gameTicks < GRACE_PERIOD_TICKS) {
            g2d.setColor(Color.YELLOW);
            g2d.drawString("¡PREPÁRATE!", 200, 55);
        } else {
            int activeTicks = gameTicks - GRACE_PERIOD_TICKS;
            int secondsPassed = activeTicks / 60;
            int timeLeft = Math.max(0, 60 - secondsPassed); 
            
            if (timeLeft <= 10) {
                g2d.setColor(Color.RED);
            } else {
                g2d.setColor(Color.WHITE);
            }
            
            g2d.drawString("Tiempo: " + timeLeft + "s", 200, 55);
        }

        g2d.setColor(Color.YELLOW);
        for (Item item : items) {
            g2d.fillOval(item.getX(), item.getY(), item.getWidth(), item.getHeight());
        }

        g2d.setColor(new Color(180, 180, 180));
        for (Ladder ladder : ladders) {
            g2d.fillRect(ladder.getX(), ladder.getY(), ladder.getWidth(), ladder.getHeight());
        }

        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(8)); 
        for (Platform p : platforms) {
            g2d.drawLine(p.getX1(), p.getY1(), p.getX2(), p.getY2());
        }

        g2d.setColor(new Color(139, 69, 19));
        for (Barrel barrel : barrels) {
            g2d.fillOval(barrel.getX(), barrel.getY(), barrel.getWidth(), barrel.getHeight());
        }

        g2d.setColor(Color.RED);
        g2d.fillRect(player.getX(), player.getY(), player.getWidth(), player.getHeight());
    }
}