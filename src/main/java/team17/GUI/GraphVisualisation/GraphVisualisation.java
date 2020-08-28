package team17.GUI.GraphVisualisation;

import javafx.embed.swing.SwingNode;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swingViewer.DefaultView;
import org.graphstream.ui.view.Viewer;

import javax.swing.*;
import java.awt.Dimension;
import java.util.List;
import java.util.Map;

public class GraphVisualisation {
    team17.DAG.Graph _graph;

    public GraphVisualisation(team17.DAG.Graph graph) {
        // Set default graph renderer
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        _graph = graph;
    }

    /**
     * Embeds the swing component graphstream graph into javafx using SwingNode
     *
     * @param sn The SwingNode to embed the graph view in
     */
    public void createSwingGraph(SwingNode sn) {
        Graph graph = convertToGraphStream();

        Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        viewer.enableAutoLayout();

        DefaultView view = new DefaultView(viewer, Viewer.DEFAULT_VIEW_ID, Viewer.newGraphRenderer());
        view.setMinimumSize(new Dimension(600,400));

        SwingUtilities.invokeLater(() -> {
            sn.setContent((JPanel) view);
        });
    }

    /**
     * This method converts the graph for the algorithm into a graphstream graph for visualisation
     *
     * @return the graphstream graph
     */
    public Graph convertToGraphStream() {
        Graph graph = new SingleGraph("Visualise Graph");
        graph.addAttribute("ui.stylesheet", "url(file:src/main/resources/team17/GUI/graph.css)");

        List<team17.DAG.Node> dagNodes = _graph.getNodeList();

        // Add all the nodes and ids
        for(team17.DAG.Node dagNode: dagNodes) {
            String id = dagNode.getId();
            if(!id.equals("end")) {
                Node n = graph.addNode(id);
                n.addAttribute("ui.label", id);
            }
        }

        // Add all the edges and weights
        for(team17.DAG.Node dagNode: dagNodes) {
            if(!dagNode.getId().equals("end")) {
                Map<team17.DAG.Node, Integer> map = dagNode.getIncomingEdges();
                for (Map.Entry<team17.DAG.Node, Integer> entry : map.entrySet()) {
                    Edge e = graph.addEdge(entry.getKey().getId() + dagNode.getId(), entry.getKey().getId(),
                            dagNode.getId(), true);
                    e.addAttribute("ui.label", "Weight=" + entry.getValue());
                }
            }
        }

        return graph;
    }

}
