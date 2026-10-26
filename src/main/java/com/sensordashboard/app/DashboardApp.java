package com.sensordashboard.app;
import com.sensordashboard.concurrency.*;
import com.sensordashboard.logging.FileLogger;
import com.sensordashboard.model.SensorReading;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Locale;

public class DashboardApp extends Application {
    private final XYChart.Series<Number, Number> series = new XYChart.Series<>();
    private final Label currentValue = new Label("Current temperature: -- °C");
    private final Label status = new Label("Status: stopped");
    private Thread producerThread, consumerThread;
    private boolean running = false;
    private int sampleNumber = 0;

    @Override public void start(Stage stage) {
        NumberAxis xAxis = new NumberAxis(); xAxis.setLabel("Sample number");
        NumberAxis yAxis = new NumberAxis(0, 50, 5); yAxis.setLabel("Temperature (°C)");
        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle("Live Temperature Readings"); chart.setAnimated(false); chart.setCreateSymbols(false);
        series.setName("Temperature"); chart.getData().add(series);

        Button start = new Button("Start"), stop = new Button("Stop"); stop.setDisable(true);
        start.setOnAction(e -> {
            try { startMonitoring(); start.setDisable(true); stop.setDisable(false); }
            catch (IOException ex) { status.setText("Cannot open log file: " + ex.getMessage()); }
        });
        stop.setOnAction(e -> { stopMonitoring(); start.setDisable(false); stop.setDisable(true); });

        HBox controls = new HBox(12, start, stop, currentValue, status); controls.setPadding(new Insets(12));
        BorderPane root = new BorderPane(); root.setCenter(chart); root.setBottom(controls); root.setPadding(new Insets(10));
        stage.setTitle("Real-Time Sensor Dashboard"); stage.setScene(new Scene(root, 950, 600));
        stage.setOnCloseRequest(e -> stopMonitoring()); stage.show();
    }

    private void startMonitoring() throws IOException {
        if (running) return;
        SensorBuffer buffer = new SensorBuffer(20);
        FileLogger logger = new FileLogger(Path.of("sensor-log.csv"));
        SensorConsumer consumer = new SensorConsumer(buffer, logger, this::displayReading,
                message -> status.setText("Status: " + message));
        producerThread = new Thread(new SensorProducer(buffer), "Sensor-Producer");
        consumerThread = new Thread(consumer, "Sensor-Consumer");
        running = true; status.setText("Status: running • log: " + Path.of("sensor-log.csv").toAbsolutePath());
        consumerThread.start(); producerThread.start();
    }

    private void displayReading(SensorReading reading) {
        sampleNumber++;
        series.getData().add(new XYChart.Data<>(sampleNumber, reading.getValue()));
        if (series.getData().size() > 50) series.getData().remove(0);
        currentValue.setText(String.format(Locale.US, "Current temperature: %.2f °C", reading.getValue()));
    }

    private void stopMonitoring() {
        if (!running) return;
        running = false;
        if (producerThread != null) producerThread.interrupt();
        if (consumerThread != null) consumerThread.interrupt();
        status.setText("Status: stopped");
    }

    @Override public void stop() { stopMonitoring(); }
    public static void main(String[] args) { launch(args); }
}
