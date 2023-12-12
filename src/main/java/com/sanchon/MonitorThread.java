package com.sanchon;

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
            JOptionPane.showMessageDialog(null, "Apolo13 launched!");
        }
    }

    public void setCountdownThread(CountdownThread countdownThread) {
        this.countdownThread = countdownThread;
    }
}