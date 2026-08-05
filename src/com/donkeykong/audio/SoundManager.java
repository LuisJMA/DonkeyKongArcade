package com.donkeykong.audio;

import javax.sound.sampled.*;
import java.io.InputStream;

public class SoundManager {
    private Clip backgroundClip;

    public void playBackgroundMusic(String audioFilePath) {
        try {
            // Carga el archivo de audio de manera segura desde los recursos
            InputStream audioSrc = getClass().getResourceAsStream(audioFilePath);
            if (audioSrc == null) {
                System.out.println("No se pudo encontrar el archivo de audio: " + audioFilePath);
                return;
            }
            
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioSrc);
            backgroundClip = AudioSystem.getClip();
            backgroundClip.open(audioStream);
            
            // Reproducir en bucle continuo
            backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
            backgroundClip.start();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopBackgroundMusic() {
        if (backgroundClip != null && backgroundClip.isRunning()) {
            backgroundClip.stop();
            backgroundClip.close();
        }
    }
}