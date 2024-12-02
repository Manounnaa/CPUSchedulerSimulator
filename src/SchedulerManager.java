import java.util.List;

public class SchedulerManager {
    public static void runScheduler(int choice, List<Process> processes) {
        // ADD ur class then uncomment here plz
        switch (choice) {
            case 1:
                System.out.println("Running Shortest Job First (SJF)...");
                //SJFScheduler sjfScheduler = new SJFScheduler();
               // sjfScheduler.schedule(processes);
                break;
            case 2:
                System.out.println("Running Shortest Remaining Time First (SRTF)...");
//                SRTFScheduler srtfScheduler = new SRTFScheduler();
//                srtfScheduler.schedule(processes);
                break;
            case 3:
                System.out.println("Running Priority Scheduling...");
//                PriorityScheduler priorityScheduler = new PriorityScheduler();
//                priorityScheduler.schedule(processes);
                break;
            case 4:
                System.out.println("Running FCAI Scheduling...");
//                FCAIScheduler fcaiScheduler = new FCAIScheduler();
//                fcaiScheduler.schedule(processes);
                break;
            default:
                System.out.println("Invalid choice!");
        }
    }
}
