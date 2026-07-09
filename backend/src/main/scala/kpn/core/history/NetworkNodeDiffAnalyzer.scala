package kpn.core.history

import kpn.api.common.RouteType
import kpn.api.common.common.Ref
import kpn.api.common.diff.TagDiffs
import kpn.api.common.diff.network.NetworkNodeDiff
import kpn.api.common.diff.network.NodeIntegrityCheckDiff
import kpn.api.common.diff.network.NodeRouteReferenceDiffs
import kpn.core.analysis.NetworkNodeInfo
import kpn.core.doc.RouteDoc

class NetworkNodeDiffAnalyzer(routeType: RouteType, before: NetworkNodeInfo, after: NetworkNodeInfo) {

  def diffs: NetworkNodeDiff = NetworkNodeDiff(
    connection,
    roleConnection,
    definedInNetworkRelation,
    routeReferenceDiffs,
    nodeIntegrityCheckDiff,
    tagDiffs
  )

  private val beforeNode = before.networkNode.node
  private val afterNode = after.networkNode.node

  private def connection: Option[Boolean] = {
    Option.when(before.connection != after.connection) {
      after.connection
    }
  }

  private def roleConnection: Option[Boolean] = {
    if (before.roleConnection != after.roleConnection) {
      Some(after.roleConnection)
    }
    else {
      None
    }
  }

  private def definedInNetworkRelation: Option[Boolean] = {
    Option.when(before.definedInRelation != after.definedInRelation) {
      after.definedInRelation
    }
  }

  private def routeReferenceDiffs: Option[NodeRouteReferenceDiffs] = {
    val beforeRouteIds = before.referencedInRoutes.map(_._id).toSet
    val afterRouteIds = after.referencedInRoutes.map(_._id).toSet

    Option.when(beforeRouteIds != afterRouteIds) {
      val removedIds = beforeRouteIds -- afterRouteIds
      val addedIds = afterRouteIds -- beforeRouteIds
      val remainingIds = afterRouteIds intersect beforeRouteIds

      val removedRouteRefs = routeRefs(before.referencedInRoutes, removedIds)
      val addedRouteRefs = routeRefs(after.referencedInRoutes, addedIds)
      val remainingRouteRefs = routeRefs(after.referencedInRoutes, remainingIds)

      NodeRouteReferenceDiffs(removedRouteRefs, addedRouteRefs, remainingRouteRefs)
    }
  }

  private def routeRefs(routes: Seq[RouteDoc], ids: Set[Long]): Seq[Ref] = {
    routes.filter(route => ids.contains(route._id)).map(_.toRef)
  }

  private def tagDiffs: Option[TagDiffs] = {
    new TagDiffAnalyzer(beforeNode, afterNode, NodeTagDiffAnalyzer.mainTagKeys).diffs
  }

  private def nodeIntegrityCheckDiff: Option[NodeIntegrityCheckDiff] = {
    Option.when(before.integrityCheck != after.integrityCheck) {
      NodeIntegrityCheckDiff(before.integrityCheck, after.integrityCheck)
    }
  }
}
