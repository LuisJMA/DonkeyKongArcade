package com.donkeykong.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MenuPanel extends JPanel {
    private JButton startButton;
    private JButton exitButton;

    public MenuPanel(ActionListener startListener, ActionListener exitListener) {
        setBackground(Color.BLACK);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel titleLabel = new JLabel("DONKEY KONG", JLabel.CENTER);
        titleLabel.setForeground(Color.RED);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 40, 0);
        add(titleLabel, gbc);

        startButton = new JButton("Iniciar Juego");
        styleButton(startButton);
        startButton.addActionListener(startListener);
        
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 15, 0);
        add(startButton, gbc);

        exitButton = new JButton("Salir");
        styleButton(exitButton);
        exitButton.addActionListener(exitListener);
        
        gbc.gridy = 2;
        add(exitButton, gbc);
    }

    private void styleButton(JButton button) {
        button.setBackground(Color.RED);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 40));
    }
}