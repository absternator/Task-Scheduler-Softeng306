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

        DefaultView view = (DefaultView) viewer.addDefaultView(false);
        view.setMinimumSize(new Dimension(340,320));

        SwingUtilities.invokeLater(() -> sn.setContent(view));
    }

    /**
     * This method converts the graph for the algorithm into a graphstream graph for visualisation
     *
     * @return the graphstream graph
     */
    public Graph convertToGraphStream() {
        Graph graph = new SingleGraph("Visualise Graph");
        graph.addAttribute("ui.stylesheet", getStyle());

        List<DAGNode> dagNodes = _graph.getNodeList();

        // Add all the nodes and ids
        // To change node colour: n.addAttribute("ui.class", "p+processorNum")
         for(DAGNode dagNode: dagNodes) {
            String id = dagNode.getId();
            if(!id.equals("end")) {
                Node n = graph.addNode(id);
                n.addAttribute("ui.label", id);
                n.addAttribute("layout.weight", 0.1);
            }
        }

        // Add all the edges and weights
        for(DAGNode dagNode: dagNodes) {
            if(!dagNode.getId().equals("end")) {
                Map<DAGNode, Integer> map = dagNode.getIncomingEdges();
                for (Map.Entry<DAGNode, Integer> entry : map.entrySet()) {
                    Edge e = graph.addEdge(entry.getKey().getId() + dagNode.getId(), entry.getKey().getId(),
                            dagNode.getId(), true);
                    e.addAttribute("ui.label", entry.getValue());
                }
            }
        }
        return graph;
    }

    // Gotta delete
    public String getStyle() {
        String s = "graph {\n" +
                "    text-size: 10px;\n" +
                "}\n" +
                "\n" +
                "node {\n" +
                "    text-style: bold;\n" +
                "    text-alignment: center;\n" +
                "    size: 25px, 25px;\n" +
                "    fill-color: lightgrey;\n" +
                "    stroke-mode: plain;\n" +
                "    stroke-color: darkgrey;\n" +
                "}\n" +
                "\n" +
                "edge {\n" +
                "    fill-color: darkgrey;\n" +
                "    text-style: bold-italic;\n" +
                "    text-alignment: along;\n" +
                "}\n" +
                "\n" +
                "/* Node Colours */\n" +
                "\n" +
                "node.p1 {\n" +
                "    fill-color: #babade;\n" +
                "    stroke-color: #8686c5;\n" +
                "}\n" +
                "\n" +
                "node.p2 {\n" +
                "    fill-color: #ccbade;\n" +
                "    stroke-color: #a686c5;\n" +
                "}\n" +
                "\n" +
                "node.p3 {\n" +
                "    fill-color: #baccde;\n" +
                "    stroke-color: #86a6c5;\n" +
                "}\n" +
                "\n" +
                "node.p4 {\n" +
                "    fill-color: #debade;\n" +
                "    stroke-color: #c586c5;\n" +
                "}\n" +
                "\n" +
                "node.p5 {\n" +
                "    fill-color: #dedeba;\n" +
                "    stroke-color: #c5c586;\n" +
                "}\n" +
                "\n" +
                "node.p6 {\n" +
                "    fill-color: #badecc;\n" +
                "    stroke-color: #86c5a6;\n" +
                "}\n" +
                "\n" +
                "node.p7 {\n" +
                "    fill-color: #deccba;\n" +
                "    stroke-color: #c5a686;\n" +
                "}\n" +
                "\n" +
                "node.p8 {\n" +
                "    fill-color: #badede;\n" +
                "    stroke-color: #86c5c5;\n" +
                "}";
        return s;
    }
}
