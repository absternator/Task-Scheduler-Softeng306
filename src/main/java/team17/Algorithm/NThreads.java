package team17.Algorithm;

import java.util.Set;
import team17.DAG.Graph;

public class NThreads extends Thread{
    AStar _aStar;
    Graph _graph;
    PartialSolution _completePartialSolution;

    public NThreads(AStar aStar, Graph graph) {
        _aStar = aStar;
        _graph = graph;
    }

    public PartialSolution getCompletePartialSolution() {
        return _completePartialSolution;
    }

    @Override
    public void run() {
        while(true) {
            PartialSolution partialSolution = _aStar.getNextPartialSolution();
            if(partialSolution==null) {
                break;
            } else {
                if (partialSolution.isCompleteSchedule()) {
                    _completePartialSolution = partialSolution;
                    break;
                }
                Set<PartialSolution> children = _aStar.expandSearch(partialSolution, _graph);
                _aStar.openAddChildren(children);
            }
        }
    }
}
