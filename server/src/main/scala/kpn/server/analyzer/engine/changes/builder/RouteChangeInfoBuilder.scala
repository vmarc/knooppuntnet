package kpn.server.analyzer.engine.changes.builder

import kpn.api.common.Bounds
import kpn.api.common.ElementChangeType
import kpn.api.common.LatLon
import kpn.api.common.changes.ChangeSetInfo
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.diff.WayInfo
import kpn.api.common.route.GeometryDiffAnalyzer
import kpn.api.common.route.RouteChangeInfo
import kpn.api.common.route.RouteNodeChange
import kpn.api.common.route.WayGeometry

class RouteChangeInfoBuilder {

  def build(index: Long, routeChange: RouteChange, changeSetInfos: Seq[ChangeSetInfo]): RouteChangeInfo = {

    val changeSetInfo = changeSetInfos.find(changeSetInfo => changeSetInfo.id == routeChange.key.changeSetId)

    if (routeChange.before.isDefined && routeChange.after.isDefined) {

      val before = routeChange.before.get
      val after = routeChange.after.get

      val waysBefore = WayGeometry.from(before)
      val waysAfter = WayGeometry.from(after)
      val geometryDiff = new GeometryDiffAnalyzer().analysis(waysBefore, waysAfter)
      val comment = changeSetInfo.flatMap(_.tagValue("comment"))

      val allNodes = before.networkNodes ++ after.networkNodes
      val allNodeIds = allNodes.map(_.nodeId).distinct

      val nodeIdsAdded = routeChange.diffs.nodeDiffs.flatMap(_.added.map(_.id))
      val nodeIdsRemoved = routeChange.diffs.nodeDiffs.flatMap(_.removed.map(_.id))

      val nodeChanges = allNodeIds.flatMap { nodeId =>
        val changeType = if (nodeIdsAdded.contains(nodeId)) {
          ElementChangeType.Added
        } else if (nodeIdsRemoved.contains(nodeId)) {
          ElementChangeType.Removed
        } else {
          before.networkNodes.find(_.nodeId == nodeId) match {
            case None => ElementChangeType.Unchanged
            case Some(nodeBefore) =>
              after.networkNodes.find(_.nodeId == nodeId) match {
                case None => ElementChangeType.Unchanged
                case Some(nodeAfter) =>
                  if (nodeBefore.latitude == nodeAfter.latitude && nodeBefore.longitude == nodeAfter.longitude) {
                    ElementChangeType.Unchanged
                  }
                  else {
                    ElementChangeType.Changed
                  }
              }
          }
        }
        val node = if (changeType == ElementChangeType.Removed) {
          before.networkNodes.find(_.nodeId == nodeId)
        }
        else {
          after.networkNodes.find(_.nodeId == nodeId)
        }

        node.map { node =>
          RouteNodeChange(
            nodeId,
            node.latitude,
            node.longitude,
            changeType
          )
        }
      }

      val bounds = {
        val latLons = geometryDiff match {
          case Some(diff) =>
            // note that the 'common' points are not taken into account here, so that we zoom in on the actual changes
            val segments = diff.before ++ diff.after
            if (segments.nonEmpty) {
              segments.flatMap(s => Seq(s.p1, s.p2))
            }
            else {
              val allSegments = diff.before ++ diff.after ++ diff.common
              allSegments.flatMap(s => Seq(s.p1, s.p2))
            }
          case None =>
            Seq.empty
        }
        Bounds.from(latLons)
      }

      RouteChangeInfo(
        index,
        after.relationId,
        after.meta.version,
        routeChange.key,
        routeChange.changeType,
        comment,
        routeChange.before.map(_.meta),
        routeChange.after.map(_.meta),
        routeChange.removedWays.map(WayInfo.from),
        routeChange.addedWays.map(WayInfo.from),
        routeChange.updatedWays,
        routeChange.diffs,
        after.networkNodes,
        nodeChanges,
        changeSetInfo,
        geometryDiff = geometryDiff,
        bounds = bounds,
        happy = routeChange.happy,
        investigate = routeChange.investigate
      )
    }
    else if (routeChange.before.isDefined) {

      val routeData = routeChange.before.get
      val ways = WayGeometry.from(routeData)
      val geometryDiff = new GeometryDiffAnalyzer().analysis(Seq.empty, ways)
      val comment = changeSetInfo.flatMap(_.tagValue("comment"))

      val bounds = {
        val nodeLatLons: Seq[LatLon] = routeData.networkNodes
        val wayLatLons: Seq[LatLon] = ways.flatMap(_.nodes)
        val latLons = (nodeLatLons ++ wayLatLons).distinct
        Bounds.from(latLons)
      }

      val nodeChanges = routeData.networkNodes.map { node =>
        RouteNodeChange(
          node.nodeId,
          node.latitude,
          node.longitude,
          ElementChangeType.Removed
        )
      }

      RouteChangeInfo(
        index,
        routeData.relationId,
        routeData.meta.version,
        routeChange.key,
        routeChange.changeType,
        comment,
        routeChange.before.map(_.meta),
        routeChange.after.map(_.meta),
        routeChange.removedWays.map(WayInfo.from),
        routeChange.addedWays.map(WayInfo.from),
        routeChange.updatedWays,
        routeChange.diffs,
        routeData.networkNodes,
        nodeChanges,
        changeSetInfo,
        geometryDiff = geometryDiff,
        bounds = bounds,
        happy = routeChange.happy,
        investigate = routeChange.investigate
      )
    }
    else if (routeChange.after.isDefined) {

      val routeData = routeChange.after.get
      val ways = WayGeometry.from(routeData)
      val geometryDiff = new GeometryDiffAnalyzer().analysis(Seq.empty, ways)
      val comment = changeSetInfo.flatMap(_.tagValue("comment"))

      val bounds = {
        val nodeLatLons: Seq[LatLon] = routeData.networkNodes
        val wayLatLons: Seq[LatLon] = ways.flatMap(_.nodes)
        val latLons = (nodeLatLons ++ wayLatLons).distinct
        Bounds.from(latLons)
      }

      val nodeChanges = routeData.networkNodes.map { node =>
        RouteNodeChange(
          node.nodeId,
          node.latitude,
          node.longitude,
          ElementChangeType.Added
        )
      }

      RouteChangeInfo(
        index,
        routeData.relationId,
        routeData.meta.version,
        routeChange.key,
        routeChange.changeType,
        comment,
        routeChange.before.map(_.meta),
        routeChange.after.map(_.meta),
        routeChange.removedWays.map(WayInfo.from),
        routeChange.addedWays.map(WayInfo.from),
        routeChange.updatedWays,
        routeChange.diffs,
        routeData.networkNodes,
        nodeChanges,
        changeSetInfo,
        geometryDiff = geometryDiff,
        bounds = bounds,
        happy = routeChange.happy,
        investigate = routeChange.investigate
      )
    }
    else {
      throw new IllegalStateException(s"Cannot derive RouteChangeInfo from RouteChange $routeChange")
    }
  }
}
