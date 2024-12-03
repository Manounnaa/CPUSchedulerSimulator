package schedulers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
/*
import javafx.scene.control.TextArea; // Correct import

import javafx.scene.control.TextArea;*/
import models.Process;

public class SJFScheduler extends Scheduler {
    private static final int PRIORITY_INCREMENT_INTERVAL = 5; // Time interval to increment priority

    public SJFScheduler(List<Process> processes) {
        super(processes);  // Pass processes to the parent constructor
    }
    @Override
    public void schedule() {
        List<Process> completedProcesses = new ArrayList<>();
        int currentTime = 0;
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;

        System.out.println("Processes execution order:");

        while (!processes.isEmpty()) {
            List<Process> availableProcesses = new ArrayList<>();
            for (Process p : processes) {
                if (p.getArrivalTime() <= currentTime) {
                    availableProcesses.add(p);
                }
            }
            for (Process p : availableProcesses) {
                int waitingTime = currentTime - p.getArrivalTime();
                if (waitingTime > PRIORITY_INCREMENT_INTERVAL) {
                    p.incrementPriority();
                }
            }

            availableProcesses.sort(
                    Comparator.comparingInt(Process::getPriority).reversed() // piriority first
                            .thenComparingInt(Process::getBurstTime) // excution time
            );
            if (!availableProcesses.isEmpty()) {
                Process currentProcess = availableProcesses.get(0);
                processes.remove(currentProcess);

                System.out.println("-> " + currentProcess.getId());

                currentProcess.setWaitingTime(currentTime - currentProcess.getArrivalTime());
                currentTime += currentProcess.getBurstTime();
                currentProcess.setTurnaroundTime(currentTime - currentProcess.getArrivalTime());

                completedProcesses.add(currentProcess);

            } else {
                currentTime++;
            }
        }

        // Print process details
        System.out.println("\nProcess Details:");
        for (Process p : completedProcesses) {
            System.out.println(p.getId() + ": Waiting Time = " + p.getWaitingTime() +
                    ", Turnaround Time = " + p.getTurnaroundTime());
        }
        CalcAvg( completedProcesses);



    }



//    @Override
//    public void GUIschedule(TextArea resultArea) {
//        List<Process> completedProcesses = new ArrayList<>();
//        int currentTime = 0;
//
//        resultArea.appendText("Processes execution order:\n");
//
//        while (!processes.isEmpty()) {
//            List<Process> availableProcesses = new ArrayList<>();
//            for (Process p : processes) {
//                if (p.getArrivalTime() <= currentTime) {
//                    availableProcesses.add(p);
//                }
//            }
//            for (Process p : availableProcesses) {
//                int waitingTime = currentTime - p.getArrivalTime();
//                if (waitingTime > PRIORITY_INCREMENT_INTERVAL) {
//                    p.incrementPriority();
//                }
//            }
//
//            availableProcesses.sort(
//                    Comparator.comparingInt(Process::getPriority).reversed()
//                            .thenComparingInt(Process::getBurstTime)
//            );
//
//            if (!availableProcesses.isEmpty()) {
//                Process currentProcess = availableProcesses.get(0);
//                processes.remove(currentProcess);
//
//                resultArea.appendText("-> " + currentProcess.getId() + "\n");
//
//                currentProcess.setWaitingTime(currentTime - currentProcess.getArrivalTime());
//                currentTime += currentProcess.getBurstTime();
//                currentProcess.setTurnaroundTime(currentTime - currentProcess.getArrivalTime());
//
//                completedProcesses.add(currentProcess);
//            } else {
//                currentTime++;
//            }
//        }
//
//        // Output process details
//        resultArea.appendText("\nProcess Details:\n");
//        for (Process p : completedProcesses) {
//            resultArea.appendText(p.getId() + ": Waiting Time = " + p.getWaitingTime() +
//                    ", Turnaround Time = " + p.getTurnaroundTime() + "\n");
//        }
//
//        // Output average times
//        CalcAvg(completedProcesses, resultArea);
//    }
//
//    private void CalcAvg(List<Process> completedProcesses, TextArea resultArea) {
//        int totalWaitingTime = 0;
//        int totalTurnaroundTime = 0;
//
//        for (Process p : completedProcesses) {
//            totalWaitingTime += p.getWaitingTime();
//            totalTurnaroundTime += p.getTurnaroundTime();
//        }
//
//        double avgWaitingTime = (double) totalWaitingTime / completedProcesses.size();
//        double avgTurnaroundTime = (double) totalTurnaroundTime / completedProcesses.size();
//
//        resultArea.appendText("\nAverage Waiting Time: " + avgWaitingTime + "\n");
//        resultArea.appendText("Average Turnaround Time: " + avgTurnaroundTime + "\n");
//    }
//

}
