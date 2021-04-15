package kpn.core.tools.route

import com.softwaremill.diffx._
import com.softwaremill.diffx.generic.auto._
import kpn.api.common.RouteLocationAnalysis
import kpn.api.common.common.TrackPath
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

    val mm = matchingMapNodes(routeInfoPair3)
    val mm2 = matchingMapTentacles(routeInfoPair3)

    val routeInfoPair4 = ignoreMapNodes(routeInfoPair3)
    if (!routeInfoPair4.isIdentical) {
      val c = compare(routeInfoPair4.oldRoute, routeInfoPair4.newRoute)
      log.info("mismatch\n" + c.show())
    }

    if (mm && mm2 && routeInfoPair4.isIdentical) {
      log.info("OK")
    }
    else {
      log.info("DIFF")
    }

    RouteAnalysisComparison(factsDiff)
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
        summary = routeInfoPair.newRoute.summary.copy(country = None),
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

  private def matchingMapNodes(routeInfoPair: RouteInfoPair): Boolean = {

    val oldMapNodes = toRouteMapNodes(routeInfoPair.oldRoute)
    val newMapNodes = toRouteMapNodes(routeInfoPair.newRoute)

    if (oldMapNodes == newMapNodes) {
      true
    }
    else {
      val c = compare(oldMapNodes, newMapNodes)
      log.info("Mismatch in route map nodes\n" + c.show())
      false
    }
  }

  private def matchingMapTentacles(routeInfoPair: RouteInfoPair): Boolean = {
    matchingMapStartTentacles(routeInfoPair) && matchingMapEndTentacles(routeInfoPair)
  }

  private def matchingMapStartTentacles(routeInfoPair: RouteInfoPair): Boolean = {
    val oldPaths = routeInfoPair.oldRoute.analysis.map.startTentaclePaths
    val newPaths = routeInfoPair.newRoute.analysis.map.startTentaclePaths
    matchingPaths("start tentacles", oldPaths, newPaths)
  }

  private def matchingMapEndTentacles(routeInfoPair: RouteInfoPair): Boolean = {
    val oldPaths = routeInfoPair.oldRoute.analysis.map.endTentaclePaths
    val newPaths = routeInfoPair.newRoute.analysis.map.endTentaclePaths
    matchingPaths("end tentacles", oldPaths, newPaths)
  }

  private def matchingPaths(message: String, oldPaths: Seq[TrackPath], newPaths: Seq[TrackPath]): Boolean = {

    val oldPaths1 = oldPaths.map(_.copy(pathId = 0))
    val newPaths1 = newPaths.map(_.copy(pathId = 0))
    val newPathsReversed = newPaths1.map(_.reverse)

    val isMatch = if (oldPaths1.size == newPaths1.size) {
      oldPaths1.forall { oldPath =>
        newPaths1.exists(newPath => oldPath.equals(newPath)) ||
          newPathsReversed.exists(newPath => oldPath.equals(newPath))
      }
    }
    else {
      log.info(s"Mismatch in $message paths (number of paths is different)")
      false
    }

    if (isMatch) {
      true
    }
    else {
      val c = compare(oldPaths, newPaths)
      log.info(s"Mismatch in $message paths\n${c.show()}")
      false
    }
  }

  private def toRouteMapNodes(routeInfo: RouteInfo): RouteMapNodes = {
    val routeMap = routeInfo.analysis.map
    val startNodes = (routeMap.startNodes ++ routeMap.startTentacleNodes).sortBy(_.id)
    val endNodes = (routeMap.endNodes ++ routeMap.endTentacleNodes).sortBy(_.id)
    val redundantNodes = routeMap.redundantNodes.sortBy(_.id)
    RouteMapNodes(
      startNodes,
      endNodes,
      redundantNodes
    )
  }

  private def ignoreMapNodes(routeInfoPair: RouteInfoPair): RouteInfoPair = {
    routeInfoPair.copy(
      oldRoute = withoutMapNodes(routeInfoPair.oldRoute),
      newRoute = withoutMapNodes(routeInfoPair.newRoute)
    )
  }

  private def withoutMapNodes(routeInfo: RouteInfo): RouteInfo = {
    val map = routeInfo.analysis.map
    routeInfo.copy(
      analysis = routeInfo.analysis.copy(
        map = map.copy(
          startNodes = Seq.empty,
          endNodes = Seq.empty,
          startTentacleNodes = Seq.empty,
          endTentacleNodes = Seq.empty,
          redundantNodes = Seq.empty,
          startTentaclePaths = Seq.empty,
          endTentaclePaths = Seq.empty
        )
      )
    )
  }

}
