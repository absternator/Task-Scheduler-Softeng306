# Monday 10 AUG
## Psuedocode for A* algorithm
```
OPEN <- initialise with nodes that have no incoming edges, ordered by cost.
WHILE (OPEN is not empty)
  let s = first state in OPEN
  if s is completed
    DONE
  else
    grab its children and figure out costs (create more partial solutions)
  STATES = partial solutions created (expand() more partial solutions)
  OPEN <- OPEN + STATES
```
## Classes
### Node
- weight
- id
- set<Node> incoming
- set <Node> outgoing

### Graph
- set<Node> nodes in the graph

### Algorithm
- priority queue (probably in a function)
- number of processes
- A*Algorithm (Graph), returns solution

### Partial Solution
- parent
- set<ScheduledTask>
- cost up to this point
- bottom level
- expand(), returns set<PartialSolution>

### ScheduledTask
- processorNum
- Node
- Start time

