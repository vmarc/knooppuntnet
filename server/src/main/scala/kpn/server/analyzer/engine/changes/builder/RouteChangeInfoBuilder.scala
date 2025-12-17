package kpn.server.analyzer.engine.changes.builder

import kpn.api.common.changes.ChangeSetInfo
import kpn.api.common.changes.details.BaseRouteChange
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.route.RouteChangeInfo

object RouteChangeInfoBuilder {
  def build(
    rowIndex: Long,
    routeChange: RouteChange,
    baseRouteChangeOption: Option[BaseRouteChange],
    changeSetInfos: Seq[ChangeSetInfo]
  ): RouteChangeInfo = {

    val changeSetInfo = changeSetInfos.find(changeSetInfo => changeSetInfo.id == routeChange.key.changeSetId)
    val comment = changeSetInfo.flatMap(_.tagValue("comment"))

    val routeData = (routeChange.before, routeChange.after) match {
      case (None, Some(after)) => after
      case (Some(before), None) => before
      case (Some(before), Some(after)) => after
    }

    RouteChangeInfo(
      rowIndex = rowIndex,
      id = routeData.relationId,
      version = routeData.raw.version,
      changeKey = routeChange.key,
      changeType = routeChange.changeType,
      comment = comment,
      before = routeChange.before.map(_.raw.meta),
      after = routeChange.after.map(_.raw.meta),
      diffs = routeChange.diffs,
      routeData.networkNodes,
      routeChange.nodeChanges,
      changeSetInfo = changeSetInfo,
      wayDiffs = baseRouteChangeOption.flatMap(_.wayDiffs),
      geometryDiff = baseRouteChangeOption.flatMap(_.geometryDiff),
      bounds = baseRouteChangeOption.flatMap(_.bounds),
      happy = routeChange.happy,
      investigate = routeChange.investigate
    )
  }
}
