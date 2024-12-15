# CPU Scheduler Simulator

This project is a **CPU Scheduling Simulator** that simulates and visualizes different CPU scheduling algorithms. The implemented algorithms are **Non-preemptive Priority Scheduling**, **Non-preemptive Shortest Job First (SJF)**, **Shortest Remaining Time First (SRTF)**, and a custom **FCAI Scheduling** algorithm. The simulator includes functionality for generating Gantt charts to visualize the scheduling process and handle different scheduling scenarios.

## Features

- **Non-preemptive Priority Scheduling** with context switching
- **Non-preemptive Shortest Job First (SJF)** with starvation handling
- **Shortest Remaining Time First (SRTF)** with context switching and starvation handling
- **FCAI Scheduling**, an adaptive algorithm combining priority, arrival time, and remaining burst time

## Algorithms Explained

### 1. **Non-preemptive Priority Scheduling**
This algorithm selects the process with the highest priority for execution. If two processes have the same priority, they are executed in the order of their arrival time. Context switching is involved when switching between processes.

### 2. **Non-preemptive Shortest Job First (SJF)**
This scheduling algorithm selects the process with the shortest burst time for execution. If there are processes with equal burst time, they are executed in the order of their arrival time. The starvation problem is solved by dynamically adjusting the priorities of processes.

### 3. **Shortest Remaining Time First (SRTF)**
This is a preemptive version of the SJF algorithm. It selects the process with the shortest remaining burst time. The process may be preempted if a new process with a shorter burst time arrives. Context switching is included, and starvation is handled by increasing the priority of waiting processes.

### 4. **FCAI Scheduling**
FCAI Scheduling combines priority, arrival time, and remaining burst time into an adaptive **FCAI Factor**. This factor determines the execution order and quantum allocation for each process. FCAI scheduling minimizes starvation and optimizes process execution in systems with varying burst times and priorities.

## Input Format

When you run the simulation, you will be prompted to input the following:

1. **Choose a scheduling algorithm**:
   - 1 for **Shortest Job First (SJF)**
   - 2 for **Shortest Remaining Time First (SRTF)**
   - 3 for **Priority Scheduling**
   - 4 for **FCAI Scheduling**

2. **Enter the number of processes** (e.g., 4 processes).

3. **Enter the details for each process**:
   For each process, provide the following details:
   - **Process ID** (an integer)
   - **Burst Time** (an integer)
   - **Arrival Time** (an integer)
   - **Priority** (an integer) — Only applicable for Priority Scheduling
   - **Color** (string) — For process visualization (optional)

### Example Input

For selecting **Shortest Job First (SJF)** with 4 processes:

```plaintext
Please select the scheduling algorithm:
1. Shortest Job First (SJF)
2. Shortest Remaining Time First (SRTF)
3. Priority Scheduling
4. FCAI Scheduling
Enter your choice (1-4): 1

Enter the number of processes: 4

Enter Process ID, Burst Time, Arrival Time, Priority, Color (separated by spaces):
1 8 0 1 red
2 2 1 3 blue
3 4 2 2 green
4 3 3 1 yellow

```
### Output

Once the input is provided, the simulator will execute the scheduling algorithm and display the following information:

- The list of processes and their attributes.
- The order in which the processes are executed.
- Waiting Time (WT) and Turnaround Time (TAT) for each process.
- Average Waiting Time and Average Turnaround Time.
- If starvation is detected, the priority of a waiting process will be adjusted.

### Example Output

```plaintext
Processes:
Process{id='1', burstTime=8, arrivalTime=0, priority=1, quantum=N/A, color='red'}
Process{id='2', burstTime=2, arrivalTime=1, priority=3, quantum=N/A, color='blue'}
Process{id='3', burstTime=4, arrivalTime=2, priority=2, quantum=N/A, color='green'}
Process{id='4', burstTime=3, arrivalTime=3, priority=1, quantum=N/A, color='yellow'}

Processes execution order:
-> 1
-> 2
-> 4
Starvation detected, decreasing priority of Process 3
-> 3

Process Details:
1: Waiting Time = 0, Turnaround Time = 8
2: Waiting Time = 7, Turnaround Time = 9
4: Waiting Time = 7, Turnaround Time = 10
3: Waiting Time = 11, Turnaround Time = 15

Average WT : 6.25
Average TAT : 10.50

Results saved to result.json.
```

## File Structure

```plaintext
CPUSchedulerSimulator/
├── models/
│   └── Process.java              # Represents a process
├── schedulers/
│   ├── FCAI_Scheduling.java      # FCAI Scheduling algorithm
│   ├── PriorityScheduler.java    # Non-preemptive Priority Scheduling
│   ├── Scheduler.java            # Base scheduler class
│   ├── SJFScheduler.java         # Non-preemptive Shortest Job First (SJF)
│   └── SRTFScheduler.java        # Shortest Remaining Time First (SRTF)
├── CPUSchedulerSimulator.java    # Main class to run the simulation
├── InputHandler.java             # Handles input and process generation
├── SchedulerManager.java         # Manages the scheduling algorithms
├── gantt_chart.png               # Gantt chart visualization of the scheduling
├── generate_gantt_chart.py       # Python script to generate Gantt chart
├── result.json                   # Output results in JSON format
└── README.md                     # This file
```

## Getting Started

### Prerequisites

- **Java 8 or higher**: Ensure you have Java installed on your machine.
- **Python (for generating Gantt chart)**: Install Python to generate visualizations.

### Setup

Clone this repository to your local machine:

```bash
git clone https://github.com/Manounnaa/CPUSchedulerSimulator.git
cd CPUSchedulerSimulator
```

### Running the Simulator

1. **Compile the Java files:**

```bash
javac -d bin src/*.java
```

2. **Run the CPU Scheduler Simulator:**

```bash
java -cp bin CPUSchedulerSimulator
```

3. **Choose a scheduling algorithm (1-4) when prompted.**
4. **Enter the process details (burst time, arrival time, priority, etc.) as requested.**
5. The simulator will output the scheduling results, including a Gantt chart.

### Generating the Gantt Chart

The simulator generates the scheduling results in a **JSON** format, which can be visualized using the Python script `generate_gantt_chart.py`. 

To generate the Gantt chart:

1. Install Python and required libraries:

```bash
pip install matplotlib
```

2. Run the Python script:

```bash
python generate_gantt_chart.py
```

This will generate the Gantt chart (`gantt_chart.png`) for the selected scheduling algorithm.

## Contributing

Feel free to fork this repository, create a branch, and submit a pull request if you have any improvements or bug fixes.

## License

For more information, visit the repository: [https://github.com/Manounnaa/CPUSchedulerSimulator](https://github.com/Manounnaa/CPUSchedulerSimulator)


