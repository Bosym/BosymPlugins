package me.mcmainiac.ArenaManager.api.utils;

public class GameCountdown implements Runnable {
    private int start = 10;
    private boolean running = false;
    private TickAction tickAction = new TickAction() { public void run() {} };

    public void setStart(int start) {
        this.start = start;
    }

    public void setTickAction(TickAction tickAction) {
        this.tickAction = tickAction;
    }

    public void run() {
        try {
            running = true;
            Thread t = new Thread(this.tickAction);
            for (int i = this.start; i > 0; i--) {
                if (!this.running) break;

                this.tickAction.setCurrent(i);
                t.start();

                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            this.running = false;
        }
    }

    public void stop() {
        this.running = false;
    }

    public abstract static class TickAction implements Runnable {
        private int current = 10;

        public void setCurrent(int current) {
            this.current = current;
        }

        public int getCurrent() {
            return this.current;
        }
    }
}
