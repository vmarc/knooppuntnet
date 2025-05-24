package kpn.server.analyzer.engine.changes.builder

import kpn.api.common.ElementChangeType
import kpn.api.common.changes.ChangeSetInfo
import kpn.api.common.changes.details.BaseRouteChange
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.diff.WayDiffsInfo
import kpn.api.common.route.RouteChangeInfo
import kpn.api.common.route.RouteNodeChange

class RouteChangeInfoBuilder {

  def build(
    index: Long,
    routeChange: RouteChange,
    baseRouteChangeOption: Option[BaseRouteChange],
    changeSetInfos: Seq[ChangeSetInfo]
  ): RouteChangeInfo = {

    val changeSetInfo = changeSetInfos.find(changeSetInfo => changeSetInfo.id == routeChange.key.changeSetId)

    if (routeChange.before.isDefined && routeChange.after.isDefined) {

      val before = routeChange.before.get
      val after = routeChange.after.get

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

      RouteChangeInfo(
        index,
        after.relationId,
        after.meta.version,
        routeChange.key,
        routeChange.changeType,
        comment,
        routeChange.before.map(_.meta),
        routeChange.after.map(_.meta),
        routeChange.diffs,
        after.networkNodes,
        nodeChanges,
        changeSetInfo,
        baseRouteChangeOption.map(_.wayDiffs).getOrElse(WayDiffsInfo.empty),
        geometryDiff = baseRouteChangeOption.map(_.geometryDiff),
        bounds = baseRouteChangeOption.flatMap(_.bounds),
        happy = routeChange.happy,
        investigate = routeChange.investigate
      )
    }
    else if (routeChange.before.isDefined) {

      val routeData = routeChange.before.get
      val comment = changeSetInfo.flatMap(_.tagValue("comment"))

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
        routeChange.diffs,
        routeData.networkNodes,
        nodeChanges,
        changeSetInfo,
        wayDiffs = baseRouteChangeOption.map(_.wayDiffs).getOrElse(WayDiffsInfo.empty),
        geometryDiff = baseRouteChangeOption.map(_.geometryDiff),
        bounds = baseRouteChangeOption.flatMap(_.bounds),
        happy = routeChange.happy,
        investigate = routeChange.investigate
      )
    }
    else if (routeChange.after.isDefined) {

      val routeData = routeChange.after.get
      val comment = changeSetInfo.flatMap(_.tagValue("comment"))
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
        routeChange.diffs,
        routeData.networkNodes,
        nodeChanges,
        changeSetInfo,
        wayDiffs = baseRouteChangeOption.map(_.wayDiffs).getOrElse(WayDiffsInfo.empty),
        geometryDiff = baseRouteChangeOption.map(_.geometryDiff),
        bounds = baseRouteChangeOption.flatMap(_.bounds),
        happy = routeChange.happy,
        investigate = routeChange.investigate
      )
    }
    else {
      throw new IllegalStateException(s"Cannot derive RouteChangeInfo from RouteChange $routeChange")
    }
  }
}
