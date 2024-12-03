package schedulers;
import models.Process;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class SRTFScheduler extends Scheduler{
    private final int contextSwitching;
    public SRTFScheduler (List<Process> processes, int contextSwitching) {
        super(processes);
        this.contextSwitching = contextSwitching;
    }
    public void schedule() {
        int currentTime = 0;
        Queue<Process> readyQueue = new LinkedBlockingQueue<>();
        List<Process> completedProcesses = new ArrayList<>();
        Map<Process, Integer> remainingTime = new HashMap<>();
        for (Process process : processes) {
            remainingTime.put(process, process.getBurstTime());
        }
        processes.sort(Comparator.comparingInt(Process::getArrivalTime));
        while (!completedProcesses.containsAll(processes)) {
            for (Process process : processes) {
                if (process.getArrivalTime() <= currentTime && !readyQueue.contains(process) && !completedProcesses.contains(process)) {
                    readyQueue.add(process);
                }
            }
            Process currentProcess = getShortestRemainingTimeProcess(readyQueue, remainingTime);
            if (currentProcess != null) {
                readyQueue.remove(currentProcess);
                int burstTime = remainingTime.get(currentProcess);
                int executionTime = Math.min(1, burstTime);
                executionTime = (int) Math.ceil(executionTime);
                currentTime += executionTime;
                burstTime -= executionTime;
                remainingTime.put(currentProcess, burstTime);

                if (burstTime <= 0) {
                    completedProcesses.add(currentProcess);
                    currentProcess.setTurnaroundTime(currentTime - currentProcess.getArrivalTime());
                    currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBurstTime());
                } else {
                    readyQueue.add(currentProcess);
                } currentTime += contextSwitching;
            } else {
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
        } return shortestRemainingProcess;
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






