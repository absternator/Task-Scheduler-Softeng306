#Algorithm
The scheduling problem can be solved fast using greedy algorithms such as list scheduling or optimally using state space search algorithms. These include A* and depth first search(DFS) branch and bound
##Greedy Algorithm
A List scheduling algorithm was implemented to help improve the performance of other algorithms implemented.
This algorithm is very fast but does not return an optimal solution. Thus we explore state space techniques to solve the scheduling problem.
##State Space Search
This method involves searching all permutations and combinations of tasks scheduled on different processors.
This method is slower than the greedy algorithm, but it provides an optimal solution. The 2 state space techniques implemented were A* and Depth first search branch and bound.
Both techniques search through `PartialSolutions` that contain `ScheduledTasks`.
##A*
This method involves searching through the the state space tree and storing each Partial solution in a priority queue.
The priority is determined by the cost underestimate to the final solution. This is explained further in Pruning/Heretics.
When a complete solution is at the top of the queue, the algorithm finishes.
The algorithm returns a optimal solution, however for larger graphs takes a long time and consumes lots of memory.
###DFS Branch-and-Bound
This method performs the state search in a depth first search way. This uses a stack data structure to achieve this.
A upper bound is created using the list scheduling solution. Also a heuristic is used like A* to create a cost underestimate.
A partial solution is then only added to the stack if the cost underestimate is lower than the upper bound. If a full schedule is lower than the upper bound, that schedule becomes the new upper bound.
When the tree is fully searched, the best solution is returned.
