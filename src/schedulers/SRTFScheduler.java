package schedulers;
import models.Process;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class SRTFScheduler extends Scheduler {
    private final int contextSwitching;
    private static final int AGE_THRESHOLD = 10; // Threshold to age processes

    public SRTFScheduler(List<Process> processes, int contextSwitching) {
        super(processes);
        this.contextSwitching = contextSwitching;
    }

    public void schedule() {
        int currentTime = 0;
        Queue<Process> readyQueue = new LinkedBlockingQueue<>();
        List<Process> completedProcesses = new ArrayList<>();
        Map<Process, Integer> remainingTime = new HashMap<>();

        // Initialize remaining time for each process
        for (Process process : processes) {
            remainingTime.put(process, process.getBurstTime());
        }

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

            Process currentProcess = getShortestRemainingTimeProcess(readyQueue, remainingTime);

            // Handle process execution
            if (currentProcess != null) {
                readyQueue.remove(currentProcess);

                // Simulate context switching
                if (lastExecutedProcess != null && !lastExecutedProcess.equals(currentProcess)) {
                    currentTime += contextSwitching;
                }

                // Execute process for 1 time unit or until completion
                int burstTime = remainingTime.get(currentProcess);
                int executionTime = Math.min(1, burstTime); // 1 time unit execution
                executionTime = (int) Math.ceil(executionTime);
                currentTime += executionTime;
                burstTime -= executionTime;
                remainingTime.put(currentProcess, burstTime);

                // Handle aging to prevent starvation
                for (Process process : readyQueue) {
                    if (process != currentProcess) {
                        process.incrementAge();
                        if (process.getAge() >= AGE_THRESHOLD) {
                            process.increasePriority(); // Increase priority after aging
                        }
                    }
                }

                if (burstTime <= 0) { // Process completion
                    completedProcesses.add(currentProcess);
                    currentProcess.setTurnaroundTime(currentTime - currentProcess.getArrivalTime());
                    currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBurstTime());
                } else {
                    readyQueue.add(currentProcess); // Re-add process if not completed
                }

                lastExecutedProcess = currentProcess;
            } else {
                // Increment time if no process is ready
                currentTime++;
            }
        }

        printResults(completedProcesses);
    }

    private Process getShortestRemainingTimeProcess(Queue<Process> readyQueue, Map<Process, Integer> remainingTime) {
        Process shortestRemainingProcess = null;
        int shortestTime = Integer.MAX_VALUE;
        for (Process process : readyQueue) {
            int remaining = remainingTime.get(process);
            if (remaining < shortestTime) {
                shortestTime = remaining;
                shortestRemainingProcess = process;
            }
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








