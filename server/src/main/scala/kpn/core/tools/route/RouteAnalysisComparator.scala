package kpn.core.tools.route

import com.softwaremill.diffx._
import com.softwaremill.diffx.generic.auto._
import kpn.api.common.RouteLocationAnalysis
import kpn.api.common.route.RouteInfo
import kpn.api.common.route.RouteNetworkNodeInfo
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.analyzers.NodeNameAnalyzer

class RouteAnalysisComparator {

  private val log = Log(classOf[RouteAnalysisTool])

  def compareRouteInfos(oldRoute: RouteInfo, newRoute: RouteInfo): RouteAnalysisComparison = {
    val factsDiff = new RouteFactsComparator().compare(oldRoute, newRoute)

    val routeInfoPair = RouteInfoPair(oldRoute, newRoute)
    val routeInfoPair1 = ignoreLocation(routeInfoPair)
    val routeInfoPair2 = ignoreTrackPathsAndStructureStrings(routeInfoPair1)
    val routeInfoPair3 = ignoreNormalization(routeInfoPair2)

    if (!routeInfoPair3.isIdentical) {
      printDiffs(routeInfoPair3)
      log.info("DIFF")
    }
    else {
      log.info("OK")
    }

    RouteAnalysisComparison(factsDiff)
  }

  private def printDiffs(routeInfoPair: RouteInfoPair): Unit = {
    val c = compare(routeInfoPair.oldRoute, routeInfoPair.newRoute)
    val diff = c.show()
    log.info("\n" + diff)
  }

  private def ignoreLocation(routeInfoPair: RouteInfoPair): RouteInfoPair = {
    routeInfoPair.copy(
      oldRoute = routeInfoPair.oldRoute.copy(
        summary = routeInfoPair.oldRoute.summary.copy(country = None),
        analysis = routeInfoPair.oldRoute.analysis.copy(
          locationAnalysis = RouteLocationAnalysis(None, Seq.empty, Seq.empty)
        )
      ),
      newRoute = routeInfoPair.newRoute.copy(
        summary = routeInfoPair.oldRoute.summary.copy(country = None),
        analysis = routeInfoPair.newRoute.analysis.copy(
          locationAnalysis = RouteLocationAnalysis(None, Seq.empty, Seq.empty)
        )
      )
    )
  }

  private def ignoreTrackPathsAndStructureStrings(routeInfoPair: RouteInfoPair): RouteInfoPair = {
    routeInfoPair.copy(
      newRoute = routeInfoPair.newRoute.copy(
        analysis = routeInfoPair.newRoute.analysis.copy(
          structureStrings = Seq.empty,
          map = routeInfoPair.newRoute.analysis.map.copy(
            trackPaths = Seq.empty
          )
        )
      )
    )
  }

  private def ignoreNormalization(routeInfoPair: RouteInfoPair): RouteInfoPair = {
    routeInfoPair.copy(
      oldRoute = {
        val route = routeInfoPair.oldRoute
        val analysis = route.analysis
        val map = analysis.map
        route.copy(
          summary = route.summary.copy(
            name = normalizeRouteName(route.summary.name),
            nodeNames = route.summary.nodeNames.map(normalizeNodeName)
          ),
          analysis = analysis.copy(
            members = analysis.members.map { routeMemberInfo =>
              routeMemberInfo.copy(
                nodes = normalizeNodes(routeMemberInfo.nodes)
              )
            },
            structureStrings = Seq.empty,
            map = map.copy(
              startNodes = normalizeNodes(map.startNodes),
              endNodes = normalizeNodes(map.endNodes),
              startTentacleNodes = normalizeNodes(map.startTentacleNodes),
              endTentacleNodes = normalizeNodes(map.endTentacleNodes),
              redundantNodes = normalizeNodes(map.redundantNodes)
            ),
            expectedName = normalizeRouteName(analysis.expectedName)
          )
        )
      },
      newRoute = {
        val route = routeInfoPair.newRoute
        val analysis = route.analysis
        val map = analysis.map
        route.copy(
          analysis = analysis.copy(
            members = analysis.members.map { routeMemberInfo =>
              routeMemberInfo.copy(
                nodes = withoutAlternateNames(routeMemberInfo.nodes)
              )
            },
            map = map.copy(
              startNodes = withoutAlternateNames(map.startNodes),
              endNodes = withoutAlternateNames(map.endNodes),
              startTentacleNodes = withoutAlternateNames(map.startTentacleNodes),
              endTentacleNodes = withoutAlternateNames(map.endTentacleNodes),
              redundantNodes = withoutAlternateNames(map.redundantNodes)
            )
          )
        )
      }
    )
  }

  private def normalizeRouteName(routeName: String): String = {
    val parts = routeName.split("-")
    parts.map(normalizeNodeName).mkString("-")
  }

  private def normalizeNodeName(nodeName: String): String = {
    NodeNameAnalyzer.normalize(nodeName)
  }

  private def normalizeNodes(nodes: Seq[RouteNetworkNodeInfo]): Seq[RouteNetworkNodeInfo] = {
    nodes.map(node =>
      node.copy(
        name = normalizeNodeName(node.name),
        alternateName = "removed"
      )
    )
  }

  private def withoutAlternateNames(nodes: Seq[RouteNetworkNodeInfo]): Seq[RouteNetworkNodeInfo] = {
    nodes.map(node =>
      node.copy(
        alternateName = "removed"
      )
    )
  }

}
