package com.sensordashboard.concurrency;
import com.sensordashboard.logging.FileLogger;
import com.sensordashboard.model.SensorReading;
import javafx.application.Platform;
import java.io.IOException;
import java.util.function.Consumer;
public class SensorConsumer implements Runnable {
    private final SensorBuffer buffer;
    private final FileLogger logger;
    private final Consumer<SensorReading> onReading;
    private final Consumer<String> onError;
    public SensorConsumer(SensorBuffer buffer, FileLogger logger, Consumer<SensorReading> onReading, Consumer<String> onError) {
        this.buffer = buffer; this.logger = logger; this.onReading = onReading; this.onError = onError;
    }
    @Override public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                SensorReading reading = buffer.take();
                logger.write(reading);
                Platform.runLater(() -> onReading.accept(reading));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            Platform.runLater(() -> onError.accept("Logging failed: " + e.getMessage()));
        } finally {
            try { logger.close(); }
            catch (IOException e) { Platform.runLater(() -> onError.accept("Could not close log: " + e.getMessage())); }
        }
    }
}
