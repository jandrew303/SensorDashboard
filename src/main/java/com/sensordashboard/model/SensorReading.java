package com.sensordashboard.model;
import java.time.Instant;
public final class SensorReading {
    private final Instant timestamp;
    private final String sensorName;
    private final double value;
    public SensorReading(Instant timestamp, String sensorName, double value) {
        this.timestamp = timestamp; this.sensorName = sensorName; this.value = value;
    }
    public Instant getTimestamp() { return timestamp; }
    public String getSensorName() { return sensorName; }
    public double getValue() { return value; }
}
