package schedulers;
import java.util.List;
import java.util.PriorityQueue;
import models.Process;
public class PriorityScheduler extends Scheduler {

    public PriorityScheduler(List<Process> processes) {
        super(processes);
    }

    @Override
    public void schedule() {
        PriorityQueue<Process> processQueue = new PriorityQueue<>(
                (p1, p2) -> {
                    if (p1.getPriority() != p2.getPriority()) {
                        return Integer.compare(p1.getPriority(), p2.getPriority());
                    }
                    return Integer.compare(p1.getArrivalTime(), p2.getArrivalTime());
                }
        );

        processQueue.addAll(processes);

        int currentTime = 0;
        System.out.println("\nExecution Order:");

        // iterate over p in priority order
        while (!processQueue.isEmpty()) {
            Process current = processQueue.poll(); // get p with  highest priority
            // if p arrives after current time, we need to update current time to its arrival time
            if (current.getArrivalTime() > currentTime) {
                currentTime = current.getArrivalTime(); // p can't start before its arrival
            }
            // calculate waiting time: how long p waited before starting execution
            current.setWaitingTime(currentTime - current.getArrivalTime());
            // update current time by adding burst time of the current p
            currentTime += current.getBurstTime();
            // calculate turnaround time: total time from arrival to completion
            current.setTurnaroundTime(current.getWaitingTime() + current.getBurstTime());
            // show process id in order it is executed
            System.out.println(current.getId());
        }
        // after scheduling, calculate and display the average waiting time and turnaround time
        CalcAvg(processes);
    }

//    @Override
//    public void GUIschedule() {
//        System.out.println("GUI scheduling not implemented yet.");
//    }
}

