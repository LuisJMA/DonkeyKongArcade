package com.donkeykong.view;

import javax.swing.*;

import com.donkeykong.audio.SoundManager;

import java.awt.*;

public class MainFrame extends JFrame implements GameEventListener {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private CardLayout cardLayout;
    private JPanel mainContainer;
    private MenuPanel menuPanel;
    private GamePanel gamePanel;

    private SoundManager soundManager;

    public MainFrame() {
        setTitle("Donkey Kong Arcade");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        soundManager = new SoundManager();

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        // 1. Crear el Menú con sus acciones
        menuPanel = new MenuPanel(
            e -> startGame(),       // Acción para el botón "Iniciar Juego"
            e -> System.exit(0)     // Acción para el botón "Salir"
        );

        mainContainer.add(menuPanel, "MENU");
        add(mainContainer);
        
        cardLayout.show(mainContainer, "MENU");
        soundManager.playBackgroundMusic("/com/donkeykong/audio/menu.wav");
    }

    private void startGame() {

        soundManager.stopBackgroundMusic();

        // Creamos el panel de juego pasándole este frame como listener
        gamePanel = new GamePanel(this);
        mainContainer.add(gamePanel, "GAME");
        
        cardLayout.show(mainContainer, "GAME");
        gamePanel.requestFocusInWindow(); // Asegura que las teclas funcionen inmediatamente

        soundManager.playBackgroundMusic("/com/donkeykong/audio/background.wav");
    }

    @Override
    public void onGameOver(String message) {
        soundManager.stopBackgroundMusic();
        JOptionPane.showMessageDialog(this, message, "Fin del Juego", JOptionPane.ERROR_MESSAGE);
        cardLayout.show(mainContainer, "MENU");

        soundManager.playBackgroundMusic("/com/donkeykong/audio/menu.wav");
    }

    @Override
    public void onVictory(String message) {
        soundManager.stopBackgroundMusic();
        JOptionPane.showMessageDialog(this, message, "¡Victoria!", JOptionPane.INFORMATION_MESSAGE);
        cardLayout.show(mainContainer, "MENU");

        soundManager.playBackgroundMusic("/com/donkeykong/audio/menu.wav");

    }

    @Override
    public void onReturnToMenu() {
        soundManager.stopBackgroundMusic();
        cardLayout.show(mainContainer, "MENU");

        soundManager.playBackgroundMusic("/com/donkeykong/audio/menu.wav");
    }
}