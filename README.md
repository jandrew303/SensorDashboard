REAL-TIME SENSOR DASHBOARD
Student: Andrew Obeth J | Register Number: 711525BEC005
Course: Java Programming (B25CSI302)

This project uses ordinary Java source files and a manually configured JavaFX SDK.

REQUIREMENTS
1. JDK 21
2. JavaFX SDK compatible with Java 21 (download the SDK archive from https://openjfx.io/)
3. IntelliJ IDEA

INTELLIJ SETUP
1. Extract this ZIP.
2. IntelliJ: File > New > Project, choose Java, select JDK 21, and create a project.
3. Copy the included 'src' folder into the new project (merge with its existing src folder if asked).
4. In IntelliJ, open File > Project Structure > Libraries, click + > Java, and select the
   'lib' folder inside the extracted JavaFX SDK directory. Apply the change.
5. Open Run > Edit Configurations, choose the DashboardApp application configuration.
   If none exists, create an Application configuration:
   Main class: com.sensordashboard.app.DashboardApp
   Use classpath of module: your project/module
6. In VM options, enter the following, replacing the example path with your actual JavaFX SDK lib path:
   --module-path "C:\javafx-sdk-21\lib" --add-modules javafx.controls
   Example only: if you extracted JavaFX to C:\javafx-sdk-21, the path above should match that folder.
7. Set the working directory to your project folder, then click Apply > Run.

WHAT IT DOES
- Simulates a temperature sensor reading every second.
- Uses a bounded synchronized producer-consumer buffer.
- Displays readings in a JavaFX LineChart.
- Writes timestamped readings to sensor-log.csv in the working directory.
- Start and Stop buttons control monitoring.

If JavaFX is not found, check that the VM options path points to the JavaFX SDK's actual 'lib' folder.
This is a simulated sensor; no physical sensor is required.
