package team17.Algorithm;

import team17.DAG.Graph;
import team17.DAG.Node;

import java.util.ArrayList;
import java.util.List;

public class KnapsackScheduler {
    private Graph _graph;
    public KnapsackScheduler(Graph graph) {
        _graph = graph;
    }

    /**
     * Based on pseudocode by Patrick at
     * https://stackoverflow.com/questions/3009146/splitting-values-into-groups-evenly
     *
     * @return
     */
    public PartialSolution getSchedule() {
        PartialSolution solution = new PartialSolution(null, null);
        ArrayList<Node> remainingNodes = new ArrayList<>(_graph.getNodeList());

        Node endNode = remainingNodes.get(remainingNodes.size()-1);
        remainingNodes.remove(endNode);

        remainingNodes.sort((n1, n2) -> {

            int w1 = n1.getWeight();
            int w2 = n2.getWeight();

            /*For descending order*/
            return w2-w1;
        });

        double remainingWeight;
        double targetWeight;
        for (int i = AlgorithmConfig.getNumOfProcessors(); i > 0; i--) {
            remainingWeight = 0;
            for (Node node : remainingNodes) {
                remainingWeight += node.getWeight();
            }
            targetWeight = Math.ceil(remainingWeight / i);
            ArrayList<Node> nodesForThisProcessor = knapsack((int)targetWeight,remainingNodes);
            remainingNodes.removeAll(nodesForThisProcessor);
            int startTime = 0;
            for(Node node: nodesForThisProcessor){
                solution = new PartialSolution(solution, new ScheduledTask(i,node,startTime));
                startTime+=node.getWeight();
            }
        }
        solution  = new PartialSolution(solution, new ScheduledTask(1,endNode,solution.getEndTime()));

        return solution;
    }

    /**
     * Returns the nodes that add up to the maximum value that can be put in a knapsack of capacity W.
     * Adapted from https://www.geeksforgeeks.org/0-1-knapsack-problem-dp-10/
     */
    public static ArrayList<Node> knapsack(int W, List<Node> nodes) {
        int n = nodes.size();
        int[] wt = new int[n];
        int index = 0;
        for (Node node : nodes) {
            wt[index] = node.getWeight();
            index++;
        }

        int[][] K = new int[n + 1][W + 1];
        ArrayList<Node>[][] nodeTracker = new ArrayList[n + 1][W + 1];
        for (int i = 0; i < n + 1; i++) {
            for (int j = 0; j < W + 1; j++) {
                nodeTracker[i][j] = new ArrayList<>();
            }
        }

        // Build table K[][] in bottom up manner
        for (int i = 0; i <= n; i++) {
            for (int w = 0; w <= W; w++) {
                if (i == 0 || w == 0) {
                    K[i][w] = 0;
                } else if (wt[i - 1] <= w) {
                    if (wt[i - 1] + K[i - 1][w - wt[i - 1]] > K[i - 1][w]) {
                        nodeTracker[i][w].add(nodes.get(i - 1));
                        nodeTracker[i][w].addAll(nodeTracker[i - 1][w - wt[i - 1]]);
                    } else {
                        nodeTracker[i][w].addAll(nodeTracker[i - 1][w]);
                    }
                    K[i][w] = Math.max(
                            wt[i - 1] + K[i - 1][w - wt[i - 1]],
                            K[i - 1][w]);
                } else {
                    K[i][w] = K[i - 1][w];
                    nodeTracker[i][w].addAll(nodeTracker[i - 1][w]);
                }
            }
        }
        return nodeTracker[n][W];
    }
}
