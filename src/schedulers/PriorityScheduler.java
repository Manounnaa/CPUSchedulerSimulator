package schedulers;

import models.Process;

import java.util.List;
import java.util.PriorityQueue;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.io.*;

public class PriorityScheduler extends Scheduler {
    private int contextSwitchTime;

    public PriorityScheduler(List<Process> processes, int contextSwitchTime) {
        super(processes);
        this.contextSwitchTime = contextSwitchTime;
    }

    @Override
    public void schedule() {
        JSONArray timeline = new JSONArray();
        // Create a priority queue based on priority first, and if the priority is the same, arrival time is used
        PriorityQueue<Process> processQueue = new PriorityQueue<>(
                (p1, p2) -> {
                    if (p1.getPriority() != p2.getPriority()) {
                        return Integer.compare(p1.getPriority(), p2.getPriority());
                    }
                    return Integer.compare(p1.getArrivalTime(), p2.getArrivalTime());
                }
        );

        processQueue.addAll(processes);

        int currentTime = 0;  // The time tracker
        System.out.println("\nExecution Order:");

        // Process the processes in priority order
        while (!processQueue.isEmpty()) {
            Process current = processQueue.poll();  // Process with highest priority

            // If the current process arrives after the current time, update current time
            if (current.getArrivalTime() > currentTime) {
                currentTime = current.getArrivalTime();
            }

            // Calculate Waiting Time
            int waitingTime = currentTime - current.getArrivalTime();
            current.setWaitingTime(waitingTime); // Waiting Time = Start Time - Arrival Time

            // Calculate Completion Time
            int completionTime = currentTime + current.getBurstTime();
            current.setTurnaroundTime(completionTime - current.getArrivalTime()); // Turnaround Time = Completion Time - Arrival Time

            // Capture start time and executed time for the timeline
            int startTime = currentTime;
            int executedTime = current.getBurstTime();

            // Add the process details to the timeline
            timeline.put(new JSONObject()
                    .put("process", current.getId())
                    .put("start_time", startTime)
                    .put("duration", executedTime));

            // Update current time
            currentTime = completionTime;

            // Output the process and its completion time
            System.out.println("Process ID: " + current.getId() + " | Completion Time: " + completionTime);

            // Add context switch time if there are more processes in the queue
            if (!processQueue.isEmpty()) {
                currentTime += contextSwitchTime;
            }
        }

        // After scheduling, calculate and display the average waiting time and turnaround time
        double[] averages  = CalcAvg(processes);
        double avgWaitingTime = averages[0];
        double avgTurnaroundTime = averages[1];

        JSONObject result = new JSONObject();
        result.put("timeline", timeline);
        result.put("avg_waiting_time", avgWaitingTime);
        result.put("avg_turnaround_time", avgTurnaroundTime);
        // Write JSON to file
        try (FileWriter file = new FileWriter("result.json")) {
            file.write(result.toString());
            file.flush();
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
}
