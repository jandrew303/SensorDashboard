package com.sensordashboard.concurrency;
import com.sensordashboard.model.SensorReading;
import java.util.ArrayDeque;
import java.util.Queue;
/** Bounded shared queue protected with synchronized, wait and notifyAll. */
public class SensorBuffer {
    private final Queue<SensorReading> queue = new ArrayDeque<>();
    private final int capacity;
    public SensorBuffer(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException("Capacity must be positive.");
        this.capacity = capacity;
    }
    public synchronized void put(SensorReading reading) throws InterruptedException {
        while (queue.size() >= capacity) wait();
        queue.add(reading);
        notifyAll();
    }
    public synchronized SensorReading take() throws InterruptedException {
        while (queue.isEmpty()) wait();
        SensorReading reading = queue.remove();
        notifyAll();
        return reading;
    }
}
