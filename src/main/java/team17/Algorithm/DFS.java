package team17.Algorithm;

import team17.DAG.DAGGraph;
import team17.DAG.DAGNode;

import java.util.*;

public class DFS extends Algorithm {
    int _upperBound;
    Stack<PartialSolution> _open = new Stack<>();
    Set<PartialSolution> _closed = new HashSet<>();
    int foundfull;

    public DFS(DAGGraph graph, AlgorithmState algorithmState) {
        super(algorithmState);
        final PartialSolution _root = new PartialSolution(null, null);
        ListScheduling ls = new ListScheduling(graph);
        _bestCompletePartialSolution = ls.getSchedule();
        _upperBound = _bestCompletePartialSolution.getScheduledTask().getFinishTime();
        // Place first task on first processor and add to stack.
        Set<PartialSolution> children = expandRoot(_root, graph);
        children.forEach(_open::push); //this was lost? cause runtime to get longer
        if (_algorithmState != null) {
            _algorithmState.setCompleteSolution(_bestCompletePartialSolution);
            _algorithmState.updateNumCompleteSolutions(1);
        }
    }

    @Override
    public PartialSolution getOptimalSchedule(DAGGraph graph) {
        while (true) {
            PartialSolution partialSolution = this.getNextPartialSolution();
            if (partialSolution == null) {
                break;
            } else {
                Set<PartialSolution> children = expandSearch(partialSolution, graph);
                this.openAddChildren(children);
            }
        }
        return _bestCompletePartialSolution;
    }

    @Override
    public Set<PartialSolution> expandSearch(PartialSolution partialSolution, DAGGraph graph) {
        Set<PartialSolution> children = new HashSet<>();
        Set<DAGNode> nodesInSchedule = new HashSet<>();
        List<DAGNode> freeNodes = new ArrayList<>(graph.getNodeList()); //nodes that are eligible to be scheduled
        Set<DAGNode> notEligible = new HashSet<>();
        //Go through and remove indelible nodes
        for (ScheduledTask scheduledTask : partialSolution) {
            nodesInSchedule.add(scheduledTask.getNode());
            freeNodes.remove(scheduledTask.getNode());
        }
        for (DAGNode node : freeNodes) {
            for (DAGNode dependency : node.getDependencies()) {
                if (!nodesInSchedule.contains(dependency)) {
                    notEligible.add(node);
                }
            }
        }
        freeNodes.removeAll(notEligible);
        notEligible.clear();
        // Check if free tasks meet criteria. IF yes return node to be ordered next.
        fixedTaskOrder(partialSolution, notEligible, freeNodes);

        AddNode:
        for (DAGNode node : freeNodes) {

            // if a sibling has already scheduled an equivalent node
            for (PartialSolution child : children) {
                if (child.getScheduledTask().getNode().isEquivalent(node)) {
                    if (_algorithmState != null) {
                        _algorithmState.updateNumPruned(1);
                    }
                    continue AddNode;
                }
            }
            //Node can be placed on Processor now
            for (int i = 1; i < AlgorithmConfig.getNumOfProcessors() + 1; i++) {
                int eligibleStartTime = 0;
                // Start time based on  last task on this processor
                for (ScheduledTask scheduledTask : partialSolution) {
                    if (scheduledTask.getProcessorNum() == i) {
                        eligibleStartTime = scheduledTask.getFinishTime();
                        break;
                    }
                }
                //Start time based on dependencies on  OtherProcessors
                for (ScheduledTask scheduledTask : partialSolution) {
                    if (scheduledTask.getProcessorNum() != i) {
                        boolean dependantFound = false;
                        int communicationTime = 0;
                        for (DAGNode edge : node.getIncomingEdges().keySet()) {
                            if (edge.equals(scheduledTask.getNode())) {
                                dependantFound = true;
                                communicationTime = node.getIncomingEdges().get(edge);
                            }
                        }
                        if (!dependantFound) {
                            continue;
                        }
                        eligibleStartTime = Math.max(eligibleStartTime, scheduledTask.getFinishTime() + communicationTime);
                    }
                }
                children.add(new PartialSolution(partialSolution, new ScheduledTask(i, node, eligibleStartTime)));
            }
        }
        return children;
    }


    @Override
    public synchronized PartialSolution getNextPartialSolution() {
        if (!_open.isEmpty()) {
            PartialSolution partialSolution = _open.pop();
            System.out.println(_closed.size());
            if (_algorithmState != null) {
                _algorithmState.updateNumExpandedPartialSolutions(1);
            }

            if (partialSolution.isCompleteSchedule()) { //TODO && !bestSchedule.equals(partialSolution)
                int fullSolutionCost = partialSolution.getScheduledTask().getStartTime();
                if (fullSolutionCost < _upperBound) {
                    _upperBound = fullSolutionCost;
                    _bestCompletePartialSolution = partialSolution;
                    if (_algorithmState != null) {
                        _algorithmState.setCompleteSolution(_bestCompletePartialSolution);
                    }
                }
                if (_algorithmState != null) {
                    _algorithmState.updateNumCompleteSolutions(1);

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
            if (cost < _upperBound && !_closed.contains(child)) {
                _open.push(child);
                if(_closed.size() < 2500000){
                    _closed.add(child);
                }
                if (_algorithmState != null) {
                    _algorithmState.updateNumUnexpandedPartialSolutions(1);
                }
            } else if (_algorithmState != null) {
                _algorithmState.updateNumPruned(1);
            }
        }
    }

}
