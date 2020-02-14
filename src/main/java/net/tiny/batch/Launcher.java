package net.tiny.batch;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Launcher implements Runnable, AutoCloseable {

    static final Logger logger = Logger.getLogger(Launcher.class.getName());

    private boolean started = false;

    @Override
    public void run() {
        started = true;
        logger.finest("[FINEST] Message");
        logger.finer("[FINER] Message");
        logger.fine("[FINE] Message");
        logger.config("[CONFIG] Message");
        logger.info("[INFO] Message");
        logger.log(Level.INFO, "[INFO] {0} is {1} in English", new Object[]{"Hoge", "Fuga"});

        logger.warning("[WARN] Message");
        logger.severe("[ERROR] Message");
        Exception ex = new Exception("Launcher test error message");
        logger.log(Level.SEVERE, "[ERROR] " + ex.getMessage(), ex);

        org.tiny.app.Dummy dummy = new org.tiny.app.Dummy();
        dummy.call();

        started = false;

    }

    public boolean isStarting() {
        return started;
    }

    public void cancel() {
        if (isStarting()) {
        }
    }

    @Override
    public void close() throws Exception {
        cancel();
    }


}
