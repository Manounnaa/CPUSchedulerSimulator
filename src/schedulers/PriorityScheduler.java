package schedulers;
import models.Process;
import java.util.List;
import java.util.PriorityQueue;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.io.*;
public class PriorityScheduler extends Scheduler {
    private int contextSwitchTime;
    public PriorityScheduler(List<Process> processes, int contextSwitchTime) {
        super(processes);
        this.contextSwitchTime = contextSwitchTime;}
    @Override
    public void schedule() {
        JSONArray timeline = new JSONArray();
        PriorityQueue<Process> processQueue = new PriorityQueue<>(
                (p1, p2) -> { //create pq based on priority first,if priority same, arrival time is used
                    if (p1.getPriority() != p2.getPriority()) {
                        return Integer.compare(p1.getPriority(), p2.getPriority());}
                    return Integer.compare(p1.getArrivalTime(), p2.getArrivalTime());});
        processQueue.addAll(processes);
        int currentTime = 0;  //timetacker
        System.out.println("\nExecution Order:");
        while (!processQueue.isEmpty()) {
            Process current = processQueue.poll();  // process with highest priority
            // if current process arrives after currentt, update currentt
            if (current.getArrivalTime() > currentTime) {
                currentTime = current.getArrivalTime();}
            int waitingTime = currentTime - current.getArrivalTime();
            current.setWaitingTime(waitingTime); // at = starttime-arrivaltime
            int completionTime = currentTime + current.getBurstTime();
            current.setTurnaroundTime(completionTime - current.getArrivalTime()); // tat = completiontime-arrivaltime
            // hold start time and executed time for timeline
            int startTime = currentTime;
            int executedTime = current.getBurstTime();
            // add process details to timeline
            timeline.put(new JSONObject().put("process", current.getId()).put("start_time", startTime).put("duration", executedTime).put("color", current.getColor()));
            // update current time
            currentTime = completionTime;
            System.out.println("Process ID: " + current.getId() + " | Completion Time: " + completionTime + " | Start Time: " +startTime );// output process and its completion time
            if (!processQueue.isEmpty()) {   // add context switch time if there are more processes in queue
                currentTime += contextSwitchTime;}}
        //-----------------------------------------------------------------------------------------------
        System.out.println("\nProcess Details:");
        for (Process p :processes) {
            System.out.println(p.getId() + ": Waiting Time = " + p.getWaitingTime() +
                    ", Turnaround Time = " + p.getTurnaroundTime());
        }
        double[] averages  = CalcAvg(processes);
        double avgWaitingTime = averages[0];
        double avgTurnaroundTime = averages[1];
//-------------------------------------------------------------------------------------------------------
        JSONObject result = new JSONObject();
        result.put("timeline", timeline);
        result.put("avg_waiting_time", avgWaitingTime);
        result.put("avg_turnaround_time", avgTurnaroundTime);
        File f = new File("result.json");
        // Write JSON to file
        if (f.exists()) {
            boolean deleted = f.delete();}
        try (FileWriter file = new FileWriter("result.json")) {
            file.write(result.toString());
            file.flush();
            System.out.println("Results saved to result.json.");
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
}