package team17.Algorithm;

import team17.DAG.Graph;
import team17.DAG.Node;

import java.util.*;

/**
 * Class that contains the main skeleton of the A* algorithm
 */
public class AStar extends Algorithm {
    private final Queue<PartialSolution> _open;
    private final List<Set<PartialSolution>> _closed;
    private final int _upperBound;
    private int maxOpenCount = 0; // todo: this is for testing only(remove later)

    private PartialSolution _completePartialSolution;
    private boolean _foundComplete = false;
    private AlgorithmState _algorithmState;

    public AStar(Graph graph, AlgorithmState algorithmState) {
        final PartialSolution _root = new PartialSolution(null, null);
        _open = new PriorityQueue<>(expandRoot(_root, graph));
        _closed = new ArrayList<>();
        // make a separate set for each possible schedule length
        for (int i = 0; i <= graph.getNodeList().size(); i++) {
            _closed.add(new HashSet<>());
        }
        // Adds list schedule as upperBound
        ListScheduling ls = new ListScheduling(graph);
        PartialSolution _upperBoundListSchedule = ls.getSchedule();
        _open.add(_upperBoundListSchedule);
        _upperBound = _upperBoundListSchedule.getScheduledTask().getStartTime();
        _algorithmState = algorithmState;
    }

    @Override
    public PartialSolution getSolution() {
        return _completePartialSolution;
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

        System.out.println("left in queue: " + _open.size()); //todo: for testing only(remove later)
        System.out.println("added to queue: " + maxOpenCount);
        return _completePartialSolution;
    }

    @Override
    public Set<PartialSolution> expandSearch(PartialSolution partialSolution, Graph graph) {
        Set<PartialSolution> children = new HashSet<>();
        Set<Node> nodesInSchedule = new HashSet<>();
        List<Node> freeNodes = new ArrayList<>(graph.getNodeList()); //nodes that are eligible to be scheduled
        Set<Node> notEligible = new HashSet<>();
        //Go through and remove indelible nodes
        for (ScheduledTask scheduledTask : partialSolution) {
            nodesInSchedule.add(scheduledTask.getNode());
            freeNodes.remove(scheduledTask.getNode());
        }
        for (Node node : freeNodes) {
            for (Node dependency : node.getDependencies()) {
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
        for (Node node : freeNodes) {

            // if a sibling has already scheduled an equivalent node
            for (PartialSolution child : children) {
                if (child.getScheduledTask().getNode().isEquivalent(node)) {
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
                        for (Node edge : node.getIncomingEdges().keySet()) {
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
                    _closed.get(partialSolution.getScheduleLength()).remove(partialSolution);

                    return children;
                }
            }
        }
        return children;
    }

    @Override
    public synchronized PartialSolution getNextPartialSolution() {

        PartialSolution partialSolution = _open.poll();
        _closed.get(partialSolution.getScheduleLength()).add(partialSolution);
        if (_foundComplete) {
            return null;
        }
        if (partialSolution != null) {
            if (partialSolution.isCompleteSchedule()) {
                _foundComplete = true;
                _completePartialSolution = partialSolution;
                if (_algorithmState != null) {
                    _algorithmState.setCompleteSolution(_completePartialSolution);
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
    public synchronized void openAddChildren(Set<PartialSolution> children) {
        // TODO: 26/08/20 will be updated further
        // This is to add all children at once(not preferred)
//        maxOpenCount += children.size();
//        _open.addAll(children);
        for (PartialSolution child : children) {
            if (!_closed.get(child.getScheduleLength()).contains(child) && child.getCostUnderestimate() < _upperBound) {
                maxOpenCount++;
                _open.offer(child);
            }
        }
    }

}
