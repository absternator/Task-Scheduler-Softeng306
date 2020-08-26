package team17.Algorithm;

;
import team17.DAG.Graph;

import java.util.*;

/**
 * Class that contains the main skeleton of the A* algorithm
 */
public class AStar extends Algorithm {
    private final PartialSolution _root;
    private Queue<PartialSolution> _open;
    private Set<PartialSolution> _closed;
    private final int _upperBound;
    private int openCount = 0; // todo: this is for testing only(remove later)
    private PartialSolution _completePartialSolution;
    private boolean _foundComplete = false;

    public AStar(Graph graph) {
        _root = new PartialSolution(null, null);
        _open = new PriorityQueue<>(expandRoot(_root, graph));
        _closed = new HashSet<>();
        // Adds list schedule as upperBound
        ListScheduling ls = new ListScheduling(graph);
        PartialSolution _upperBoundListSchedule = ls.getSchedule();
        _open.add(_upperBoundListSchedule);
        _upperBound = _upperBoundListSchedule.getScheduledTask().getStartTime();
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

        }

        System.out.println(_open.size()); //todo: for testing only(remove later)
        System.out.println(openCount);
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
        _closed.add(partialSolution);
        if (_foundComplete) {
            return null;
        }
        if (partialSolution != null) {
            if (partialSolution.isCompleteSchedule()) {
                _foundComplete = true;
                _completePartialSolution = partialSolution;
                return null;
            }

        }
        return partialSolution;
    }

    /**
     * This adds children to open if partial solution not already present in closed.
     * @param children Set of partial solution children not in closed
     */
    public synchronized void openAddChildren(Set<PartialSolution> children) {
        // TODO: 26/08/20 will be updated futher 
        // This is to add all children at once(not preferred)
//        openCount += children.size();
//        _open.addAll(children);

       for (PartialSolution child : children) {
            if (!_closed.contains(child) && child.getCostUnderestimate() < _upperBound) {
                openCount++;
                _open.offer(child);
            }
        }
    }

}
