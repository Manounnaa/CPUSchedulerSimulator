package schedulers;
import models.Process;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.List;
import java.util.PriorityQueue;
public class PriorityScheduler extends Scheduler {
    private int contextSwitchTime;
    public PriorityScheduler(List<Process> processes, int contextSwitchTime) {
        super(processes);
        this.contextSwitchTime = contextSwitchTime;}
    @Override
    public void schedule() {
        JSONArray timeline = new JSONArray();
        PriorityQueue<Process> processQueue = new PriorityQueue<>(//priority queue sorted by priority"ascending)",then by arrival time
                (p1, p2) -> {
                    if (p1.getPriority() != p2.getPriority()) {//if processes have different priorities, lowest priority is processed first
                        return Integer.compare(p1.getPriority(), p2.getPriority());}
                    return Integer.compare(p1.getArrivalTime(), p2.getArrivalTime());}//if two processes same priority,they are sorted by their arrival time (earliest first)
        );
        processQueue.addAll(processes);
        int currentTime = 0;  // time tracker
        System.out.println("\nExecution Order:");
        while (!processQueue.isEmpty()) {
            Process current = processQueue.poll();  // process with highest priority
            if (current.getArrivalTime() > currentTime) {//if current process arrives after currentt,update currentt
                currentTime = current.getArrivalTime();}
            // add context switching time before executing current process
            currentTime += contextSwitchTime;
            int completionTime = currentTime + current.getBurstTime(); // calculate completion time
            current.setTurnaroundTime(completionTime - current.getArrivalTime()); // calculate turnaround time
            int waitingTime = current.getTurnaroundTime() - current.getBurstTime(); // calculate waiting time correctly
            current.setWaitingTime(waitingTime); // set waiting time
            // show start time and executed time for timeline
            int startTime = currentTime;
            int executedTime = current.getBurstTime();
            timeline.put(new JSONObject().put("process", current.getId()).put("start_time", startTime).put("duration", executedTime));
            currentTime = completionTime;// update current time to process completion time
            // output process and its completion time
            System.out.println("Process ID: " + current.getId() + " | Completion Time: " + completionTime);
            // add context switch time if there are more processes in queue
            if (!processQueue.isEmpty()) {
                currentTime += contextSwitchTime;}}
        //--------------------------------------------------------------------------------------
        double[] averages = CalcAvg(processes);
        double avgWaitingTime = averages[0];
        double avgTurnaroundTime = averages[1];
        JSONObject result = new JSONObject();
        //--------------------------------------------------------------------------------------
        result.put("timeline", timeline);
        result.put("avg_waiting_time", avgWaitingTime);
        result.put("avg_turnaround_time", avgTurnaroundTime);
        //-------------------------------------------------------------------------
        try (FileWriter file = new FileWriter("result.json")) {
            file.write(result.toString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();}
        // call Python script to generate gantt chart
        try {
            Runtime rt = Runtime.getRuntime();
            String command = "python generate_gantt_chart.py";
            java.lang.Process pr = rt.exec(command);
            BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                System.out.println(line);}
            pr.waitFor();
            BufferedReader error = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
            while ((line = error.readLine()) != null) {
                System.err.println(line);}
            System.out.println("Python script executed successfully.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();}}}
