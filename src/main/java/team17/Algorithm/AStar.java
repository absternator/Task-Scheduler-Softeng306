package team17.Algorithm;

import team17.DAG.Graph;

import java.util.*;

/**
 * Class that contains the main skeleton of the A* algorithm
 */
public class AStar extends Algorithm {
    private final PartialSolution _root;
    private Queue<PartialSolution> _open;
    private List<PartialSolution> _closed;
    private PartialSolution _completePartialSolution;
    private boolean _foundComplete = false;

    public AStar(Graph graph) {
        _root = new PartialSolution(null, null);
        _open = new PriorityQueue<>(expandRoot(_root, graph));
        _closed = new ArrayList<>();
    }

    @Override
    public PartialSolution getOptimalSchedule(Graph graph) {
        while (true) {
            PartialSolution partialSolution = this.getNextPartialSolution();
            if (partialSolution == null) {
                break;
            } else {
                if (partialSolution.isCompleteSchedule()) {
                    return partialSolution;
                }
                Set<PartialSolution> children = expandSearch(partialSolution, graph);
                this.openAddChildren(children);
            }
            // TODO: 12/08/20 Need to implement partial solution equal to use 
//            for (PartialSolution child : children) {
//                if (!closed.contains(child)){
//                    open.offer(child);
//                }
//            }
        }
        return _completePartialSolution;
    }

    /**
     * This is the actual A* Algorithm that returns the optimal schedule in parallel
     *
     * @return The full Schedule which is the optimal solution
     */
    public PartialSolution getOptimalScheduleParallel(Graph graph, int nCores) {
        List<NThreads> nThreads = new ArrayList<>();
        for (int i = 0; i < nCores; i++) {
            NThreads thread = new NThreads(this, graph);
            thread.start();
            nThreads.add(thread);
        }
        PartialSolution schedule = this.getOptimalSchedule(graph);
        if (schedule != null) {
            return schedule;
        }
        for (NThreads thr : nThreads) {
            try {
                thr.join();
                if (thr.getCompletePartialSolution() != null) {
                    return thr.getCompletePartialSolution();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return _completePartialSolution;
    }

    public synchronized PartialSolution getNextPartialSolution() {
        PartialSolution partialSolution = _open.poll();
        if (_foundComplete) {
            return null;
        }
        if (partialSolution != null) {
            if (partialSolution.isCompleteSchedule()) {
                _foundComplete = true;
                _completePartialSolution = partialSolution;
                return null;
            }
            _closed.add(partialSolution);
        }
        return partialSolution;
    }

    /**
     * This adds children to open if partial solution not already present in closed.
     * @param children Set of partial solution children not in closed
     */
    public synchronized void openAddChildren(Set<PartialSolution> children) {
//        _open.addAll(children);   This is to add all children at once(not preferred)
        for (PartialSolution child : children) {
            if (!_closed.contains(child)) {
                _open.offer(child);
            }
        }
    }

}
