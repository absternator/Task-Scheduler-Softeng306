package team17.Algorithm;

import team17.DAG.Graph;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class DFS extends Algorithm {
    ListScheduling _ls;
    PartialSolution _bestSchedule;
    int _upperBound;
    Stack<PartialSolution> _open = new Stack<>();
    int count = 0;
    Set<PartialSolution> closed = new HashSet<>();

    public DFS(Graph graph) {
        final PartialSolution _root = new PartialSolution(null, null,0);
        _ls = new ListScheduling(graph);
        _bestSchedule = _ls.getSchedule();
        _upperBound = _bestSchedule.getScheduledTask().getFinishTime();
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
//            System.out.println(closed.size());
            if(closed.size() == 4999999) {
                System.out.println("hi");
            }
            if (partialSolution.isCompleteSchedule()) {
                int costSoFar = partialSolution.getScheduledTask().getFinishTime();
                if(costSoFar < _upperBound) {//TODO && !bestSchedule.equals(partialSolution)
                    _upperBound = costSoFar;
                    _bestSchedule = partialSolution;
                    count++;
                    System.out.println(count);
                }
            }
            return partialSolution;
        } else {
            return null;
        }
    }

    @Override
    public synchronized void openAddChildren(Set<PartialSolution> children) {
        for (PartialSolution child : children) {
            int cost = child.getCostUnderestimate();
            if (cost < _upperBound && !closed.contains(child)) {
                _open.push(child);
                if(closed.size() < 5000000) {
                    closed.add(child);
                }
            }
        }
    }

}
