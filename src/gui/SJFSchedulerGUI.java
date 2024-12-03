/*package gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

import models.Process; // Ensure this class exists and is correctly imported
import schedulers.SJFScheduler; // Ensure this class exists and is correctly imported

public class SJFSchedulerGUI extends Application {

    private List<Process> processes = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {
        // Create UI elements
        Label instructionsLabel = new Label("Enter Process Details (ID, Burst Time, Arrival Time, Priority):");
        TextField processDetailsField = new TextField();
        Button addButton = new Button("Add Process");
        Button runButton = new Button("Run Scheduler");
        TextArea resultArea = new TextArea(); // Use JavaFX TextArea
        resultArea.setEditable(false);

        // Layout for input and controls
        VBox inputBox = new VBox(10, instructionsLabel, processDetailsField, addButton, runButton);
        inputBox.setPrefWidth(400);

        // Layout for result display
        VBox resultBox = new VBox(10, new Label("Execution Results:"), resultArea);
        resultBox.setPrefWidth(400);

        // Main layout
        HBox rootLayout = new HBox(20, inputBox, resultBox);
        rootLayout.setPadding(new Insets(20));

        // Set up the event handlers
        addButton.setOnAction(e -> {
            String[] details = processDetailsField.getText().split(" ");
            if (details.length == 4) {
                String id = details[0];
                int burstTime = Integer.parseInt(details[1]);
                int arrivalTime = Integer.parseInt(details[2]);
                int priority = Integer.parseInt(details[3]);
                processes.add(new Process(id, burstTime, arrivalTime, priority));
                processDetailsField.clear();
                resultArea.appendText("Process added: " + id + "\n");
            } else {
                resultArea.appendText("Invalid input. Please enter: ID BurstTime ArrivalTime Priority\n");
            }
        });

        runButton.setOnAction(e -> {
            if (processes.isEmpty()) {
                resultArea.appendText("No processes to schedule.\n");
                return;
            }

            // Create an instance of SJFScheduler and pass the resultArea
            SJFScheduler scheduler = new SJFScheduler(processes);
            scheduler.GUIschedule(resultArea); // This should now work without error
        });

        // Set up the stage and scene
        Scene scene = new Scene(rootLayout, 800, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("SJF Scheduler GUI");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
*/