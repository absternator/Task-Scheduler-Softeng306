#Project Structure

##Input/Output
###CLI:
This class takes in command line inputs and decides what mode of the program to run and how many processors the program shall run on.
### FileReadWriter:
The class parses the input directed acyclic graph and
stores the relevant information in the `DAGGraph` and `DAGNode` classes.
Then a schedule is generated, and the result is written in an output file.
##DAG
###DAGNode:
This class stores each task from the input graph.
###DAGGraph:
This class stores the input graph. It illustrates all edges between each `DAGNode`.
##Algorithm
###ScheduledTask:
This class stores a task scheduled on a  processor with a start time.
###Partial Solution:
This class represents a partial solution when searching the state space tree.
###AlgorithmConfig:
This class is a helper class  stores the number of processors input, and the total node weights of the input graph.
###AlgorithmState:
This class stores the state of the running algorithm.
This state is used for visualisation.
###Algorithm:
This abstract class outlines the methods in the `DFS` and `AStar` classes.
###ListScheduling:
This class performs the greedy algorithm and generates a schedule. 
###DFS:
This class performs a depth-first search branch and bound algorithm to generate a schedule.
###NThreads:
This class is utilised for parallelization. It used to perform operations on multiple threads.
##GUI
###MainController:
This class controls the main Graphical user interface.
###GraphVisualisation:
This class displays an interactive graph corresponding to the inout graph.
###GantChart
This class displays the best current schedule.






