package kpn.core.tools.route

import com.softwaremill.diffx._
import com.softwaremill.diffx.generic.auto._
import kpn.api.common.common.TrackPath
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
import kpn.core.doc.RouteDoc
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.node.NodeUtil

class RouteAnalysisComparator {

  private val log = Log(classOf[RouteAnalysisComparator])

  def compareRouteInfos(oldRoute: RouteDoc, newRoute: RouteDoc): Unit = {

    var routeDocPair = RouteDocPair(oldRoute, newRoute)
    routeDocPair = ignoreSummaryNodeNames(routeDocPair)
    routeDocPair = ignoreStructureStrings(routeDocPair)
    routeDocPair = ignoreNormalization(routeDocPair)
    routeDocPair = ignoreTrackPathIds(routeDocPair)

    val ok1 = matchingMapNodes(routeDocPair)
    val ok2 = matchingPaths(routeDocPair)
    routeDocPair = ignoreFreeRoutePaths(routeDocPair)
    val ok3 = matchingMapTentacles(routeDocPair)

    routeDocPair = ignoreMapNodes(routeDocPair)
    routeDocPair = normalizeRouteTags(routeDocPair)

    val ok4 = matchingFacts(routeDocPair)
    routeDocPair = ignoreFacts(routeDocPair)

    if (!routeDocPair.isIdentical) {
      val c = compare(routeDocPair.oldRoute, routeDocPair.newRoute)
      log.info("DIFF mismatch\n" + c.show())
    }

    if (ok1 && ok2 && ok3 && ok4 && routeDocPair.isIdentical) {
      log.info("OK")
    }
  }

  private def ignoreStructureStrings(routeDocPair: RouteDocPair): RouteDocPair = {
    routeDocPair.copy(
      newRoute = routeDocPair.newRoute.copy(
        analysis = routeDocPair.newRoute.analysis.copy(
          structureStrings = Seq.empty
        )
      )
    )
  }

