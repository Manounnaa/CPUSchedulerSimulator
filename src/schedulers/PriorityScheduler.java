package schedulers;

import models.Process;

import java.util.List;
import java.util.PriorityQueue;

public class PriorityScheduler extends Scheduler {
    private int contextSwitchTime;

    public PriorityScheduler(List<Process> processes, int contextSwitchTime) {
        super(processes);
        this.contextSwitchTime = contextSwitchTime;
    }

    @Override
    public void schedule() {
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
        CalcAvg(processes);
    }
}
