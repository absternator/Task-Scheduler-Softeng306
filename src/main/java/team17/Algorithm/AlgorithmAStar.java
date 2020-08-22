package team17.Algorithm;

import team17.DAG.Graph;

import java.util.*;

/**
 * Class that contains the main skeleton of the A* algorithm
 */
public class AlgorithmAStar {
    Queue<PartialSolution> _open;
    List<PartialSolution> _closed;
    private final PartialSolution _root;

    public AlgorithmAStar(Graph graph) {
        _root = new PartialSolution(null, graph, null);
        _open = new PriorityQueue<>();
        _closed = new ArrayList<>();
        _open.add(_root);
    }

    /**
     * This is the actual A* Algorithm that returns the optimal schedule
     *
     * @return The full Schedule which is the optimal solution
     */
    public List<ScheduledTask> getOptimalSchedule() {
        while(true) {
            PartialSolution partialSolution = this.getNextPartialSolution();
            if(partialSolution==null) {
                break;
            } else {
                if (partialSolution.isCompleteSchedule()) {
                    return partialSolution.fullSchedule();
                }
                Set<PartialSolution> children = partialSolution.expandSearch();
                this.openAddChildren(children);
            }
        }
//        while (!_open.isEmpty()) {
//            PartialSolution partialSolution = _open.poll();
//            _closed.add(partialSolution);
//            if (partialSolution.isCompleteSchedule()) {
//                return partialSolution.fullSchedule();
//            }
//            Set<PartialSolution> children = partialSolution.expandSearch();
//            _open.addAll(children);
//            // TODO: 12/08/20 Need to implement partial solution equal to use
////            for (PartialSolution child : children) {
////                if (!closed.contains(child)){
////                    open.offer(child);
////                }
////            }
//        }
        return null;
    }

    /**
     * This is the actual A* Algorithm that returns the optimal schedule in parallel
     *
     * @return The full Schedule which is the optimal solution
     */
    public List<ScheduledTask> getOptimalScheduleParallel(int nCores) {
        List<NThreads> nThreads = new ArrayList<>();
        for(int i=0; i<nCores; i++){
            NThreads thread = new NThreads(this);
            thread.start();
            nThreads.add(thread);
        }
        List<ScheduledTask> schedule = this.getOptimalSchedule();
        if(schedule!=null) {
            return schedule;
        }
        for(NThreads thr : nThreads){
            try {
                thr.join();
                if(thr.getFullSchedule()!=null){
                    return thr.getFullSchedule();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public synchronized PartialSolution getNextPartialSolution(){
        PartialSolution partialSolution = _open.poll();
        if(partialSolution!=null){
            _closed.add(partialSolution);
        }
        return partialSolution;
    }

    public synchronized void openAddChildren(Set<PartialSolution> children){
        _open.addAll(children);
    }
}
