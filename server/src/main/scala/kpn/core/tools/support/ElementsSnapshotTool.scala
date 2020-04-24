package kpn.core.tools.support

import java.io.PrintWriter

import kpn.api.custom.ScopedNetworkType
import kpn.core.overpass.OverpassQueryExecutorImpl
import kpn.core.util.Log
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
    val overpassQueryExecutor = new OverpassQueryExecutorImpl()
    val networkIdsLoader = new NetworkIdsLoaderImpl(overpassQueryExecutor)
    val nodeIdsLoader = new NodeIdsLoaderImpl(overpassQueryExecutor)
    val routeIdsLoader = new RouteIdsLoaderImpl(overpassQueryExecutor)
    new ElementsSnapshotTool(networkIdsLoader, nodeIdsLoader, routeIdsLoader).run()
  }
}

class ElementsSnapshotTool(
  networkIdsLoader: NetworkIdsLoader,
  nodeIdsLoader: NodeIdsLoader,
  routeIdsLoader: RouteIdsLoader
) {

  private val log = Log(classOf[ElementsSnapshotTool])

  def run(): Unit = {
    val elements: Elements = readElements()
    writeElements(elements)
    log.info("done")
  }

  private def readElements(): Elements = {
    val nodeIds = readNodeIds
    val routeIds = readRouteIds
    val networkIds = readNetworkIds
    val elements = Elements(nodeIds.toSet, routeIds.toSet, networkIds.toSet)
    elements
  }

  private def readNodeIds: Seq[Long] = {
    ScopedNetworkType.all.flatMap { scopedNetworkType =>
      nodeIdsLoader.load(AnalysisContext.networkTypeTaggingStart, scopedNetworkType)
    }
  }

  private def readRouteIds: Seq[Long] = {
    ScopedNetworkType.all.flatMap { scopedNetworkType =>
      routeIdsLoader.load(AnalysisContext.networkTypeTaggingStart, scopedNetworkType)
    }
  }

  private def readNetworkIds: Seq[Long] = {
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
