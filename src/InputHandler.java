import models.Process;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InputHandler {
    public static List<Process> getProcesses(int algorithmChoice) {
        Scanner scanner = new Scanner(System.in);
        List<Process> processes = new ArrayList<>();


        System.out.print("Enter the number of processes: ");
        int numProcesses = scanner.nextInt();
        scanner.nextLine();

        StringBuilder prompt = new StringBuilder("Enter Process ID, Burst Time, Arrival Time, Priority");

        if (algorithmChoice == 4) {
            prompt.append(", Quantum");
        }
        prompt.append(", Color");

        prompt.append(" (separated by spaces):");

        System.out.println(prompt.toString());

        for (int i = 0; i < numProcesses; i++) {
            String input = scanner.nextLine();
            String[] parts = input.split(" ");

            String processID = parts[0];
            int burstTime = Integer.parseInt(parts[1]);
            int arrivalTime = Integer.parseInt(parts[2]);

            int priority =  Integer.parseInt(parts[3]);

            //  optional fields
            int quantum = (algorithmChoice == 4 && parts.length > 4)
                    ? Integer.parseInt(parts[4])
                    : 0;

            String color = (parts.length > 4 && algorithmChoice == 4) ? parts[parts.length - 1] : parts[4];

            processes.add(new Process(processID, burstTime, arrivalTime, priority, quantum, color));
        }

        return processes;
    }
    public static int getContextSwitching() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the context switching time: ");
        return scanner.nextInt();
    }
}
