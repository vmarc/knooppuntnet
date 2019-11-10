package kpn.core.tools.support

import java.io.PrintWriter

import kpn.api.common.ScopedNetworkType
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.OverpassQueryExecutorImpl
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.context.Elements
import kpn.server.analyzer.load.NetworkIdsLoader
import kpn.server.analyzer.load.NetworkIdsLoaderImpl
import kpn.server.analyzer.load.orphan.node.NodeIdsLoader
import kpn.server.analyzer.load.orphan.node.NodeIdsLoaderImpl
import kpn.server.analyzer.load.orphan.route.RouteIdsLoader
import kpn.server.analyzer.load.orphan.route.RouteIdsLoaderImpl
import kpn.server.json.Json

/*
  Produces a snapshot with the id's of all nodes, routes and networks that
  were known on Nov 1st 2019 (the starting point for analysis based completely
  on "network:type" tag).
*/
object ElementsSnapshotTool {
  def main(args: Array[String]): Unit = {
    val executor = new OverpassQueryExecutorImpl()
    val networkIdsLoader = new NetworkIdsLoaderImpl(executor)
    val nodeIdsLoader = new NodeIdsLoaderImpl(executor: OverpassQueryExecutor)
    val routeIdsLoader = new RouteIdsLoaderImpl(executor)
    new ElementsSnapshotTool(networkIdsLoader, nodeIdsLoader, routeIdsLoader).run()
  }
}

class ElementsSnapshotTool(
  networkIdsLoader: NetworkIdsLoader,
  nodeIdsLoader: NodeIdsLoader,
  routeIdsLoader: RouteIdsLoader
) {

  def run(): Unit = {
    val elements: Elements = readElements()
    writeElements(elements)
  }

  private def readElements(): Elements = {
    val networkIds: Seq[Long] = readNetworkIds
    val routeIds: Seq[Long] = readRouteIds
    val nodeIds: _root_.scala.collection.Seq[Long] = readNodeIds
    val elements = Elements(networkIds.toSet, routeIds.toSet, nodeIds.toSet)
    elements
  }

  private def readNodeIds = {
    ScopedNetworkType.all.flatMap { scopedNetworkType =>
      nodeIdsLoader.load(AnalysisContext.networkTypeTaggingStart, scopedNetworkType)
    }
  }

  private def readRouteIds = {
    ScopedNetworkType.all.flatMap { scopedNetworkType =>
      routeIdsLoader.load(AnalysisContext.networkTypeTaggingStart, scopedNetworkType)
    }
  }

  private def readNetworkIds = {
    ScopedNetworkType.all.flatMap { scopedNetworkType =>
      networkIdsLoader.load(AnalysisContext.networkTypeTaggingStart, scopedNetworkType)
    }
  }

  private def writeElements(elements: Elements): Unit = {
    val pw = new PrintWriter(AnalysisContext.networkTypeTaggingStartSnapshotFilename)
    try {
      Json.objectMapper.writerWithDefaultPrettyPrinter().writeValue(pw, elements)
    }
    finally {
      pw.close()
    }
  }
}
