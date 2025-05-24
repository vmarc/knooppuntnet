package kpn.server.analyzer.engine.changes.builder

import kpn.api.common.ElementChangeType
import kpn.api.common.changes.ChangeSetInfo
import kpn.api.common.changes.details.BaseRouteChange
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.diff.RouteData
import kpn.api.common.route.RouteChangeInfo
import kpn.api.common.route.RouteNodeChange

object RouteChangeInfoBuilder {
  def build(
    rowIndex: Long,
    routeChange: RouteChange,
    baseRouteChangeOption: Option[BaseRouteChange],
    changeSetInfos: Seq[ChangeSetInfo]
  ): RouteChangeInfo = {

    val changeSetInfo = changeSetInfos.find(changeSetInfo => changeSetInfo.id == routeChange.key.changeSetId)
    val comment = changeSetInfo.flatMap(_.tagValue("comment"))

    new RouteChangeInfoBuilder(
      rowIndex,
      routeChange,
      baseRouteChangeOption,
      changeSetInfo,
      comment
    ).build()
  }
}

class RouteChangeInfoBuilder(
  rowIndex: Long,
  routeChange: RouteChange,
  baseRouteChangeOption: Option[BaseRouteChange],
  changeSetInfo: Option[ChangeSetInfo],
  comment: Option[String],
) {

  def build(): RouteChangeInfo = {
    (routeChange.before, routeChange.after) match {
      case (None, Some(after)) => buildRouteChangeCreate(after)
      case (Some(before), None) => updateRouteChangeDelete(before)
      case (Some(before), Some(after)) => buildRouteChangeUpdate(before, after)
      case _ =>
        throw new IllegalStateException(s"Cannot derive RouteChangeInfo from RouteChange $routeChange")
    }
  }

  private def updateRouteChangeDelete(before: RouteData): RouteChangeInfo = {
    val nodeChanges = before.networkNodes.map { node =>
      RouteNodeChange(
        node.nodeId,
        node.latitude,
        node.longitude,
        ElementChangeType.Added
      )
    }

    RouteChangeInfo(
      rowIndex,
      before.relationId,
      before.meta.version,
      routeChange.key,
      routeChange.changeType,
      comment,
      routeChange.before.map(_.meta),
      routeChange.after.map(_.meta),
      routeChange.diffs,
      before.networkNodes,
      nodeChanges,
      changeSetInfo,
      wayDiffs = baseRouteChangeOption.flatMap(_.wayDiffs),
      geometryDiff = baseRouteChangeOption.flatMap(_.geometryDiff),
      bounds = baseRouteChangeOption.flatMap(_.bounds),
      happy = routeChange.happy,
      investigate = routeChange.investigate
    )
  }

  private def buildRouteChangeCreate(after: RouteData): RouteChangeInfo = {

    val nodeChanges = after.networkNodes.map { node =>
      RouteNodeChange(
        node.nodeId,
        node.latitude,
        node.longitude,
        ElementChangeType.Removed
      )
    }

    RouteChangeInfo(
      rowIndex,
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
      wayDiffs = baseRouteChangeOption.flatMap(_.wayDiffs),
      geometryDiff = baseRouteChangeOption.flatMap(_.geometryDiff),
      bounds = baseRouteChangeOption.flatMap(_.bounds),
      happy = routeChange.happy,
      investigate = routeChange.investigate
    )
  }

  private def buildRouteChangeUpdate(before: RouteData, after: RouteData): RouteChangeInfo = {

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
      rowIndex = rowIndex,
      id = after.relationId,
      version = after.meta.version,
      changeKey = routeChange.key,
      changeType = routeChange.changeType,
      comment = comment,
      before = routeChange.before.map(_.meta),
      after = routeChange.after.map(_.meta),
      diffs = routeChange.diffs,
      nodes = after.networkNodes,
      nodeChanges = nodeChanges,
      changeSetInfo = changeSetInfo,
      wayDiffs = baseRouteChangeOption.flatMap(_.wayDiffs),
      geometryDiff = baseRouteChangeOption.flatMap(_.geometryDiff),
      bounds = baseRouteChangeOption.flatMap(_.bounds),
      happy = routeChange.happy,
      investigate = routeChange.investigate
    )
  }
}
