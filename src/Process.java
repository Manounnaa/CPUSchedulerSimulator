public class Process {
    private String id;          // Process ID
    private int burstTime;      // Burst Time
    private int arrivalTime;    // Arrival Time
    private Integer priority;   // Priority (optional) 3 ,4
    private Integer quantum;    // Quantum (optional) 4

    // Constructor
    public Process(String id, int burstTime, int arrivalTime, Integer priority, Integer quantum) {
        this.id = id;
        this.burstTime = burstTime;
        this.arrivalTime = arrivalTime;
        this.priority = priority;
        this.quantum = quantum;
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

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getQuantum() {
        return quantum;
    }

    public void setQuantum(Integer quantum) {
        this.quantum = quantum;
    }

    @Override
    public String toString() {
        return "Process{" +
                "id='" + id + '\'' +
                ", burstTime=" + burstTime +
                ", arrivalTime=" + arrivalTime +
                ", priority=" + (priority != null ? priority : "N/A") +
                ", quantum=" + (quantum != null ? quantum : "N/A") +
                '}';
    }
}
