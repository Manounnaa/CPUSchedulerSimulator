package schedulers;
import models.Process;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Comparator;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.io.*;

public class FCAI_Scheduling extends Scheduler {
    private Queue<Process> readyQueue;
    private Queue<Process> ProcessQueue;
    private double V1, V2;
    private int currentTime;
    private List<Process> finishedQueue;

    public FCAI_Scheduling(List<Process> processes) {
        super(processes);
        this.ProcessQueue = new PriorityQueue<>(
                Comparator.comparingInt(Process::getArrivalTime)
                        .thenComparingInt(this::calculateFCAIFactor)
        );
        this.readyQueue = new LinkedList<>();
        this.V1 = processes.stream().mapToInt(Process::getArrivalTime).max().getAsInt() / 10.0;
        this.V2 = processes.stream().mapToInt(Process::getBurstTime).max().getAsInt() / 10.0;
        this.finishedQueue = new LinkedList<>();
    }

    @Override
    public void schedule() {
        currentTime = 0;
        addReadyProcesses(currentTime);

        System.out.println("Starting scheduling...\n");
        JSONArray timeline = new JSONArray();
        while (!processes.isEmpty() || !readyQueue.isEmpty()) {
            if (readyQueue.isEmpty()) {
                currentTime = processes.get(0).getArrivalTime();
                System.out.println("CPU is idle until time " + currentTime);
                addReadyProcesses(currentTime);
            }

            Process current = readyQueue.poll();
            int quantum = current.getQuantum();
            int executedTime = 0;
            Boolean preempted = false;
            System.out.println("Time " + currentTime + ": Process " + current.getId() + " starts executing (Quantum: " + quantum + ") with FCAI Factor " + calculateFCAIFactor(current));
            int startTime = currentTime;
            while (current.getRemainingTime() > 0 && executedTime < quantum) {
                currentTime++;
                current.setRemainingTime(current.getRemainingTime() - 1);
                executedTime++;

                addReadyProcesses(currentTime);
                Process nextProcess = readyQueue.peek();
                if(currentTime >= 0.4 * current.getQuantum()) {
                    if (nextProcess != null && calculateFCAIFactor(nextProcess) < calculateFCAIFactor(current)) {
                        readyQueue.offer(current);
                        System.out.println("Time " + currentTime + ": Process " + current.getId() +
                                " preempted by Process " + nextProcess.getId());
                        preempted = true;
                        break;
                    }
                }
            }
            timeline.put(new JSONObject()
                    .put("process", current.getId())
                    .put("start_time", startTime)
                    .put("duration", executedTime));
            if (current.getRemainingTime() > 0) {
                if(preempted){
                    int unusedQuantum = quantum - executedTime;
                    current.setQuantum(current.getQuantum() + unusedQuantum);
                    if (!readyQueue.contains(current)) {
                        readyQueue.offer(current);
                    }
                    //readyQueue.offer(current);
                    System.out.println("Time " + currentTime + ": Process " + current.getId() +
                            " returned to queue. Remaining time: " + current.getRemainingTime() +
                            " (New Quantum: " + current.getQuantum() + ")");
                }else{
                    int newQuantum = quantum + 2;
                    current.setQuantum(newQuantum);
                    if (!readyQueue.contains(current)) {
                        readyQueue.offer(current);
                    }
                    //         readyQueue.offer(current);
                    System.out.println("Time " + currentTime + ": Process " + current.getId() +
                            " returned to queue. Remaining time: " + current.getRemainingTime() +
                            " (New Quantum: " + newQuantum + ")");
                }

            } else {
                current.setTurnaroundTime(currentTime - current.getArrivalTime());
                current.setWaitingTime(current.getTurnaroundTime() - current.getBurstTime());
                finishedQueue.add(current);
                System.out.println("Time " + currentTime + ": Process " + current.getId() +
                        " completed. Waiting Time: " + current.getWaitingTime() +
                        ", Turnaround Time: " + current.getTurnaroundTime());
            }
        }

        double[] averages  = CalcAvg(finishedQueue);
        double avgWaitingTime = averages[0];
        double avgTurnaroundTime = averages[1];

        JSONObject result = new JSONObject();
        result.put("timeline", timeline);
        result.put("avg_waiting_time", avgWaitingTime);
        result.put("avg_turnaround_time", avgTurnaroundTime);
        // Write JSON to file
        try (FileWriter file = new FileWriter("result.json")) {
            file.write(result.toString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            Runtime rt = Runtime.getRuntime();
            String command = "python generate_gantt_chart.py";
            java.lang.Process pr = rt.exec(command);

            // Get the output stream of the process
            BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line;

            // Print the output of the Python script
            while ((line = input.readLine()) != null) {
                System.out.println(line);
            }

            // Wait for the script to finish execution
            pr.waitFor();

            // Get the error stream of the process (if any)
            BufferedReader error = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
            while ((line = error.readLine()) != null) {
                System.err.println(line);
            }

            System.out.println("Python script executed successfully.");

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void GUIschedule() {}

    private int calculateFCAIFactor(Process process) {
        // Adjust the formula to give more weight to burst time
        int priorityFactor = 10 - process.getPriority();
        int arrivalFactor = (int) Math.ceil(process.getArrivalTime() / V1);
        int burstFactor = (int) Math.ceil(process.getRemainingTime() / V2);

        // Increase the impact of burst time by multiplying it
        return priorityFactor + arrivalFactor + (burstFactor);
    }

    private void addReadyProcesses(int currentTime) {
        while (!processes.isEmpty() && processes.get(0).getArrivalTime() <= currentTime) {
            Process process = processes.remove(0);
            // Check if the process is already in the readyQueue
            if (!readyQueue.contains(process)) {
                readyQueue.offer(process);
            }
        }
    }

}
