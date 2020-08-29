package team17.Algorithm;

import team17.DAG.Graph;

import java.util.Set;
import java.util.Stack;

public class DFS extends Algorithm {
    PartialSolution _bestSchedule;
    int _upperBound;
    Stack<PartialSolution> _open = new Stack<>();

    public DFS(Graph graph, AlgorithmState algorithmState) {
        super(algorithmState);
        final PartialSolution _root = new PartialSolution(null, null);
        ListScheduling ls = new ListScheduling(graph);
        _bestSchedule = ls.getSchedule();
        _upperBound = _bestSchedule.getScheduledTask().getStartTime();
        // Place first task on first processor and add to stack.
        Set<PartialSolution> children = expandRoot(_root, graph);
        children.forEach(_open::push); //this was lost? cause runtime to get longer
        if (_algorithmState != null) {
            _algorithmState.setCompleteSolution(_bestSchedule);
            _algorithmState.updateNumCompleteSolutions(1);
        }
    }

    @Override
    public PartialSolution getSolution(){
        return _bestSchedule;
    }

    @Override
    public synchronized PartialSolution getNextPartialSolution() {
        if(!_open.isEmpty()) {
            PartialSolution partialSolution = _open.pop();
            if (_algorithmState != null) {
                _algorithmState.updateNumExpandedPartialSolutions(1);
            }
            int costSoFar = partialSolution.getCostUnderestimate();
            if (partialSolution.isCompleteSchedule() && costSoFar < _upperBound) { //TODO && !bestSchedule.equals(partialSolution)
                _upperBound = costSoFar;
                _bestSchedule = partialSolution;
                if (_algorithmState != null) {
                    _algorithmState.setCompleteSolution(_bestSchedule);
                    _algorithmState.updateNumCompleteSolutions(1);
                }
            } else if (partialSolution.isCompleteSchedule() && _algorithmState != null){
                _algorithmState.updateNumCompleteSolutions(1);
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
                if (_algorithmState != null) {
                    _algorithmState.updateNumUnexpandedPartialSolutions(1);
                }
            } else if(_algorithmState != null) {
                _algorithmState.updateNumPruned(1);
            }
        }
    }

}
