package models;

import java.util.List;

public class Process {
    private String id;          // models.Process ID
    private int burstTime;      // Burst Time
    private int arrivalTime;    // Arrival Time
    private int priority;   // Priority
    private Integer quantum;    // Quantum (optional) 4

    private int waitingTime;    // Time the process waits before execution
    private int turnaroundTime; // Total time from arrival to completion

    public Process(String id, int burstTime, int arrivalTime, int priority) {
        this(id, burstTime, arrivalTime, priority, null);
    }
    // Constructor
    public Process(String id, int burstTime, int arrivalTime, int priority, Integer quantum) {
        this.id = id;
        this.burstTime = burstTime;
        this.arrivalTime = arrivalTime;
        this.priority = priority;
        this.quantum = quantum;
        this.waitingTime = 0;
        this.turnaroundTime = 0;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Integer getQuantum() {
        return quantum;
    }

    public void setQuantum(Integer quantum) {
        this.quantum = quantum;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getTurnaroundTime() {
        return turnaroundTime;
    }

    public void setTurnaroundTime(int turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }
    public void incrementPriority() {
        this.priority++;
    }

    @Override
    public String toString() {
        return "models.Process{" +
                "id='" + id + '\'' +
                ", burstTime=" + burstTime +
                ", arrivalTime=" + arrivalTime +
                ", priority=" + (priority ) +
                ", quantum=" + (quantum != null ? quantum : "N/A") +
                '}';
    }
}
