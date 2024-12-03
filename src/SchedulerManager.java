import java.util.List;

import schedulers.*;

import models.Process;

public class SchedulerManager {

    public static void runScheduler(int choice, List<Process> processes, int contextSwitching) {
        Scheduler scheduler = createScheduler(choice, processes, contextSwitching);
        if (scheduler == null) {
            System.out.println("Invalid scheduler choice.");
            return;
        }

        scheduler.schedule();
    }

    private static Scheduler createScheduler(int choice, List<Process> processes, int contextSwitching) {
        switch (choice) {
            case 1:
                return new SJFScheduler(processes);
            case 2:
                return new SRTFScheduler(processes, contextSwitching);
            case 3:
              return new PriorityScheduler(processes);
            case 4:
                return new FCAI_Scheduling(processes);
            default:
                return null; // Invalid choice
        }
    }
}
