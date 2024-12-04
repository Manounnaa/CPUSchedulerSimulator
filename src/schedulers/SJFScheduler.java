package schedulers;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.json.JSONObject;
import models.Process;
import org.json.JSONArray;
import java.io.File;

/*
import javafx.scene.control.TextArea; // Correct import

import javafx.scene.control.TextArea;*/
import models.Process;

public class SJFScheduler extends Scheduler {
    private static final int PRIORITY_INCREMENT_INTERVAL = 5; // Time interval to increment priority

    private List<JSONObject> timeline; // To store the timeline of process executions

    public SJFScheduler(List<Process> processes) {
        super(processes);// Pass processes to the parent constructor
        this.timeline = new ArrayList<>();
    }
    @Override
    public void schedule() {
        List<Process> completedProcesses = new ArrayList<>();
        int currentTime = 0;
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;

        System.out.println("Processes execution order:");

        while (!processes.isEmpty()) {
            List<Process> availableProcesses = new ArrayList<>();
            for (Process p : processes) {
                if (p.getArrivalTime() <= currentTime) {
                    availableProcesses.add(p);
                }
            }
            for (Process p : availableProcesses) {
                int waitingTime = currentTime - p.getArrivalTime();
                if (waitingTime > PRIORITY_INCREMENT_INTERVAL) {
                    p.incrementPriority();
                }
            }

            availableProcesses.sort(
                    Comparator.comparingInt(Process::getPriority).reversed() // piriority first
                            .thenComparingInt(Process::getBurstTime) // excution time
            );
            if (!availableProcesses.isEmpty()) {
                Process currentProcess = availableProcesses.get(0);
                processes.remove(currentProcess);

                System.out.println("-> " + currentProcess.getId());

                currentProcess.setWaitingTime(currentTime - currentProcess.getArrivalTime());
                currentTime += currentProcess.getBurstTime();
                currentProcess.setTurnaroundTime(currentTime - currentProcess.getArrivalTime());

                completedProcesses.add(currentProcess);


                int startTime = currentTime - currentProcess.getBurstTime();
                int duration = currentProcess.getBurstTime();
                JSONObject processTimeline = new JSONObject()
                        .put("process", currentProcess.getId())
                        .put("start_time", startTime)
                        .put("duration", duration);
                timeline.add(processTimeline);

            } else {
                currentTime++;
            }
        }

        // Print process details
        System.out.println("\nProcess Details:");
        for (Process p : completedProcesses) {
            System.out.println(p.getId() + ": Waiting Time = " + p.getWaitingTime() +
                    ", Turnaround Time = " + p.getTurnaroundTime());
        }
        double[] averages  = CalcAvg(completedProcesses);
        double avgWaitingTime = averages[0];
        double avgTurnaroundTime = averages[1];
        // Create a JSON object to hold the timeline and other results
        JSONObject result = new JSONObject();
        result.put("timeline", new JSONArray(timeline));  // Add timeline as a JSON array
        result.put("avg_waiting_time", avgWaitingTime);
        result.put("avg_turnaround_time", avgTurnaroundTime);
        File f = new File("result.json");

        // Check if the file exists
        if (f.exists()) {
            boolean deleted = f.delete();
            // Write JSON to file
        }
        try (FileWriter file = new FileWriter("result.json")) {
            file.write(result.toString());
            file.flush();
            System.out.println("Results saved to result.json.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            Runtime rt = Runtime.getRuntime();
            String command = "python generate_gantt_chart.py";
            java.lang.Process pr = rt.exec(command);

            // Get the output stream of the process
            BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line;

            // Print the output of the Python script
            while ((line = input.readLine()) != null) {
                System.out.println(line);
            }

            // Wait for the script to finish execution
            pr.waitFor();

            // Get the error stream of the process (if any)
            BufferedReader error = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
            while ((line = error.readLine()) != null) {
                System.err.println(line);
            }

            System.out.println("Python script executed successfully.");

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }


    }



//    @Override
//    public void GUIschedule(TextArea resultArea) {
//        List<Process> completedProcesses = new ArrayList<>();
//        int currentTime = 0;
//
//        resultArea.appendText("Processes execution order:\n");
//
//        while (!processes.isEmpty()) {
//            List<Process> availableProcesses = new ArrayList<>();
//            for (Process p : processes) {
//                if (p.getArrivalTime() <= currentTime) {
//                    availableProcesses.add(p);
//                }
//            }
//            for (Process p : availableProcesses) {
//                int waitingTime = currentTime - p.getArrivalTime();
//                if (waitingTime > PRIORITY_INCREMENT_INTERVAL) {
//                    p.incrementPriority();
//                }
//            }
//
//            availableProcesses.sort(
//                    Comparator.comparingInt(Process::getPriority).reversed()
//                            .thenComparingInt(Process::getBurstTime)
//            );
//
//            if (!availableProcesses.isEmpty()) {
//                Process currentProcess = availableProcesses.get(0);
//                processes.remove(currentProcess);
//
//                resultArea.appendText("-> " + currentProcess.getId() + "\n");
//
//                currentProcess.setWaitingTime(currentTime - currentProcess.getArrivalTime());
//                currentTime += currentProcess.getBurstTime();
//                currentProcess.setTurnaroundTime(currentTime - currentProcess.getArrivalTime());
//
//                completedProcesses.add(currentProcess);
//            } else {
//                currentTime++;
//            }
//        }
//
//        // Output process details
//        resultArea.appendText("\nProcess Details:\n");
//        for (Process p : completedProcesses) {
//            resultArea.appendText(p.getId() + ": Waiting Time = " + p.getWaitingTime() +
//                    ", Turnaround Time = " + p.getTurnaroundTime() + "\n");
//        }
//
//        // Output average times
//        CalcAvg(completedProcesses, resultArea);
//    }
//
//    private void CalcAvg(List<Process> completedProcesses, TextArea resultArea) {
//        int totalWaitingTime = 0;
//        int totalTurnaroundTime = 0;
//
//        for (Process p : completedProcesses) {
//            totalWaitingTime += p.getWaitingTime();
//            totalTurnaroundTime += p.getTurnaroundTime();
//        }
//
//        double avgWaitingTime = (double) totalWaitingTime / completedProcesses.size();
//        double avgTurnaroundTime = (double) totalTurnaroundTime / completedProcesses.size();
//
//        resultArea.appendText("\nAverage Waiting Time: " + avgWaitingTime + "\n");
//        resultArea.appendText("Average Turnaround Time: " + avgTurnaroundTime + "\n");
//    }
//

}
