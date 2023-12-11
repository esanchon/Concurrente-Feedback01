package com.sanchon;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    static JLabel countdownLabel;
    static JProgressBar progressBar;
    static JButton startButton;
    static JButton cancelButton;
    static JTextField secondsField;
    static CountdownThread countdownThread;
    static MonitorThread monitorThread;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Apollo 11 Launch");
        frame.setSize(300, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        countdownLabel = new JLabel("Enter seconds:");
        countdownLabel.setBounds(50, 30, 200, 30);
        frame.add(countdownLabel);

        secondsField = new JTextField();
        secondsField.setBounds(50, 60, 200, 30);
        frame.add(secondsField);

        progressBar = new JProgressBar();
        progressBar.setBounds(50, 100, 200, 30);
        frame.add(progressBar);

        startButton = new JButton("Start");
        startButton.setBounds(50, 140, 80, 30);
        frame.add(startButton);

        cancelButton = new JButton("Cancel");
        cancelButton.setBounds(140, 140, 80, 30);
        frame.add(cancelButton);

        frame.setVisible(true);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int seconds = Integer.parseInt(secondsField.getText());
                countdownThread = new CountdownThread(seconds);
                countdownThread.start();
                monitorThread = new MonitorThread(countdownThread, "log.txt");
                monitorThread.start();

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