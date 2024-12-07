package schedulers;

import models.Process;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.io.*;

public class SRTFScheduler extends Scheduler {
    private final int contextSwitching;
    private static final int AGE_THRESHOLD = 10; // Threshold to age processes
    private List<JSONObject> timeline; // To store the timeline of process executions

    public SRTFScheduler(List<Process> processes, int contextSwitching) {
        super(processes);
        this.contextSwitching = contextSwitching;
        this.timeline = new ArrayList<>();
    }

    public void schedule() {
        int currentTime = 0;
        Queue<Process> readyQueue = new LinkedBlockingQueue<>();
        List<Process> completedProcesses = new ArrayList<>();

        // Sort processes by arrival time
        processes.sort(Comparator.comparingInt(Process::getArrivalTime));

        Process lastExecutedProcess = null;

        while (completedProcesses.size() < processes.size()) {
            // Add new processes to the ready queue
            for (Process process : processes) {
                if (process.getArrivalTime() <= currentTime && !readyQueue.contains(process) && !completedProcesses.contains(process)) {
                    readyQueue.add(process);
                }
            }

            Process currentProcess = getShortestRemainingTimeProcess(readyQueue);

            // Handle process execution
            if (currentProcess != null) {
                readyQueue.remove(currentProcess);

                // Simulate context switching
                if (lastExecutedProcess != null && !lastExecutedProcess.equals(currentProcess)) {
                    currentTime += contextSwitching;
                }

                // Execute process for 1 time unit or until completion
                int burstTime = currentProcess.getRemainingTime();
                int executionTime = Math.min(1, burstTime); // 1 time unit execution
                currentTime += executionTime;
                currentProcess.setRemainingTime(burstTime - executionTime);

                JSONObject processTimeline = new JSONObject()
                        .put("process", currentProcess.getId())
                        .put("start_time", currentTime - executionTime)
                        .put("duration", executionTime)
                        .put("color", currentProcess.getColor());

                timeline.add(processTimeline);

                // Handle aging to prevent starvation
                for (Process process : readyQueue) {
                    if (process != currentProcess) {
                        process.incrementAge();
                        if (process.getAge() >= AGE_THRESHOLD) {
                            process.decreasePriority(); // Increase priority after aging
                        }
                    }
                }

                if (burstTime <= 0) { // Process completion
                    completedProcesses.add(currentProcess);
                    currentProcess.setTurnaroundTime(currentTime - currentProcess.getArrivalTime());
                    currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBurstTime());
                }

                lastExecutedProcess = currentProcess;
            } else {
                currentTime++;
            }
        }

        printResults(completedProcesses);
        double[] averages = CalcAvg(completedProcesses);
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

            BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line;

            while ((line = input.readLine()) != null) {
                System.out.println(line);
            }

            pr.waitFor();

            BufferedReader error = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
            while ((line = error.readLine()) != null) {
                System.err.println(line);
            }

            System.out.println("Python script executed successfully.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Process getShortestRemainingTimeProcess(Queue<Process> readyQueue) {
        Process shortestRemainingProcess = null;
        int shortestTime = Integer.MAX_VALUE;

        // Sort by priority first and then by remaining time
        List<Process> sortedReadyQueue = new ArrayList<>(readyQueue);
        sortedReadyQueue.sort((p1, p2) -> {
            if (p1.getRemainingTime() != p2.getRemainingTime()) {
                return Integer.compare(p1.getRemainingTime(), p2.getRemainingTime());
            }
            return Integer.compare(p1.getPriority(), p2.getPriority());

        });

        // Pick the first process from the sorted list (priority first, then remaining time)
        if (!sortedReadyQueue.isEmpty()) {
            shortestRemainingProcess = sortedReadyQueue.get(0);
        }
        return shortestRemainingProcess;
    }

    private void printResults(List<Process> completedProcesses) {
        System.out.println("\nSRTF Scheduling Results:");
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;
        for (Process process : completedProcesses) {
            System.out.printf("Process %s - Waiting Time: %d, Turnaround Time: %d\n",
                    process.getId(),
                    process.getWaitingTime(),
                    process.getTurnaroundTime());
            totalWaitingTime += process.getWaitingTime();
            totalTurnaroundTime += process.getTurnaroundTime();
        }

        System.out.printf("\nAverage Waiting Time: %.2f\n", (double) totalWaitingTime / completedProcesses.size());
        System.out.printf("Average Turnaround Time: %.2f\n", (double) totalTurnaroundTime / completedProcesses.size());
    }
}
