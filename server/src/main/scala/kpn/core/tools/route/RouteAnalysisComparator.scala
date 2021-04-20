package kpn.core.tools.route

import com.softwaremill.diffx._
import com.softwaremill.diffx.generic.auto._
import kpn.api.common.common.TrackPath
import kpn.api.common.route.RouteInfo
import kpn.api.common.route.RouteNetworkNodeInfo
import kpn.api.custom.Fact.RouteBroken
import kpn.api.custom.Fact.RouteIncomplete
import kpn.api.custom.Fact.RouteInvalidSortingOrder
import kpn.api.custom.Fact.RouteNameMissing
import kpn.api.custom.Fact.RouteNodeMissingInWays
import kpn.api.custom.Fact.RouteNodeNameMismatch
import kpn.api.custom.Fact.RouteNotBackward
import kpn.api.custom.Fact.RouteNotForward
import kpn.api.custom.Tags
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.analyzers.NodeNameAnalyzer

class RouteAnalysisComparator {

  private val log = Log(classOf[RouteAnalysisTool])

  def compareRouteInfos(oldRoute: RouteInfo, newRoute: RouteInfo): Unit = {

    var routeInfoPair = RouteInfoPair(oldRoute, newRoute)
    routeInfoPair = ignoreSummaryNodeNames(routeInfoPair)
    routeInfoPair = ignoreTrackPathsAndStructureStrings(routeInfoPair)
    routeInfoPair = ignoreNormalization(routeInfoPair)
    routeInfoPair = ignoreTrackPathIds(routeInfoPair)

    val mm = matchingMapNodes(routeInfoPair)
    val mm3 = matchingPaths(routeInfoPair)
    routeInfoPair = ignoreFreeRoutePaths(routeInfoPair)
    val mm2 = matchingMapTentacles(routeInfoPair)

    routeInfoPair = ignoreMapNodes(routeInfoPair)
    routeInfoPair = normalizeRouteTags(routeInfoPair)

    val mm4 = matchingFacts(routeInfoPair)
    routeInfoPair = ignoreFacts(routeInfoPair)

    if (!routeInfoPair.isIdentical) {
      val c = compare(routeInfoPair.oldRoute, routeInfoPair.newRoute)
      log.info("DIFF mismatch\n" + c.show())
    }

    if (mm && mm2 && mm3 && mm4 && routeInfoPair.isIdentical) {
      log.info("OK")
    }
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
              freeNodes = normalizeNodes(map.freeNodes),
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
              freeNodes = withoutAlternateNames(map.freeNodes),
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

  private def ignoreSummaryNodeNames(routeInfoPair: RouteInfoPair): RouteInfoPair = {
    routeInfoPair.copy(
      oldRoute = {
        routeInfoPair.oldRoute.copy(
          summary = routeInfoPair.oldRoute.summary.copy(
            nodeNames = Seq.empty
          )
        )
      },
      newRoute = {
        routeInfoPair.newRoute.copy(
          summary = routeInfoPair.newRoute.summary.copy(
            nodeNames = Seq.empty
          )
        )
      }
    )
  }

  private def ignoreTrackPathIds(routeInfoPair: RouteInfoPair): RouteInfoPair = {
    routeInfoPair.copy(
      oldRoute = withoutTrackPathIds(routeInfoPair.oldRoute),
      newRoute = withoutTrackPathIds(routeInfoPair.newRoute)
    )
  }

  private def withoutTrackPathIds(routeInfo: RouteInfo): RouteInfo = {
    val analysis = routeInfo.analysis
    val map = analysis.map
    routeInfo.copy(
      analysis = analysis.copy(
        map = map.copy(
          freePaths = map.freePaths.map(_.copy(pathId = 0)),
          forwardPath = map.forwardPath.map(_.copy(pathId = 0)),
          backwardPath = map.backwardPath.map(_.copy(pathId = 0)),
          startTentaclePaths = map.startTentaclePaths.map(_.copy(pathId = 0)),
          endTentaclePaths = map.endTentaclePaths.map(_.copy(pathId = 0))
        )
      )
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
      if (newMapNodes.isFreeRoute) {
        val newNodes = newMapNodes.freeNodes.sortBy(_.id)
        val oldNodes = (oldMapNodes.startNodes ++ oldMapNodes.endNodes).sortBy(_.id).distinct
        if (oldNodes == newNodes) {
          if (oldMapNodes.redundantNodes == newMapNodes.redundantNodes) {
            true
          }
          else {
            val c = compare(oldMapNodes, newMapNodes)
            log.info("DIFF Mismatch in route map redundant nodes\n" + c.show())
            false
          }
        }
        else {
          val c = compare(oldMapNodes, newMapNodes)
          log.info("DIFF Mismatch in route map free nodes\n" + c.show())
          false
        }
      }
      else {
        val c = compare(oldMapNodes, newMapNodes)
        log.info("DIFF Mismatch in route map nodes\n" + c.show())
        false
      }
    }
  }

  private def matchingPaths(routeInfoPair: RouteInfoPair): Boolean = {

    if (isFreeRoute(routeInfoPair.newRoute)) {
      val oldTrackPathNodes = trackPathNodess(routeInfoPair.oldRoute)
      val newTrackPathNodes = trackPathNodess(routeInfoPair.oldRoute)
      if (oldTrackPathNodes == newTrackPathNodes) {
        true
      }
      else {
        val c = compare(oldTrackPathNodes, newTrackPathNodes)
        log.info("DIFF mismatch paths\n" + c.show())
        false
      }
    }
    else {
      true
    }
  }

  private def trackPathNodess(routeInfo: RouteInfo): Seq[TrackPathNodes] = {
    val map = routeInfo.analysis.map

    val paths = map.freePaths ++
      map.forwardPath.toSeq ++
      map.backwardPath.toSeq ++
      map.startTentaclePaths ++
      map.endTentaclePaths

    paths.map(path => TrackPathNodes(path.startNodeId, path.endNodeId)).sortBy(nodes => s"${nodes.startNodeId}-${nodes.startNodeId}")
  }


  private def isFreeRoute(routeInfo: RouteInfo): Boolean = {
    routeInfo.analysis.map.freeNodes.nonEmpty &&
      routeInfo.analysis.map.startNodes.isEmpty &&
      routeInfo.analysis.map.endNodes.isEmpty &&
      routeInfo.analysis.map.startTentacleNodes.isEmpty &&
      routeInfo.analysis.map.endTentacleNodes.isEmpty
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
      log.info(s"DIFF Mismatch in $message paths (number of paths is different)")
      false
    }

    if (isMatch) {
      true
    }
    else {
      val c = compare(oldPaths, newPaths)
      log.info(s"DIFF Mismatch in $message paths\n${c.show()}")
      false
    }
  }

