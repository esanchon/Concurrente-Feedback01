package com.sanchon;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

class MonitorThread extends Thread {
    private PrintWriter logWriter;
    private CountdownThread countdownThread;
    private static final Logger LOGGER = Logger.getLogger(MonitorThread.class.getName());

    public MonitorThread(CountdownThread countdownThread, String logFileName) {
        this.countdownThread = countdownThread;
        try {
            this.logWriter = new PrintWriter(new FileWriter(logFileName, true));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error opening log file", e);
        }
    }

    @Override
    public void run() {
        while (countdownThread.getStatus().equals("active")) {
            logStatus("ACTIVE");
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                LOGGER.log(Level.SEVERE, "Error sleeping", ex);
                logStatus("INTERRUPTED");
            }
        }
        logStatus(countdownThread.getStatus().toUpperCase());
        logWriter.close();
    }

    private void logStatus(String status) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String message = timestamp + " - " + countdownThread.getName() + " - Estado:" + status + " - Segundos restantes: " + countdownThread.getSecondsLeft();
        logWriter.println(message);
        System.out.println(message);
        if (status.equals("CANCELLED")) {
            JOptionPane.showMessageDialog(null, "Launch cancelled!");
        } else if (status.equals("COMPLETED")) {
               JOptionPane.showMessageDialog(null, "Launch completed!");
                new JFXPanel();
                Platform.runLater(() -> {
                    Pane root = new Pane();
                    Scene scene = new Scene(root, 300, 250);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.setTitle("Apolo XI");
                    stage.show();
                    Image image = new Image("/images/apollo11.png");
                    ImageView imageView = new ImageView(image);
                    root.getChildren().add(imageView);
                    Media sound = new Media(getClass().getResource("/sounds/success-fanfare-trumpets-6185.mp3").toExternalForm());
                    MediaPlayer mediaPlayer = new MediaPlayer(sound);
                    mediaPlayer.play();
                });
        }
    }

    public void setCountdownThread(CountdownThread countdownThread) {
        this.countdownThread = countdownThread;
    }
}