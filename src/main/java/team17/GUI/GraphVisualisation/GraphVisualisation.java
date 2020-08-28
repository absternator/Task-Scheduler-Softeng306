package team17.GUI.GraphVisualisation;

import javafx.embed.swing.SwingNode;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swingViewer.DefaultView;
import org.graphstream.ui.view.Viewer;

import javax.swing.*;
import java.awt.*;

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

    public Graph convertToGraphStream() {
        Graph graph = new SingleGraph("Visualise Graph");
        graph.addAttribute("ui.stylesheet", "url(file:src/main/resources/team17/GUI/graph.css)");


        for(int i = 0; i<5; i++){
            Node n = graph.addNode(""+ i);
            n.addAttribute("ui.label", "" + i);
        }

        Edge e = graph.addEdge("01", "0", "1", true);
        e.addAttribute("ui.label", "weight=2");
        return graph;
    }

}
