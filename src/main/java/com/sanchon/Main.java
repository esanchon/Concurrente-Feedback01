package com.sanchon;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    static JLabel countdownLabel;
    static JProgressBar progressBar;
    static JButton startButton;
    static JButton cancelButton;
    static CountdownThread countdownThread;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Apollo 11 Launch");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        countdownLabel = new JLabel("Enter seconds:");
        countdownLabel.setBounds(50, 30, 200, 30);
        frame.add(countdownLabel);

        progressBar = new JProgressBar();
        progressBar.setBounds(50, 70, 200, 30);
        frame.add(progressBar);

        startButton = new JButton("Start");
        startButton.setBounds(50, 110, 80, 30);
        frame.add(startButton);

        cancelButton = new JButton("Cancel");
        cancelButton.setBounds(140, 110, 80, 30);
        frame.add(cancelButton);

        frame.setVisible(true);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int seconds = Integer.parseInt(JOptionPane.showInputDialog("Enter countdown seconds:"));
                countdownThread = new CountdownThread(seconds);
                countdownThread.start();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (countdownThread != null) {
                    countdownThread.interrupt();
                }
            }
        });
    }
}

class CountdownThread extends Thread {
    private int seconds;

    public CountdownThread(int seconds) {
        this.seconds = seconds;
    }

    @Override
    public void run() {
        for (int i = seconds; i >= 0; i--) {
            if (Thread.interrupted()) {
                JOptionPane.showMessageDialog(null, "Launch cancelled!");
                return;
            }
            Main.countdownLabel.setText("Countdown: " + i);
            Main.progressBar.setValue((int) ((double) i / seconds * 100));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                JOptionPane.showMessageDialog(null, "Launch cancelled!");
                return;
            }
        }
        JOptionPane.showMessageDialog(null, "Launch successful!");
    }
}