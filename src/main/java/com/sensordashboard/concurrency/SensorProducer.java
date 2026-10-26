package com.sensordashboard.concurrency;
import com.sensordashboard.model.SensorReading;
import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;
public class SensorProducer implements Runnable {
    private final SensorBuffer buffer;
    public SensorProducer(SensorBuffer buffer) { this.buffer = buffer; }
    @Override public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                double value = 20.0 + ThreadLocalRandom.current().nextDouble(15.0);
                buffer.put(new SensorReading(Instant.now(), "Temperature", value));
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}
