package com.sensordashboard.logging;
import com.sensordashboard.model.SensorReading;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
public class FileLogger implements AutoCloseable {
    private final BufferedWriter writer;
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());
    public FileLogger(Path path) throws IOException {
        Path parent = path.toAbsolutePath().getParent();
        if (parent != null) Files.createDirectories(parent);
        boolean newFile = !Files.exists(path) || Files.size(path) == 0;
        writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND);
        if (newFile) { writer.write("timestamp,sensor,value"); writer.newLine(); writer.flush(); }
    }
    public synchronized void write(SensorReading r) throws IOException {
        writer.write(FORMAT.format(r.getTimestamp()) + "," + r.getSensorName() + "," + String.format(Locale.US, "%.2f", r.getValue()));
        writer.newLine(); writer.flush();
    }
    @Override public synchronized void close() throws IOException { writer.close(); }
}
