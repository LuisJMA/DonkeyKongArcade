package com.donkeykong.view;

public interface GameEventListener {
    void onGameOver(String message);
    void onVictory(String message);
    void onReturnToMenu();
}