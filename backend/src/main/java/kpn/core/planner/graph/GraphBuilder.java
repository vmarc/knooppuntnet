package kpn.core.planner.graph;

import org.jgrapht.Graph;
import org.jgrapht.graph.AsWeightedGraph;

import java.util.function.Function;

public class GraphBuilder {
    public static AsWeightedGraph<String, String> weigthedGraph(Graph<String, String> graph, Function<String, Double> weightFunction) {
        return new AsWeightedGraph<>(graph, weightFunction, false, false);
    }
}
