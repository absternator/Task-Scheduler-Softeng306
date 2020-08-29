package team17.Algorithm;

import team17.DAG.DAGGraph;

import java.util.Set;
import java.util.Stack;

public class DFS extends Algorithm {
    ListScheduling _ls;
    PartialSolution _bestSchedule;
    int _upperBound;
    Stack<PartialSolution> _open = new Stack<>();
    private final AlgorithmState _algorithmState;

    public DFS(DAGGraph graph, AlgorithmState algorithmState) {
        final PartialSolution _root = new PartialSolution(null, null);
        _ls = new ListScheduling(graph);
        _bestSchedule = _ls.getSchedule();
        _algorithmState = algorithmState;
        if (_algorithmState != null) {
            _algorithmState.setCompleteSolution(_bestSchedule);
        }
        _upperBound = _bestSchedule.getCostUnderestimate();
        _open.push(_root);
    }

    @Override
    public PartialSolution getSolution(){
        return _bestSchedule;
    }

    @Override
    public synchronized PartialSolution getNextPartialSolution() {
        if(!_open.isEmpty()) {
            PartialSolution partialSolution = _open.pop();
            int costSoFar = partialSolution.getCostUnderestimate();
            if (partialSolution.isCompleteSchedule() && costSoFar < _upperBound) { //TODO && !bestSchedule.equals(partialSolution)
                _upperBound = costSoFar;
                _bestSchedule = partialSolution;
                if (_algorithmState != null) {
                    _algorithmState.setCompleteSolution(_bestSchedule);
                }
            }
            return partialSolution;
        } else {
            return null;
        }
    }

    @Override
    public synchronized void openAddChildren(Set<PartialSolution> children) {
        for (PartialSolution child:children) {
            int cost = child.getCostUnderestimate();
            if (cost < _upperBound) {
                _open.push(child);
            }
        }
    }

}
