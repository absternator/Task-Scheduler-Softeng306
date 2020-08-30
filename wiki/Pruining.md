#Pruning
##Duplication Detection
Duplicate partial solutions are handled by using a CLOSED set.
Before adding to the data structure of choice for A* or DFS, the partial solution is checked if in CLOSED set. 
##Heuristics
Heuristics are utilised for both DFS and A*.They represent the cost underestimate from a partial solution to the full solution. Once again a partial solution is checked to ensure the cost underestimate is lower than the upper bound.
The cost underestimate is also used to determine Priority in the A* priority queue OPEN.
The heuristics used are as follows:
* Load Balancing - This method gets the (total weight of all nodes + idle time)/total number of processors
* Bottom Level - This method takes the start time of task + the bottom level of node
* Bottom Load - This method takes the start time of task + the total weight of all its children.
## Fixed Task Ordering
This method is for handling graphs with forks, joins, independent and fork-joins.
Sinnen, O. (2014). Reducing the solution space of optimal task scheduling.
As O. Sinnen describes, if certain conditions are met, tasks can be scheduled in order.
This is the basis of this method. 
## Upper Bound
List scheduling algorithm was run to find a full solution that would serve as an upper bound for the Algorithms.
For A*, partial solutions are only added to the OPEN priority queue if the Partial solutions cost underestimate is less than the upper bound. The upper bound solution is also added to the OPEN queue.
DFS is similar to A*, however if a full solution with a cost less than the upper bound is obtained. That solution becomes the new upper bound.
## Partial Expansion
This method is utilised for A*. The method is as follows:
If a child partial solution of a parent has a cost underestimate lower than the parent, then the child is expanded rather than the parent. The parent is then placed back in the priority queue.
## Identical Node Detection
If two nodes have the same parents and children and same edge weights, then can be labelled as identical.
Thus, in this situation we schedule one identical node first, then the other node. 

