package com.sanchon;

import javax.swing.*;

public class Main {
    static JLabel countdownLabel;
    static JProgressBar progressBar;
    static JButton startButton;
    static JButton cancelButton;
    static JTextField secondsField;
    static CountdownThread[] countdownThreads = new CountdownThread[4];
    static MonitorThread monitorThread;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Apolo XI");
        frame.setSize(300, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        countdownLabel = new JLabel("Indique segundos (por fase):");
        countdownLabel.setBounds(50, 30, 200, 30);
        frame.add(countdownLabel);

        secondsField = new JTextField();
        secondsField.setBounds(50, 60, 200, 30);
        frame.add(secondsField);

        progressBar = new JProgressBar();
        progressBar.setBounds(50, 100, 200, 30);
        frame.add(progressBar);

        startButton = new JButton("Iniciar");
        startButton.setBounds(50, 140, 80, 30);
        frame.add(startButton);

        cancelButton = new JButton("Parar");
        cancelButton.setBounds(140, 140, 80, 30);
        frame.add(cancelButton);

        frame.setVisible(true);

        startButton.addActionListener(e -> startThreads());
        cancelButton.addActionListener(e -> stopThreads());
    }

    private static void startThreads() {
        new Thread(() -> {
            int seconds = Integer.parseInt(secondsField.getText());
            for (int i = 0; i < 4; i++) {
                countdownThreads[i] = new CountdownThread(seconds);
            }
            monitorThread = new MonitorThread(countdownThreads[0], "log.txt");
            monitorThread.start();
            for (int i = 0; i < 4; i++) {
                countdownThreads[i].setName("Fase:" + (i + 1));
                countdownThreads[i].start();
                monitorThread.setCountdownThread(countdownThreads[i]);
                try {
                    countdownThreads[i].join();
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }).start();
    }

    private static void stopThreads() {
        for (CountdownThread countdownThread : countdownThreads) {
            if (countdownThread != null) {
                countdownThread.interrupt();
            }
        }
    }
}