  private def toRouteMapNodes(routeInfo: RouteInfo): RouteMapNodes = {
    val routeMap = routeInfo.analysis.map
    val startNodes = (routeMap.startNodes ++ routeMap.startTentacleNodes).sortBy(_.id)
    val endNodes = (routeMap.endNodes ++ routeMap.endTentacleNodes).sortBy(_.id)
    val redundantNodes = routeMap.redundantNodes.sortBy(_.id)
    RouteMapNodes(
      routeMap.freeNodes,
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
          freeNodes = Seq.empty,
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

  private def ignoreFreeRoutePaths(routeInfoPair: RouteInfoPair): RouteInfoPair = {
    if (isFreeRoute(routeInfoPair.newRoute)) {
      routeInfoPair.copy(
        oldRoute = withoutFreeRoutePaths(routeInfoPair.oldRoute),
        newRoute = withoutFreeRoutePaths(routeInfoPair.newRoute)
      )
    }
    else {
      routeInfoPair
    }
  }

  private def withoutFreeRoutePaths(routeInfo: RouteInfo): RouteInfo = {
    val map = routeInfo.analysis.map
    routeInfo.copy(
      analysis = routeInfo.analysis.copy(
        map = map.copy(
          freePaths = Seq.empty,
          forwardPath = None,
          backwardPath = None,
          startTentaclePaths = Seq.empty,
          endTentaclePaths = Seq.empty
        )
      )
    )
  }

  private def normalizeRouteTags(routeInfoPair: RouteInfoPair): RouteInfoPair = {
    routeInfoPair.copy(
      oldRoute = normalizeRouteInfoTags(routeInfoPair.oldRoute),
      newRoute = normalizeRouteInfoTags(routeInfoPair.newRoute)
    )
  }

  private def normalizeRouteInfoTags(routeInfo: RouteInfo): RouteInfo = {
    routeInfo.copy(
      summary = routeInfo.summary.copy(
        tags = normalizeTags(routeInfo.summary.tags)
      ),
      tags = normalizeTags(routeInfo.tags)
    )
  }

  private def normalizeTags(tags: Tags): Tags = {
    Tags(tags.tags.sortBy(_.key))
  }

  private def matchingFacts(routeInfoPair: RouteInfoPair): Boolean = {
    val obsoleteFacts = Seq(RouteInvalidSortingOrder)

    val oldFacts = routeInfoPair.oldRoute.facts.filterNot(obsoleteFacts.contains).sortBy(_.name)
    val newFacts = routeInfoPair.newRoute.facts.sortBy(_.name)

    if (oldFacts.equals(Seq(RouteBroken, RouteNodeMissingInWays, RouteNotBackward, RouteNotForward)) &&
      newFacts.equals(Seq(RouteBroken, RouteNodeMissingInWays))
    ) {
      true
    }
    else if (oldFacts.equals(Seq(RouteBroken, RouteIncomplete, RouteNotBackward, RouteNotForward)) &&
      newFacts.equals(Seq(RouteIncomplete))
    ) {
      true
    }
    else if (oldFacts.equals(Seq(RouteBroken, RouteNameMissing)) && newFacts.isEmpty) {
      true
    }
    else if (oldFacts.equals(Seq(RouteNodeNameMismatch)) && newFacts.isEmpty) {
      true
    }
    else {
      if (oldFacts.equals(newFacts)) {
        true
      }
      else {
        log.info(s"DIFF Mismatch in facts $oldFacts <> $newFacts")
        false
      }
    }
  }

  private def ignoreFacts(routeInfoPair: RouteInfoPair): RouteInfoPair = {
    routeInfoPair.copy(
      oldRoute = routeInfoPair.oldRoute.copy(
        facts = Seq.empty
      ),
      newRoute = routeInfoPair.oldRoute.copy(
        facts = Seq.empty
      )
    )
  }
}
