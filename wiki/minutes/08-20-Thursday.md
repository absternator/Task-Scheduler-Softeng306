# THURSDAY 20 AUG
## Parallelisation - Jacinta
### Potential methods
- Top n in the queue. Only problem: could be doing expansion of "not the best option"
- After expanding one, go through all the children finding the best 

N = 1 core = 1 thread

## Algorithm
Run out of memory for A* , so switch over to DFS.  
Low priority task: A* pruning

### Pruning methods:
- Identical nodes
- Equal partial solutions
- Complete solution comparison

- Nodes with no dependencies need a pruning method! -> Heuristics - DM
### DFS
#### DFS pseudocode
- BestSchedule(actual schedule with best cost) <- List scheduling solution
- B(best cost) <- cost of BestSchedule
- if B = critical path then return
  
- If there are free tasks
  - For each free task:
    - Add root to stack
    - While(stack is not empty)
      - Stack pop off top most,
       - if complete and less than B
          - set as B and BestSchedule
      - Else expand
      - If cost of children is less than B 
          - then add children to stack
    - End while





