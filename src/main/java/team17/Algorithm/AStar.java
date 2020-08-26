package team17.Algorithm;

import team17.DAG.Graph;

import java.util.*;

/**
 * Class that contains the main skeleton of the A* algorithm
 */
public class AStar extends Algorithm {
    private final Queue<PartialSolution> _open;
    private final List<PartialSolution> _closed;
    private PartialSolution _completePartialSolution;
    private boolean _foundComplete = false;

    public AStar(Graph graph) {
        final PartialSolution _root = new PartialSolution(null, null);
        _open = new PriorityQueue<>();
        _closed = new ArrayList<>();
        _open.add(_root);
    }

    @Override
    public PartialSolution getSolution(){
        return _completePartialSolution;
    }

    @Override
    public synchronized PartialSolution getNextPartialSolution(){
        PartialSolution partialSolution = _open.poll();
        if(_foundComplete) {
            return null;
        }
        if(partialSolution!=null){
            if (partialSolution.isCompleteSchedule()) {
                _foundComplete = true;
                _completePartialSolution = partialSolution;
                return null;
            }
            _closed.add(partialSolution);
        }
        return partialSolution;
    }

    @Override
    public synchronized void openAddChildren(Set<PartialSolution> children){
        _open.addAll(children);
    }
}