  private def ignoreNormalization(routeDocPair: RouteDocPair): RouteDocPair = {
    routeDocPair.copy(
      oldRoute = {
        val route = routeDocPair.oldRoute
        val analysis = route.analysis
        val map = analysis.map
        route.copy(
          summary = route.summary.copy(
            name = normalizeRouteName(route.summary.name),
            nodeNames = route.summary.nodeNames.map(NodeUtil.normalize)
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
        val route = routeDocPair.newRoute
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

  private def ignoreSummaryNodeNames(routeDocPair: RouteDocPair): RouteDocPair = {
    routeDocPair.copy(
      oldRoute = {
        routeDocPair.oldRoute.copy(
          summary = routeDocPair.oldRoute.summary.copy(
            nodeNames = Seq.empty
          )
        )
      },
      newRoute = {
        routeDocPair.newRoute.copy(
          summary = routeDocPair.newRoute.summary.copy(
            nodeNames = Seq.empty
          )
        )
      }
    )
  }

  private def ignoreTrackPathIds(routeDocPair: RouteDocPair): RouteDocPair = {
    routeDocPair.copy(
      oldRoute = withoutTrackPathIds(routeDocPair.oldRoute),
      newRoute = withoutTrackPathIds(routeDocPair.newRoute)
    )
  }

  private def withoutTrackPathIds(routeDoc: RouteDoc): RouteDoc = {
    val analysis = routeDoc.analysis
    val map = analysis.map
    routeDoc.copy(
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
    parts.map(NodeUtil.normalize).mkString("-")
  }

  private def normalizeNodes(nodes: Seq[RouteNetworkNodeInfo]): Seq[RouteNetworkNodeInfo] = {
    nodes.map(node =>
      node.copy(
        name = NodeUtil.normalize(node.name),
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

  private def matchingMapNodes(routeDocPair: RouteDocPair): Boolean = {

    val oldMapNodes = toRouteMapNodes(routeDocPair.oldRoute)
    val newMapNodes = toRouteMapNodes(routeDocPair.newRoute)

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

  private def matchingPaths(routeDocPair: RouteDocPair): Boolean = {

    if (isFreeRoute(routeDocPair.newRoute)) {
      val oldTrackPathNodes = trackPathNodess(routeDocPair.oldRoute)
      val newTrackPathNodes = trackPathNodess(routeDocPair.oldRoute)
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

  private def trackPathNodess(routeDoc: RouteDoc): Seq[TrackPathNodes] = {
    val map = routeDoc.analysis.map

    val paths = map.freePaths ++
      map.forwardPath.toSeq ++
      map.backwardPath.toSeq ++
      map.startTentaclePaths ++
      map.endTentaclePaths

    paths.map(path => TrackPathNodes(path.startNodeId, path.endNodeId)).sortBy(nodes => s"${nodes.startNodeId}-${nodes.startNodeId}")
  }


  private def isFreeRoute(routeDoc: RouteDoc): Boolean = {
    routeDoc.analysis.map.freeNodes.nonEmpty &&
      routeDoc.analysis.map.startNodes.isEmpty &&
      routeDoc.analysis.map.endNodes.isEmpty &&
      routeDoc.analysis.map.startTentacleNodes.isEmpty &&
      routeDoc.analysis.map.endTentacleNodes.isEmpty
  }

  private def matchingMapTentacles(routeDocPair: RouteDocPair): Boolean = {
    matchingMapStartTentacles(routeDocPair) && matchingMapEndTentacles(routeDocPair)
  }

  private def matchingMapStartTentacles(routeDocPair: RouteDocPair): Boolean = {
    val oldPaths = routeDocPair.oldRoute.analysis.map.startTentaclePaths
    val newPaths = routeDocPair.newRoute.analysis.map.startTentaclePaths
    matchingPaths("start tentacles", oldPaths, newPaths)
  }

  private def matchingMapEndTentacles(routeDocPair: RouteDocPair): Boolean = {
    val oldPaths = routeDocPair.oldRoute.analysis.map.endTentaclePaths
    val newPaths = routeDocPair.newRoute.analysis.map.endTentaclePaths
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

  private def toRouteMapNodes(routeDoc: RouteDoc): RouteMapNodes = {
    val routeMap = routeDoc.analysis.map
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

  private def ignoreMapNodes(routeDocPair: RouteDocPair): RouteDocPair = {
    routeDocPair.copy(
      oldRoute = withoutMapNodes(routeDocPair.oldRoute),
      newRoute = withoutMapNodes(routeDocPair.newRoute)
    )
  }

  private def withoutMapNodes(routeDoc: RouteDoc): RouteDoc = {
    val map = routeDoc.analysis.map
    routeDoc.copy(
      analysis = routeDoc.analysis.copy(
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

  private def ignoreFreeRoutePaths(routeDocPair: RouteDocPair): RouteDocPair = {
    if (isFreeRoute(routeDocPair.newRoute)) {
      routeDocPair.copy(
        oldRoute = withoutFreeRoutePaths(routeDocPair.oldRoute),
        newRoute = withoutFreeRoutePaths(routeDocPair.newRoute)
      )
    }
    else {
      routeDocPair
    }
  }

  private def withoutFreeRoutePaths(routeDoc: RouteDoc): RouteDoc = {
    val map = routeDoc.analysis.map
    routeDoc.copy(
      analysis = routeDoc.analysis.copy(
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

  private def normalizeRouteTags(routeDocPair: RouteDocPair): RouteDocPair = {
    routeDocPair.copy(
      oldRoute = normalizeRouteDocTags(routeDocPair.oldRoute),
      newRoute = normalizeRouteDocTags(routeDocPair.newRoute)
    )
  }

  private def normalizeRouteDocTags(routeDoc: RouteDoc): RouteDoc = {
    routeDoc.copy(
      summary = routeDoc.summary.copy(
        tags = normalizeTags(routeDoc.summary.tags)
      ),
      tags = normalizeTags(routeDoc.tags)
    )
  }

  private def normalizeTags(tags: Tags): Tags = {
    Tags(tags.tags.sortBy(_.key))
  }

  private def matchingFacts(routeDocPair: RouteDocPair): Boolean = {
    val obsoleteFacts = Seq(RouteInvalidSortingOrder)

    val oldFacts = routeDocPair.oldRoute.facts.filterNot(obsoleteFacts.contains).sortBy(_.name)
    val newFacts = routeDocPair.newRoute.facts.sortBy(_.name)

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

  private def ignoreFacts(routeDocPair: RouteDocPair): RouteDocPair = {
    routeDocPair.copy(
      oldRoute = routeDocPair.oldRoute.copy(
        facts = Seq.empty
      ),
      newRoute = routeDocPair.oldRoute.copy(
        facts = Seq.empty
      )
    )
  }
}
