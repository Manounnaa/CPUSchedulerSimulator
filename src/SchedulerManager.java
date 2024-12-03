import java.util.List;

//import schedulers.FCAIScheduler;
//import schedulers.PriorityScheduler;
import schedulers.FCAI_Scheduling;
import schedulers.SJFScheduler;
//import schedulers.SRTFScheduler;
import schedulers.Scheduler;
import models.Process;

public class SchedulerManager {

    public static void runScheduler(int choice, List<Process> processes) {
        Scheduler scheduler = createScheduler(choice, processes);
        if (scheduler == null) {
            System.out.println("Invalid scheduler choice.");
            return;
        }

        scheduler.schedule();
    }

    private static Scheduler createScheduler(int choice, List<Process> processes) {
        switch (choice) {
            case 1:
                return new SJFScheduler(processes);
            case 2:
              //  return new SRTFScheduler(processes);
            case 3:
           //     return new PriorityScheduler(processes);
            case 4:
                return new FCAI_Scheduling(processes);
            default:
                return null; // Invalid choice
        }
    }
}
