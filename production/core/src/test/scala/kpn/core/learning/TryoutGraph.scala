package kpn.core.learning

import kpn.core.data.DataBuilder
import kpn.core.engine.analysis.NetworkAnalyzerImpl
import kpn.core.engine.analysis.NetworkRelationAnalyzerImpl
import kpn.core.engine.analysis.country.CountryAnalyzerImpl
import kpn.core.engine.analysis.route.MasterRouteAnalyzerImpl
import kpn.core.engine.analysis.route.analyzers.AccessibilityAnalyzerImpl
import kpn.core.loadOld.OsmDataXmlReader
import kpn.shared.NetworkType
import scalax.collection.Graph
import scalax.collection.GraphPredef.EdgeAssoc
import scalax.collection.edge.Implicits.edge2WUnDiEdgeAssoc
import scalax.collection.edge.WUnDiEdge

object TryoutGraph {
  def main(args: Array[String]): Unit = {
    new TryoutGraph().run()
  }
}

class TryoutGraph {
  def run(): Unit = {

    println("load network")
    val countryAnalyzer = new CountryAnalyzerImpl()
    val routeAnalyzer = new MasterRouteAnalyzerImpl(new AccessibilityAnalyzerImpl())
    val networkRelationAnalyzer = new NetworkRelationAnalyzerImpl(countryAnalyzer)
    val networkAnalyzer = new NetworkAnalyzerImpl(countryAnalyzer, routeAnalyzer)

    val rawData = OsmDataXmlReader.read("/home/marcv/wrk/gps/data/2014-07-06/nl/rwn/networks/3138543.xml") // Brabantse Wal
    val networkData = new DataBuilder(NetworkType.hiking, rawData).data
    val networkRelationAnalysis = networkRelationAnalyzer.analyze(networkData.relations(3138543))
    val network = networkAnalyzer.analyze(networkRelationAnalysis, networkData, 3138543)

    // make list of nodes + list of edges
    val nodeMap = network.nodes.map(_.networkNode).map(n => (n.id, n.name)).toMap
    val nodes = nodeMap.keys.toSeq
    val edges = network.routes.flatMap { networkMemberRoute =>

      // TODO should be using forwardSegments and backwardSegments here (perhaps make bi-directional if they are the same)
      // derive meters from the segments (make sure to take into account ways that are only used partially)
      val rn = networkMemberRoute.routeAnalysis.routeNodes
      if (rn.startNodes.isEmpty || rn.endNodes.isEmpty) {
        None
      }
      else {
        Some(rn.startNodes.head.id ~ rn.endNodes.head.id % networkMemberRoute.routeAnalysis.route.summary.meters.toLong)
      }
    }

    // drop network details from memory to be able to monitor memory usage?

    // timed: create graph

    println("create graph")
    val graph = Graph.from(nodes, edges)

    // timed: find shortest route between given nodes + print

    println("shortest path")
    shortestPath(nodeMap, graph, 9999, 1234)
    shortestPath(nodeMap, graph, 42547866, 8888)
    shortestPath(nodeMap, graph, 42547866, 1234) // 5 to 24
    shortestPath(nodeMap, graph, 813424276, 813424247) // 15 to 19
    shortestPath(nodeMap, graph, 813424276, 42547866) // 15 to 5
    shortestPath(nodeMap, graph, 813424276, 42544227) // 15 to 3

    shortestPath(nodeMap, graph, 813424276, 42544283) // 15 to 37
    shortestPath(nodeMap, graph, 813424276, 587752628) // 15 to 1
  }

  private def shortestPath(nodeMap: Map[Long, String], graph: Graph[Long, WUnDiEdge], from: Long, to: Long): Unit = {
    graph.find(from) match {
      case None => println("from node not defined: " + from)
      case Some(fromNode) =>
        graph.find(to) match {
          case None => println("to node not defined: " + to)
          case Some(toNode) =>
            fromNode.shortestPathTo(toNode) match {
              case None => printf("no path between %s and %s\n", from, to)
              case Some(path) =>
                printf("from=%s, to=%s, path=%s\n", from, to, path.nodes.map(id => nodeMap(id)).mkString(" - "))
                path.edges.foreach { edge =>
                  printf("  %s %% %s\n", edge.nodes.map(nodeMap(_)).mkString("-"), edge.weight)
                }
            }
        }
    }
  }
}
