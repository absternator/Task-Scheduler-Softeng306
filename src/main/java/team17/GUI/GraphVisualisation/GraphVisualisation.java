package team17.GUI.GraphVisualisation;

import javafx.embed.swing.SwingNode;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swingViewer.DefaultView;
import org.graphstream.ui.view.Viewer;
import team17.DAG.DAGGraph;
import team17.DAG.DAGNode;

import javax.swing.*;
import java.awt.Dimension;
import java.util.List;
import java.util.Map;

/**
 * This class visualises the graph using graphstream and embeds it into SwingNode
 */
public class GraphVisualisation {
    DAGGraph _graph;

    public GraphVisualisation(DAGGraph graph) {
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

//        DefaultView view = new DefaultView(viewer, Viewer.DEFAULT_VIEW_ID, Viewer.newGraphRenderer());
        DefaultView view = (DefaultView) viewer.addDefaultView(false);
        view.setMinimumSize(new Dimension(600,400));

        SwingUtilities.invokeLater(() -> sn.setContent(view));
    }

    /**
     * This method converts the graph for the algorithm into a graphstream graph for visualisation
     *
     * @return the graphstream graph
     */
    public Graph convertToGraphStream() {
        Graph graph = new SingleGraph("Visualise Graph");
        graph.addAttribute("ui.stylesheet", "url(file:src/main/resources/team17/GUI/graph.css)");

        List<DAGNode> dagNodes = _graph.getNodeList();

        // Add all the nodes and ids
        // To change node colour: n.addAttribute("ui.class", "p+processorNum")
         for(DAGNode dagNode: dagNodes) {
            String id = dagNode.getId();
            if(!id.equals("end")) {
                Node n = graph.addNode(id);
                n.addAttribute("ui.label", id);
            }
        }

        // Add all the edges and weights
        for(DAGNode dagNode: dagNodes) {
            if(!dagNode.getId().equals("end")) {
                Map<DAGNode, Integer> map = dagNode.getIncomingEdges();
                for (Map.Entry<DAGNode, Integer> entry : map.entrySet()) {
                    Edge e = graph.addEdge(entry.getKey().getId() + dagNode.getId(), entry.getKey().getId(),
                            dagNode.getId(), true);
                    e.addAttribute("ui.label", "Weight=" + entry.getValue());
                }
            }
        }
        return graph;
    }
}
