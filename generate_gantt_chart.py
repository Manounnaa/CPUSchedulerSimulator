import matplotlib.pyplot as plt
import json
import random

def generate_gantt_chart(timeline_data):
    try:
        data = json.loads(timeline_data)
        timeline = data['timeline']  # Extracting the timeline list
        avg_waiting_time = data['avg_waiting_time']  # Extract average waiting time
        avg_turnaround_time = data['avg_turnaround_time']  # Extract average turnaround time
        print("Timeline data loaded successfully:", timeline)  # Debugging line
    except json.JSONDecodeError as e:
        print(f"Error decoding JSON: {e}")
        return
    except KeyError as e:
        print(f"Key error: The key {e} is missing in the JSON data.")
        return

    process_colors = {}
    fig, ax = plt.subplots(figsize=(10, 6))

    # Prepare for plotting
    y_positions = [1] * len(timeline)  # All tasks are placed at the same Y level
    start_times = [event['start_time'] for event in timeline]
    durations = [event['duration'] for event in timeline]
    process_labels = [event['process'] for event in timeline]

    for i, event in enumerate(timeline):
        process = event['process']
        if process not in process_colors:
            process_colors[process] = (random.random(), random.random(), random.random())  # Random colors for each process
        ax.barh(y_positions[i], durations[i], left=start_times[i], color=process_colors[process], edgecolor='black')

    # Add process labels
    for i, event in enumerate(timeline):
        ax.text(event['start_time'] + event['duration'] / 2, y_positions[i], event['process'],
                ha='center', va='center', color='white', fontsize=10)

    # Add average waiting time and turnaround time to the plot
    ax.text(0.5, 1.05, f"Avg Waiting Time: {avg_waiting_time:.2f}", ha='center', va='bottom', fontsize=12, transform=ax.transAxes)
    ax.text(0.5, 1.1, f"Avg Turnaround Time: {avg_turnaround_time:.2f}", ha='center', va='bottom', fontsize=12, transform=ax.transAxes)

    ax.set_yticks([])  # Remove Y ticks as all bars are on the same level
    ax.set_xlabel("Time")
    ax.set_title("Gantt Chart")

    # Display plot
    plt.tight_layout()
    plt.show()
    plt.savefig("gantt_chart.png")

if __name__ == '__main__':
    # Define the path to the JSON file (no user input required)
    file_path = r"C:\Users\Ahmed\IdeaProjects\CPUSchedulerSimulator3\result.json"
    print(f"Looking for file at: {file_path}")

    try:
        with open(file_path, 'r') as f:
            print("File found, reading data...")
            data = f.read()
            print("Data read from file:", data)  # Debugging line

        generate_gantt_chart(data)
    except FileNotFoundError:
        print(f"Error: The file '{file_path}' was not found.")
    except Exception as e:
        print(f"Unexpected error: {e}")
