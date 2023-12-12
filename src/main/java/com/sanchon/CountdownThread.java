package com.sanchon;

public class CountdownThread extends Thread {
    private final int seconds;
    private String status;
    private int secondsLeft;

    public CountdownThread(int seconds) {
        this.seconds = seconds;
        this.status = "active";

    }

    @Override
    public void run() {
        for (int i = seconds; i >= 0; i--) {
            if (Thread.interrupted()) {
                this.status = "cancelled";
                return;
            }
            Main.countdownLabel.setText(this.getName() + "  Segundos restantes: " + i);
            Main.progressBar.setValue((int) ((double) i / seconds * 100));
            secondsLeft = i;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                this.status = "cancelled";
                try {
                    Thread.currentThread().join();
                } catch (InterruptedException e2) {
                    throw new RuntimeException(e2);
                }
                return;
            }
        }
        this.status = "completed";
    }

    public String getStatus() {
        return status;
    }


    public int getSecondsLeft() {
        return secondsLeft;
    }

}