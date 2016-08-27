package me.mcmainiac.ArenaManager.api.utils;

public class GameCountdown implements Runnable {
    private int start = 10;
    private boolean running = false;
    private long sleeptime = 1000;
    private TickAction tickAction = new TickAction() { public void run() {} };

    public void setStart(int start) {
        this.start = start;
    }

    public void setTickAction(TickAction tickAction) {
        this.tickAction = tickAction;
    }

    public void setSleepTime(long sleeptime) {
        this.sleeptime = sleeptime;
    }

    public void run() {
        try {
            running = true;

            for (int i = this.start; i >= 0; i--) {
                if (!this.running) break;

                this.tickAction.setCurrent(i);

                long time = System.currentTimeMillis();
                this.tickAction.run();
                long runtime = System.currentTimeMillis() - time;

                if (sleeptime - runtime > 0)
                    Thread.sleep(sleeptime - runtime);
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
