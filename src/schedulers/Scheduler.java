package schedulers;
/*
import java.awt.*;*/
import java.util.List;

import models.Process;

public abstract  class Scheduler {
    protected List<Process> processes;

    public Scheduler(List<Process> processes) {
        this.processes = processes;
    }

    public double[] CalcAvg(List<Process> processes) {
        double total_wait = 0, total_turnaround = 0;

        for (Process p : processes) {
            total_wait += p.getWaitingTime();
            total_turnaround += p.getTurnaroundTime();
        }

        double avgWaitingTime = total_wait / processes.size();
        double avgTurnaroundTime = total_turnaround / processes.size();

        // Print the averages
        System.out.printf("Average WT : %.2f%n", avgWaitingTime);
        System.out.printf("Average TAT : %.2f%n", avgTurnaroundTime);

        // Return the averages as an array
        return new double[]{avgWaitingTime, avgTurnaroundTime};
    }

    public abstract void schedule();


}