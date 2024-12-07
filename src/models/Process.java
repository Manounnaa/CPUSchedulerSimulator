package models;

import java.util.List;

public class Process {
    private String id;          // models.Process ID
    private int burstTime;      // Burst Time
    private int arrivalTime;    // Arrival Time
    private int priority;   // Priority
    private int quantum;    // Quantum (optional) 4
    private int remainingTime;      // Burst Time
    private int age;  // New field for aging
    private String color;       // Color for Gantt chart



    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    private int waitingTime;    // Time the process waits before execution
    private int turnaroundTime; // Total time from arrival to completion

    public Process(String id, int burstTime, int arrivalTime, int priority , int quantum) {
        this.id = id;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.arrivalTime = arrivalTime;
        this.priority = priority;
        this.quantum = quantum;
        this.waitingTime = 0;
        this.turnaroundTime = 0;
        this.age = 0;  // Initial age is 0
        this.color = "gray";
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
    public Process(String id, int burstTime, int arrivalTime, int priority, int quantum, String color) {
        this.id = id;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.arrivalTime = arrivalTime;
        this.priority = priority;
        this.quantum = quantum;
        this.waitingTime = 0;
        this.turnaroundTime = 0;
        this.age = 0;
        this.color = color;
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

    public int getQuantum() {
        return quantum;
    }

    public void setQuantum(int quantum) {
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
    public int getAge() {
        return age;
    }

    // Increment the age of the process
    public void incrementAge() {
        this.age++;
    }

    // Reset the age (if necessary)
    public void resetAge() {
        this.age = 0;
    }
    public String getColor() {
        return color;
    }



    // Increase the priority based on the age (this is where you can define the logic for aging)
    public void increasePriority() {
        if (this.age > 10) { // Example threshold for aging (process waits for too long)
            this.priority += 1;  // Increase priority
            this.age = 0;        // Reset age after priority increase
        }
    }
    public void decreasePriority() {
        if (this.priority > 0) {
            this.priority--; // Decrease priority to prevent starvation
            System.out.println("Starvation detected, decreasing priority of Process " + this.getId());

        }
    }

    @Override
    public String toString() {
        return "Process{" +
                "id='" + id + '\'' +
                ", burstTime=" + burstTime +
                ", arrivalTime=" + arrivalTime +
                ", priority=" + (priority ) +
                ", quantum=" + (quantum != 0 ? quantum : "N/A") +
                ", color='" + color + '\'' +
                '}';
    }
}
