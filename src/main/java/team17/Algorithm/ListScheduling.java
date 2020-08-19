package team17.Algorithm;

import team17.DAG.Graph;
import team17.DAG.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that contains methods associated with the list scheduling algorithm
 */
public class ListScheduling {
    private Graph _graph;

    public ListScheduling(Graph graph) {
        _graph = graph;
    }

    /**
     * Sorts the nodes by topological order, prioritised by the node's weight
     * Assumes there are no cycles in the graph.
     *
     * @return sorted list of nodes
     */
    public List<Node> getTopologicalOrder() {
        // get a copy of nodes for the unordered list
        List<Node> unorderedNodes = new ArrayList<>();
        for (Node node : _graph.getNodeList()) {
            if (!node.getId().equals("end")) {
                unorderedNodes.add(new Node(node));
            }
        }

        List<Node> orderedNodes = new ArrayList<>();

        int largestWeight;
        Node nextNode = unorderedNodes.get(0);

        while (!unorderedNodes.isEmpty()) {
            largestWeight = -1;

            // add the largest node with no dependencies to the list.
            for (Node node : unorderedNodes) {
                if (node.getDependencies().size() == 0 && node.getWeight() > largestWeight) {
                    largestWeight = node.getWeight();
                    nextNode = node;
                }
            }
            orderedNodes.add(nextNode);

            // remove that node from unordered list
            unorderedNodes.remove(nextNode);

            // remove that node as a dependency
            for (Node node : unorderedNodes) {
                node.getDependencies().remove(nextNode);
            }
        }

        // return a list of the original nodes, using the topological order
        List<Node> nodes = new ArrayList<>();
        for (Node node : orderedNodes) {
            for (Node graphNode : _graph.getNodeList()) {
                if (node.equals(graphNode)) {
                    nodes.add(graphNode);
                }
            }
        }
        return nodes;
    }

    /**
     * Generates a valid solution for a graph input
     * @return a valid schedule
     */
    public List<ScheduledTask> getSchedule() {
        int numProcessors = _graph.getNumOfProcessors();
        ScheduledTask[] processors = new ScheduledTask[numProcessors]; // latest task of each processor
        List<Node> nodes = getTopologicalOrder();
        List<ScheduledTask> schedule = new ArrayList<>();

        int earliestStart; // the earliest start time for a node
        int processor; // the processor to add the next node to
        int finishTime; // the finish time of the processor
        int latestCommunicationTime;
        int currentCommunicationTime;
        ScheduledTask scheduledTask;

        for (Node node : nodes) {
            earliestStart = Integer.MAX_VALUE;
            processor = 0;

            // find the processor with the earliest start time
            if (node.getDependencies().size() == 0) {

                // get the processor with earliest start time for nodes with no dependencies
                for (int p = 0; p < numProcessors; p++) {
                    // if there are no previously scheduled tasks on a processor, finishTime is 0
                    if (processors[p] != null) {
                        finishTime = processors[p].getFinishTime();
                    } else {
                        finishTime = 0;
                    }

                    if (finishTime < earliestStart) {
                        earliestStart = finishTime;
                        processor = p;
                    }
                }

            } else {
                // get the processor for nodes with dependencies

                for (int p = 0; p < numProcessors; p++) {

                    // get the finish time
                    if (processors[p] != null) {
                        finishTime = processors[p].getFinishTime();
                    } else {
                        finishTime = 0;
                    }

                    // get the latest dependency+wait times
                    latestCommunicationTime = 0;
                    // if a dependency is in another processor, get the finish + wait time
                    for (ScheduledTask st : schedule) {
                        // go through schedule checking for dependencies in another processor
                        if (node.getDependencies().contains(st.getNode())) {
                            if (st.getProcessorNum()-1 != p) {
                                // get the finish + wait time for the dependency
                                currentCommunicationTime = st.getFinishTime() + node.getIncomingEdges().get(st.getNode());
                                if (currentCommunicationTime > latestCommunicationTime) {
                                    latestCommunicationTime = currentCommunicationTime;
                                }
                            }
                        }
                    }

                    // get the maximum of the finish time for this processor and the
                    // end+wait time for dependencies on another processor
                    finishTime = Integer.max(finishTime, latestCommunicationTime);

                    if (finishTime < earliestStart) {
                        earliestStart = finishTime;
                        processor = p;
                    }
                }

            }
            scheduledTask = new ScheduledTask(processor + 1, node, earliestStart);
            schedule.add(scheduledTask);
            processors[processor] = scheduledTask;
        }

        return schedule;
    }
}