package team17.Algorithm;

import java.util.Set;
import team17.DAG.DAGGraph;

public class NThreads extends Thread{
    private final Algorithm _algo;
    private final DAGGraph _graph;

    public NThreads(Algorithm algo, DAGGraph graph) {
        _algo = algo;
        _graph = graph;
    }

    @Override
    public void run() {
        while(true) {
            PartialSolution partialSolution = _algo.getNextPartialSolution();
            if(partialSolution==null) {
                break;
            } else {
                Set<PartialSolution> children = _algo.expandSearch(partialSolution, _graph);
                _algo.openAddChildren(children);
            }
        }
    }
}
