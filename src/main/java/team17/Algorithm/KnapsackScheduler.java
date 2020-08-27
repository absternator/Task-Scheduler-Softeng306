package team17.Algorithm;

import team17.DAG.Graph;
import team17.DAG.Node;

import java.util.List;

public class KnapsackScheduler {
    private Graph _graph;
    private List<Node> _nodes;

    public KnapsackScheduler(Graph graph) {
        _graph = graph;
        _nodes = graph.getNodeList();
    }

    public PartialSolution getSchedule() {
        return null;
    }

    public List<Node> knapsackGetNodes(List<Node> nodes, int max) {
        return null;
    }

    /**
     * Returns the maximum value that can be put in a knapsack of capacity W.
     * Modified from https://www.geeksforgeeks.org/0-1-knapsack-problem-dp-10/
     */
    public static int knapSack(int W, int[] wt, int n) {
        int i, w;
        int[][] K = new int[n + 1][W + 1];

        // Build table K[][] in bottom up manner
        for (i = 0; i <= n; i++) {
            for (w = 0; w <= W; w++) {
                if (i == 0 || w == 0) {
                    K[i][w] = 0;
                } else if (wt[i - 1] <= w) {
                    K[i][w] = Math.max(
                            wt[i - 1] + K[i - 1][w - wt[i - 1]],
                            K[i - 1][w]);
                } else {
                    K[i][w] = K[i - 1][w];
                }
            }
        }

        return K[n][W];
    }
}
