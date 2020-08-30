package team17.Algorithm;

import team17.DAG.DAGGraph;
import team17.DAG.DAGNode;

import java.util.*;

/**
 * Class that contains the main skeleton of the A* algorithm
 */
public class AStar extends Algorithm {
    private final Queue<PartialSolution> _open;
    private final Set<PartialSolution> _closed;
    private final int _upperBound;
    private int _maxOpenCount = 0;

    private boolean _foundComplete = false;

    public AStar(DAGGraph graph, AlgorithmState algorithmState) {
        super(algorithmState);
        final PartialSolution _root = new PartialSolution(null, null);
        _open = new PriorityQueue<>(expandRoot(_root, graph));
        _closed = new HashSet<>();
        // Adds list schedule as upperBound
        ListScheduling ls = new ListScheduling(graph);
        PartialSolution upperBoundListSchedule = ls.getSchedule();
        _open.add(upperBoundListSchedule);
        _upperBound = upperBoundListSchedule.getScheduledTask().getFinishTime();
        if (_algorithmState != null) {
            _algorithmState.setCompleteSolution(upperBoundListSchedule);
            _algorithmState.updateNumCompleteSolutions(1);
            _algorithmState.updateNumUnexpandedPartialSolutions(1);
        }
    }

    @Override
    public PartialSolution getOptimalSchedule(DAGGraph graph) {
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
//                if(maxOpenCount%100==0){
//                    System.out.print("\radded to queue: " + maxOpenCount+"\tstill in queue: "+_open.size());
//                }
            }
        }

        System.out.print("A*: left in queue: "+_open.size());
        System.out.print("\t\tadded to queue: "+ _maxOpenCount);
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
        // Check if free tasks meet criteria. IF yes return node to be ordered next.!!
        fixedTaskOrder(partialSolution, notEligible, freeNodes);
        // skip the nodes for children that were already made in the previous expansion
        boolean skipNodes = true;
        if (partialSolution.getLastPartialExpansionNodeId().equals("")) {
            skipNodes = false;
        }

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
            // skip to the last scheduled node from a previous partial expansion
            if (skipNodes) {
                if (node.getId().equals(partialSolution.getLastPartialExpansionNodeId())) {
                    skipNodes = false;
                } else {
                    continue;
                }
            }
            //Node can be placed on Processor now
            for (int i = 1; i < AlgorithmConfig.getNumOfProcessors() + 1; i++) {
                // skip past the previously scheduled processors from the previous partial expansion
                if (i <= partialSolution.getLastPartialExpansionProcessor() && node.getId().equals(partialSolution.getLastPartialExpansionNodeId())) {
                    continue;
                }
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
                PartialSolution child = new PartialSolution(partialSolution, new ScheduledTask(i, node, eligibleStartTime));
                children.add(child);
                // check if it should do partial expansion
                if (child.getCostUnderestimate() <= partialSolution.getCostUnderestimate()) {
                    partialSolution.setLastPartialExpansionNodeId(node.getId());
                    partialSolution.setLastPartialExpansionProcessor(i);

                    // add parent back into queue
                    _open.offer(partialSolution);
                    _closed.remove(partialSolution);

                    if (_algorithmState != null) {
                        _algorithmState.updateNumExpandedPartialSolutions(-1);
                    }

                    return children;
                }
            }
        }
        return children;
    }

    @Override
    public synchronized PartialSolution getNextPartialSolution() {
        //remove from open and place in closed set
        PartialSolution partialSolution = _open.poll();
        _closed.add(partialSolution);
        if (_algorithmState != null) {
            _algorithmState.updateNumExpandedPartialSolutions(1);
        }
        //if found a complete solution, return null
        if (_foundComplete) {
            return null;
        }
        if (partialSolution != null) {
            if (partialSolution.isCompleteSchedule()) {
                _foundComplete = true;
                _bestCompletePartialSolution = partialSolution;
                if (_algorithmState != null) {
                    _algorithmState.setCompleteSolution(_bestCompletePartialSolution);
                }
                return null;
            }

        }
        return partialSolution;
    }

    /**
     * This adds children to open if partial solution not already present in closed.
     *
     * @param children Set of partial solution children not in closed
     */
    @Override
    public synchronized void openAddChildren(Set<PartialSolution> children) {
        for (PartialSolution child : children) {
            if (!_closed.contains(child) && child.getCostUnderestimate() < _upperBound) {
                _maxOpenCount++;
                _open.offer(child);
                if (_algorithmState != null) {
                    _algorithmState.updateNumUnexpandedPartialSolutions(1);
                    if (child.isCompleteSchedule()) {
                        _algorithmState.updateNumCompleteSolutions(1);
                    }
                }
            } else if (_algorithmState != null) {
                _algorithmState.updateNumPruned(1);
                if (child.isCompleteSchedule()) {
                    _algorithmState.updateNumCompleteSolutions(1);
                }
            }
        }
    }

}
