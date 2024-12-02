import java.util.List;

abstract  class Scheduler {
    protected List<Process> processes;

    public Scheduler(List<Process> processes) {
        this.processes = processes;
    }

    public void CalcAvg(List<Process> processes) {
        double total_wait = 0, total_turnaround = 0;
        for (Process p : processes) {
            total_wait += p.getWaitingTime();
            total_turnaround += p.getTurnaroundTime();
        }
        System.out.printf("Average WT : %.2f%n", total_wait / processes.size());
        System.out.printf("Average TAT : %.2f%n", total_turnaround / processes.size());
    }

    public abstract void schedule();

    public abstract void GUIschedule();
}