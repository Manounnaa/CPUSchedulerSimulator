import models.Process;

import java.util.List;
import java.util.Scanner;

public class CPUSchedulerSimulator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please select the scheduling algorithm:");
        System.out.println("1. Shortest Job First (SJF)");
        System.out.println("2. Shortest Remaining Time First (SRTF)");
        System.out.println("3. Priority Scheduling");
        System.out.println("4. FCAI Scheduling");
        System.out.print("Enter your choice (1-4): ");

        int choice = scanner.nextInt();

        if (choice < 1 || choice > 4) {
            System.out.println("Invalid choice ");
            return;
        }

        List<Process> processes = InputHandler.getProcesses(choice);

        int contextSwitching = 0;
        if (choice == 2 || choice==3) {
            contextSwitching = InputHandler.getContextSwitching();
        }
        System.out.println("Processes:");
        for (Process p : processes) {
            System.out.println(p);
        }
        SchedulerManager.runScheduler(choice, processes, contextSwitching);

    }
}
